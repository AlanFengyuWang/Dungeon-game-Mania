package dungeonmania.staticEntity.collectableEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Player;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.TickItemUse;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity implements TickItemUse{
    private List<Entity> destroyedEntities = new ArrayList<>();
    
    /**
     * Constructor for Bomb
     * @param posix_entityType is the string that is after the entityType "Bomb_1"
     */
    public Bomb(int id, Position curr_position) {
        super(id, "bomb", curr_position);
        setCurrBlockingState(blockingState);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // MovingEntity mvEntity = (MovingEntity) entity;
        // mvEntity.setAttackDamage(mvEntity.getAttackDamage() * damage_enhance_multiply);
    }

    @Override
    public void doUse(Entity playerEntity){
        // TODO Auto-generated method stub
        
    }

    public void doItemUse(Player player, Dungeon dungeon) {
        // set position for bomb in the dungeon
        this.setCurr_position(player.getCurr_position());
        
        // Set bomb blocking state after being placed in dungeon
        this.setCurrBlockingState(blockingState);

        // remove clicked entity from inventory
        player.getInventory().useInventory(this);

        // add the cpy bomb in dungeon  
        dungeon.addEntity(this);
        
        EntityResponse er = new EntityResponse(String.valueOf(this.getId()), this.getFilteredEntityType(), player.getCurr_position(), this.IsInteractable());
        dungeon.addEntitiesResponse(er);
    }


    // Gather all existing entities adjacent to bomb that will be destroyed once switch is triggered
    public void gather(Position curr_position, Dungeon dungeon) {
        // collect entities that will be destroyed when a switch cardinally adjacent to bomb is triggered
        for (Entity entity : dungeon.getEntities()) {
            List<Position> destruction_range = curr_position.getAdjacentPositions();
            destruction_range.add(new Position(curr_position.getX(), curr_position.getY()));        
            if (!(entity instanceof Player)) {
                if (destruction_range.contains(entity.getCurr_position())) {
                    this.destroyedEntities.add(entity);
                }
            }
        }
    }

    public boolean isBombSet() {
		boolean isBombPlaced = (currBlockingState.equals(blockingState));
        return isBombPlaced;
    }

    public void setBomb() {
		currBlockingState = blockingState;
    }

    // Destroy all entities adjacent to bomb and clear the lists
    public void explode(Dungeon dungeon) {
        // Gather all entities in range
        gather(this.getCurr_position(), dungeon);
        
        // Destroy all surrounding entities 1 block away
        for (Entity entity : this.destroyedEntities) {
            dungeon.deleteEntity(entity);
        }
        this.destroyedEntities.clear();
    }
}
