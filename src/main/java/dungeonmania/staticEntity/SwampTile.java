package dungeonmania.staticEntity;

import java.util.HashMap;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity{
    private int movement_factor;
    private HashMap<MovingEntity, Integer> num_ticksLeftMap = new HashMap<>();

    public SwampTile(int id, Position curr_position) {
        super(id, "swamp_tile", curr_position);
        currBlockingState = unblockingState;
    }

    public SwampTile(int id, String posix_entityType, Position curr_position) {
        super(id, "swamp_tile_" + posix_entityType, curr_position);
        currBlockingState = unblockingState;
    }

    public SwampTile(int id, Position curr_position, int movement_factor) {
        super(id, "swamp_tile", curr_position);
        currBlockingState = unblockingState;
        this.movement_factor = movement_factor;
    }

    /**
     * @pre entity: MovingEntity
     */
    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        //player is not affected
        if(entity instanceof Player) return;
        
        //initialize the num ticks for the entity if it hasn't visited
        if (!num_ticksLeftMap.containsKey((MovingEntity)entity)) {
            num_ticksLeftMap.put((MovingEntity)entity, movement_factor);
        }
    }

    public boolean hasMovementFactorLeft(MovingEntity entity) {
        //player is not affected
        if(entity instanceof Player) return false;
        
        //initialize the num ticks for the entity if it hasn't been visited
        if (!num_ticksLeftMap.containsKey((MovingEntity)entity)) {
            num_ticksLeftMap.put((MovingEntity)entity, movement_factor-1);
            // System.out.println("SwampTile.hasMovementFactorLeft --> create num_ticksLeftMap = " + num_ticksLeftMap.get(entity));
            return true;
        }

        //when the entity still have movement factor left
        else if(num_ticksLeftMap.get(entity) > 0) {
            //update the map
            num_ticksLeftMap.put(entity, num_ticksLeftMap.get(entity)-1);
            // System.out.println("SwampTile.hasMovementFactorLeft --> update num_ticksLeftMap = " + num_ticksLeftMap.get(entity));
            return true;
        }
        
        //if the entity has zero movement factor, it can move
        if(num_ticksLeftMap.get(entity).equals(0)) {
            //remove the entity from the list
            // System.out.println("SwampTile.hasMovementFactorLeft --> remove num_ticksLeftMap = " + num_ticksLeftMap.get(entity));
            num_ticksLeftMap.remove(entity);
            return false;
        }
        return false;
    }

    // public void updateMovementFactor(MovingEntity entity) {
    // }

    public int getMovement_factor() {
		return this.movement_factor;
	}

	public void setMovement_factor(int movement_factor) {
		this.movement_factor = movement_factor;
	}

}
