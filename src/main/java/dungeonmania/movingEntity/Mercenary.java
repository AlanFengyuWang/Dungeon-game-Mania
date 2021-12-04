package dungeonmania.movingEntity;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.EntityCreator;
import dungeonmania.entity.PlayerInteract;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.movingEntity.battleStates.InvincibleState;
import dungeonmania.movingEntity.battleStates.NormalState;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.collectableEntity.Armour;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements PlayerInteract{
    /**
     * @author Alan
     * @param id
     * @param curr_position
     */
    StaticEntity armour;
    private int mindcontrolled = 0;
    private Boolean hasArmour;
    private Random random;
    private Boolean isControlled = false;

    public Mercenary(int id, Position curr_position) {
        super(id, "mercenary", curr_position, 30, 2, 20);

        //change the state to blocking state
        currBlockingState = blockingState;
        this.random = new Random(System.currentTimeMillis());
        if (this.random.nextInt(100) < 15) {
            this.hasArmour = true;
        } else {
            this.hasArmour = false;
        }
        // this.hasArmour = false;
        if (this.hasArmour == true) {
            this.setDefense(60);
        }
    }

    public Mercenary(int id, String entityType, Position curr_position) {
        super(id, entityType, curr_position, 30, 2, 20);

        //change the state to blocking state
        currBlockingState = blockingState;
        this.random = new Random(System.currentTimeMillis());
        if (this.random.nextInt(100) < 15) {
            this.hasArmour = true;
        } else {
            this.hasArmour = false;
        }
        // this.hasArmour = true;
        if (this.hasArmour == true) {
            this.setDefense(60);
        }
    }

    @Override
    public void doMove(Player player, Dungeon dungeon) {
        /**
         * Case 1 if the player has invisible state, it does not move
         * 
         * Case 2 if the player has invincible state, it moves away
         * 
         * Case 3 if the player has bribed this mercenary, it moves toward the player until 2 unit away and then follow the player, 
         * it will walk one unit (left) away if the player is trying to reach it. If the left is a wall, it will hit the wall
         * when the monster is in the range of the mercenary, it will fight the enemy with the character from a distance. The enemy health decrease
         * twice in a single round, substituting the character's statistics with the ally's statistics in the above formula.
         * 
         * Case 4 If the player has not bribed the mercenary, it will come closer to the player until it hits the player and then start the battle.
         */

        //if the mercenary can see the player
        //if the mercenary is invincible

        if(player.getCurrPlayerState() instanceof InvincibleState) {
            runAway(dungeon);
        }

        else if(player.getCurrPlayerState() instanceof NormalState) {
            runTowardPlayer(dungeon);
        }

        // Mind control ticks
        if (isControlled) {
            mindcontrolled -= 1;
            if (mindcontrolled <= 0) setState(enemyState);
        }
    }

    @Override
    public void doMove(Dungeon dungeon) {
        //do nothing
    }

    public void doInteract(Entity entity, Dungeon dungeon){
        //when it's far away from the player, approaching to it
        //e is not ground
        if(entity != null) {
            //pick up armour
            if(entity instanceof Armour) {
                StaticEntity armour = (StaticEntity) entity;
                this.armour = armour;
            }

            //hit the wall

            //battle the player if it's enemy or battle the another entity if it's ally
            boolean battlePlayer = entity instanceof Player && this.state.equals(enemyState);
            boolean battleOtherEntities = entity instanceof MovingEntity && this.state.equals(allyState);
            if(battlePlayer || battleOtherEntities) {
                attachBattleEntity((MovingEntity) entity);
                notifyObserversBattle(dungeon);
            }
        }

        //update position
        // setPrev_position(getCurr_position());
        // setCurr_position(getNextPosition());
    }

    public void doBribe(Player player, Dungeon dungeon) throws InvalidActionException {
        //if this is enemy, it is bribed 
        if(getState().equals(enemyState)) {
            // If you have sun stone, make it number one priority
            if (player.getInventory().hasItem("sun_stone")) {
                //change the state
                setState(allyState);
            }
            // If you have sceptre, mind control first over bribing
            // Unlimited durability
            else if (player.getInventory().hasItem("sceptre")) {
                //change the state
                setState(allyState);
                setMindControlled(10);
                setIsControlled(true);
            }
            //reduce the treasure if the player has treasure
            else if(player.getInventory().hasItem("treasure")) {
                //use the treasure
                player.getInventory().useInventory("treasure");

                //change the state
                setState(allyState);

                // System.out.println("Mercenary.doBribe after set to ally --> getState() = " + getState());
            }
            else {
                throw new InvalidActionException("no gold available");
            }
        }
    }

    @Override
    public void doPlayerInteract(Player player, Dungeon dungoen) {
        doBribe(player, dungoen);
    }

    public void dropArmour(Dungeon dungeon) {
        if (this.hasArmour) { 
            Armour droppedArmour = new Armour(dungeon.uniqueEntityID(), this.getCurr_position());
            dungeon.addEntity(droppedArmour);
                 
            EntityResponse er = new EntityResponse(String.valueOf(droppedArmour.getId()), droppedArmour.getFilteredEntityType(), this.getCurr_position(), droppedArmour.IsInteractable());
            dungeon.addEntitiesResponse(er);
        }
    }

    public Boolean hasArmour() {
        return this.hasArmour;
    }
    
    public void setMindControlled(int length) {
        this.mindcontrolled = length;
    }

    public void setIsControlled(boolean isControlled) {
        this.isControlled = isControlled;
    }
}
