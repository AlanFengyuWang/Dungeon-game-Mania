package dungeonmania.movingEntity.moveBehaviour;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.Wall;
import dungeonmania.staticEntity.collectableEntity.CollectableEntity;
import dungeonmania.util.Position;

public class ControlledMoveBehavior extends MovingBehavior{
    // private MovingEntity player;
    /**
     * This is only for the current player, so it can pick up items
     */
    public ControlledMoveBehavior(Dungeon dungeon, MovingEntity movingEntity, Position currPosition) {
        super(dungeon, movingEntity, currPosition);
    }

    public void makeMove() {
        //player default move
        if(player.getDirection() == null) throw new IllegalAccessError("The player's direction is not initialized");
        Position direction = player.getDirection().getOffset();
        int newX = getCurrPosition().getX() + direction.getX();
        int newY = getCurrPosition().getY() + direction.getY();

        Position final_position = new Position(newX, newY);
        List<Entity> entities = dungeon.getEntities(final_position);

        //it's ground or an enemy, player can move
        if (entities.isEmpty() || dungeon.getSwamp(final_position) != null) {
            updatePosition(final_position, dungeon, false);
        }

        else {
            for(Entity entity : entities) {
                //hit the wall
                if(entity != null && entity instanceof Wall) {
                    return;
                }
                else {
                    //if it's collectable, it will go to the player's inventory
                    if(entity instanceof CollectableEntity) {
                        CollectableEntity ce = (CollectableEntity) entity;

                        if (ce instanceof CollectableEntity) {  
                            // System.out.println("Player.doMove --> doCollect");     
                                 
                            ce.doCollect(player, dungeon);
                            
                            //remove the collectable entity after picked up
                            dungeon.deleteEntity(entity);
                        } 
                        //update the player position
                        updatePosition(final_position, dungeon, false);
                    }
                    //the player interact with each entity in the new position accordingly
                    //if the player is under other's interactable range or other entities are under player's interactable range, they will react
                    interactEachEntities(entity.getCurr_position(), entity);
                }
            }
        }
    }
}
