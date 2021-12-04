package dungeonmania.movingEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.battleStates.BattleState;
import dungeonmania.movingEntity.battleStates.NormalState;
import dungeonmania.movingEntity.moveBehaviour.MoveAwayBehavior;
import dungeonmania.movingEntity.moveBehaviour.MoveTowardPlayerBehavior;
import dungeonmania.movingEntity.moveBehaviour.MovingBehavior;
import dungeonmania.movingEntity.moveBehaviour.RandomMoveBehavior;
import dungeonmania.movingEntity.movingEntityStates.*;
import dungeonmania.movingEntity.movingEntityStates.MovingEntityState;
import dungeonmania.movingEntity.observerPatterns.DefenderObserver;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.movingEntity.observerPatterns.AttackerSubject;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.collectableEntity.Armour;
import dungeonmania.staticEntity.collectableEntity.CollectableEntity;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity implements MovingEntityState, AttackerSubject, DefenderObserver{
    /**
     * Author: Alan
     * Date: 28/10/2021
     */
    private int health;
    private int battleRadius;
    private int attackDamage;
    private Position prev_position;
    private Inventory inventory;
    private int damage_divisor;
    private int defense = 0;
    private BattleBehavior battleBehavior;
    private Position nextPosition;

    protected MovingEntityState enemyState;
    protected MovingEntityState allyState;
    protected MovingEntityState state;

    protected NormalState normalState;
    protected BattleState currBattleState;
    
    protected MovingBehavior movingBehavior;

    ArrayList<MovingEntity> listBattleObservers = new ArrayList<>();
    ArrayList<MovingEntity> listBattleSubjects = new ArrayList<>();


    public MovingEntity(int id, String entityType, Position curr_position, int health, int battleRadius, int attackDamage) {
        super(id, entityType, curr_position);
        enemyState = new EnemyState(this);
        allyState = new AllyState(this);
        this.inventory = new Inventory();

        prev_position = curr_position;  //when just intiialized an movingEntity, the previous position = curr position.
        this.health = health;
        this.battleRadius = battleRadius;
        this.attackDamage = attackDamage;

        //inititalize the state (which team they belong)
        enemyState = new EnemyState(this);
        allyState = new AllyState(this);

        //initialize the battle states
        normalState = new NormalState(this);
        currBattleState = normalState;

        //player has ally state, other entities are intiialized as enemyState
        /**
         * Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
         * enemy Health = Character Health - ((character Health * character Attack Damage) / 5)
         */
        if(!entityType.contains("player")) {
            state = enemyState;
            damage_divisor = 5;
        }
        else {
            state = allyState;
            damage_divisor = 10;
        }
    }

    //========================
    //========Movement========
    //========================
    public abstract void doMove(Dungeon dungeon);                          //for other monsters who don't care the location of the player, but it needs to detect the barriers
    public abstract void doMove(Player player, Dungeon dungeon);           //for mercenary, it needs to know the way

    public MovingBehavior getMovingBehavior() {
		return this.movingBehavior;
	}

	public void setMovingBehavior(MovingBehavior movingBehavior) {
		this.movingBehavior = movingBehavior;
	}

    public void runTowardPlayer(Dungeon dungeon) {
        MovingBehavior moveTowardPlayerBehavior = (MovingBehavior) new MoveTowardPlayerBehavior(dungeon, this, getCurr_position());
        this.setMovingBehavior(moveTowardPlayerBehavior);

        //call the controleld moveBehavior
        getMovingBehavior().makeMove();
    }

    public void randomMove(Dungeon dungeon) {
        MovingBehavior randomMoveBehavior = (MovingBehavior) new RandomMoveBehavior(dungeon, this, getCurr_position());
        this.setMovingBehavior(randomMoveBehavior);

        //call the random moveBehavior
        getMovingBehavior().makeMove();
    }

    /**
     * Using strategy pattern to call different behaviors based on the condition of wheter moving toward/away from player
     * @param dungeon
     */
    public void runAway(Dungeon dungeon) {
        MovingBehavior moveAwayPlayerBehavior = (MovingBehavior) new MoveAwayBehavior(dungeon, this, getCurr_position());
        this.setMovingBehavior(moveAwayPlayerBehavior);

        //call the movingAway moveBehavior
        getMovingBehavior().makeMove();
    }

    public void randomMovebehavior(Dungeon dungeon) {
        MovingBehavior randomMoveBehavior = (MovingBehavior) new RandomMoveBehavior(dungeon, this, getCurr_position());
        this.setMovingBehavior(randomMoveBehavior);

        //call the randomMoveBehavior moveBehavior
        getMovingBehavior().makeMove();
    }

    //========================
    //========Battle==========
    //========================
    @Override
    public void attachBattleEntity(MovingEntity observer) {
        listBattleObservers.add(observer);
    }

    @Override
    public void detachBattleEntity(MovingEntity observer) {
        listBattleObservers.remove(observer);
    }

    @Override
    public void notifyObserversBattle(Dungeon dungeon) {
        for(MovingEntity mv : listBattleObservers) {
            //it will not attack itself
            if(!mv.equals(this)) {
                //attack all enemies if it is ally.
                //attack all ally if it's enemy
                boolean allEnemey = (getState() instanceof EnemyState && mv.getState() instanceof EnemyState);
                boolean allAlly = (getState() instanceof AllyState && mv.getState() instanceof AllyState);
                if(!(allEnemey) && !(allAlly)) {
                    //add dungeon and battleBehavior to the state pattern
                    this.battleBehavior = new BattleBehavior(dungeon, this, mv);
                    currBattleState.addBattleBehavior(battleBehavior);
                    currBattleState.addDungeon(dungeon);
                    MovingEntity player = findPlayer(mv);
                    // System.out.println("MovingEntity.notifyObserversBattle --> player currBattleState = " + player.getCurrBattleState());
                    MovingEntity otherEntity = findOtherEntity(mv);
                    if(player != null) {
                        // System.out.println("-->MovingEntity.notifyObserversBattle --> player = " + player + ", otherEntity = " + otherEntity);
                        player.battleBehavior = new BattleBehavior(dungeon, player, otherEntity);
                        player.getCurrBattleState().addBattleBehavior(player.battleBehavior);
                        player.getCurrBattleState().addDungeon(dungeon);
                        player.getCurrBattleState().doBattle(otherEntity);
                    }
                    else {
                        currBattleState.doBattle(mv);
                    }
                }
            }
        }
    }

    public void battleMethology(MovingEntity target, Dungeon dungeon) {
        while(this.getHealth() > 0 && target.getHealth() > 0) {
            if(battleBehavior == null) throw new IllegalAccessError("BattleBehavior needs to be initialized first");

            //update the health
            battleBehavior.updateHealth(target);
            //TODO: Refactoring the following method in the BattleBehavior class
            // battleBehavior.updateWeaponArmour(target, dungeon);
        }
    }

    public void removeOtherEntityIfDie(MovingEntity target, Dungeon dungeon) {
        if(target.getHealth() <= 0) {
            if (zombieToastHasArmor(target)) {
                this.storeArmour(target, dungeon);
            }
            if (mercenaryHasArmour(target)) {
                // this.storeArmour(target, dungeon);
                Mercenary temp = (Mercenary) target;
                temp.dropArmour(dungeon);
            }

            //update dungeon when a enemy die
            dungeon.deleteEntity(target);
        }
    }

    public boolean zombieToastHasArmor(MovingEntity entity) {
        return entity instanceof ZombieToast && ((ZombieToast) entity).hasArmour();
    }

    public boolean mercenaryHasArmour(MovingEntity entity) {
        return entity instanceof Mercenary && ((Mercenary) entity).hasArmour();
    }

    public void removePlayerIfDie(Player character, Dungeon dungeon) {
        if(character.getHealth() <= 0) {
            // Use the one ring
            CollectableEntity oneRing = character.getOneRing();
            if(oneRing != null) {
                character.getOneRing().doUse(character);
                return;
            }
            //update dungeon when a char die
            dungeon.deleteEntity(character);
        }
    }

    public void storeArmour(MovingEntity target, Dungeon dungeon) {
        dungeon.getPlayer().getInventory().storeToInventory(new Armour(1000000928, new Position(-2,-2)));
    }

    public Player findPlayer(MovingEntity defender) {
        if(defender instanceof Player) {
            return (Player)defender;
        }
        if(this instanceof Player) {
            return (Player)this;
        }
        return null;
    }

    public MovingEntity findOtherEntity(MovingEntity defender) {
        if(!(defender instanceof Player)) return defender;
        if(!(this instanceof Player)) return this;
        return null;
    }

    public void setAttackDamage(int damage) {
		this.attackDamage = damage;
	}

    public int getBattleRadius() {
		return this.battleRadius;
	}

	public void setBattleRadius(int battleRadius) {
		this.battleRadius = battleRadius;
	}

    public int getDamage_divisor() {
		return this.damage_divisor;
	}

    public NormalState getNormalState() {
		return this.normalState;
	}

	public BattleState getCurrBattleState() {
		return this.currBattleState;
	}

	public void setCurrBattleState(BattleState currBattleState) {
		this.currBattleState = currBattleState;
	}

    public int getDefense() {
		return this.defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

    public int getAttackDamage() {
		return this.attackDamage;
	}

    //getters and setters
    public int getHealth() {
		return this.health;
	}

    public void setHealth(int health) {
		this.health = health;
	}

    //========================
    //=======Inventory========
    //========================
    public List<ItemResponse> getItemResponses() {
        return inventory.getItemResponses();
    }

    public void equip(StaticEntity staticEntity) {
        getInventory().storeToInventory(staticEntity);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean hasItem(int itemID) {
        if(inventory.getStaticEntity(itemID) != null) return true;
        return false; 
    }

    public Position getPrev_position() {
		return this.prev_position;
	}

	public void setPrev_position(Position prev_position) {
		this.prev_position = prev_position;
	}

    public void setNextPosition(Position nextPosition) {
        this.nextPosition = nextPosition;
    }

    public Position getNextPosition() {
        return nextPosition;
    }


    public void setState(MovingEntityState state) {
        this.state = state;
        //ally has to be blocking
        this.setCurrBlockingState(blockingState);
    }

    public MovingEntityState getState() {
        return state;
    }

    public BattleState getBattleState() {
        return this.currBattleState;
    }

    /**
     * 
     * @param movingEntity
     */
    public void equipArmour(Dungeon dungeon) {
        this.equip(new Armour(dungeon.uniqueEntityID(), new Position(-2, -2))); //the position for inside inventory
    }

}
