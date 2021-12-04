package dungeonmania.movingEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.movingEntity.battleStates.InvincibleState;
import dungeonmania.movingEntity.battleStates.InvisibleState;
import dungeonmania.movingEntity.moveBehaviour.ControlledMoveBehavior;
import dungeonmania.movingEntity.moveBehaviour.MovingBehavior;
import dungeonmania.movingEntity.movingEntityStates.EnemyState;
import dungeonmania.movingEntity.battleStates.BattleState;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.collectableEntity.CollectableEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends MovingEntity{
    /**
     * Author: Alan
     * Date: 28/10/2021 - 30/10/2021
     */
    private int full_health;
    private boolean bombIs_placed = false;
    private int defaultDamage = 30;
    private Direction direction;

    protected InvincibleState invincibleState;
    protected InvisibleState invisibleState;

    public Player(int id, Position curr_position) {
        super(id, "player", curr_position, 100, 0, 30);
        this.full_health = 100;

        //initialize the battle states
        invincibleState = new InvincibleState(this, 10);
        invisibleState = new InvisibleState(this, 10);

        //set state to ally
        state = allyState;

    }

    public Player(int id, String entityType, Position curr_position) {
        super(id, entityType, curr_position, 100, 0, 30);
        this.full_health = 100;

        //initialize the battle states
        invincibleState = new InvincibleState(this, 10);
        invisibleState = new InvisibleState(this, 10);

        //set state to ally
        state = allyState;
    }

    public void doMove(Direction d, Dungeon dungeon) {
        direction = d;
        MovingBehavior controlledMoveBehavior = (MovingBehavior) new ControlledMoveBehavior(dungeon, (MovingEntity)this, getCurr_position());
        super.setMovingBehavior(controlledMoveBehavior);

        //call the controleld moveBehavior
        super.getMovingBehavior().makeMove();

        //Check inventory for item durability
        for (StaticEntity se : getInventory().getStaticEntities()) {
            /**
             * @author William
             * @date 10/11/2021
            */
            if (se instanceof DurableEquipment) {
                DurableEquipment temp = (DurableEquipment) se;
                temp.checkDurability(this, dungeon);
            }
        }
        this.getCurrBattleState().updatePotionDuration();

    }

    public CollectableEntity getOneRing() {
        if(this.getInventory().getStaticEntity("one_ring") != null) {
            return (CollectableEntity)getInventory().getStaticEntity("one_ring");
        }
        return null;
    }

    @Override
    public void doMove(Dungeon dungeon) {
    }

    @Override
    public void doMove(Player player, Dungeon dungeon) {
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        MovingEntity movingEntity = (MovingEntity) entity;
        //if it's enemy, it will trigger the battle
        if(entity != null && movingEntity.getState() instanceof EnemyState) {
            attachBattleEntity((MovingEntity) entity);
            notifyObserversBattle(dungeon);
        }
    }

    public int getFull_health() {
		return this.full_health;
	}

	public void setFull_health(int full_health) {
		this.full_health = full_health;
	}

    public BattleState getCurrPlayerState() {
		return this.currBattleState;
	}

    public boolean getBombIs_placed() {
        return this.bombIs_placed;
    }

    public void setBombIs_placed(boolean bombIs_placed) {
        this.bombIs_placed = bombIs_placed;
    }

    public InvincibleState getInvincibleState() {
		return this.invincibleState;
	}

	public InvisibleState getInvisibleState() {
		return this.invisibleState;
	}

    public Direction getDirection() {
        return direction;
    }

    /**
     * Here we assume that this player is attacking mv. 
     */
    // @Override
    // public void battleMethology(MovingEntity mv, Dungeon dungeon) {
    // }

    public int getDefaultDamage() {
        return this.defaultDamage;
    }

}
