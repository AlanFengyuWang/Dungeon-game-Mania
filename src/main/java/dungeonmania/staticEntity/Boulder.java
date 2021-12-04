package dungeonmania.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.blockingStates.UnblockState;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.collectableEntity.CollectableEntity;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity implements BlockEntity {
    /**
     * Constructor for Boulder
     * @param posix_entityType is the string that is after the entityType "Boulder_1"
     */

    public Boulder(int id, Position curr_position) {
        super(id, "boulder", curr_position);

        //change the state to blocking state
        currBlockingState = blockingState;
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // ========== Pre conditions ==========
        // No other entities can move boulders
        if (!entity.getEntityType().contains("player")) return;

        Position newPos;
        int horizontal_move = this.getCurr_position().getX() - entity.getCurr_position().getX();
        int vertical_move = this.getCurr_position().getY() - entity.getCurr_position().getY();

        if (horizontal_move != 0 && vertical_move == 0) {
            // Moving horizontally
            newPos = new Position(this.getCurr_position().getX() + horizontal_move, this.getCurr_position().getY());
        } else if (vertical_move != 0 && horizontal_move == 0) {
            // Moving vertically
            newPos = new Position(this.getCurr_position().getX(), this.getCurr_position().getY() + vertical_move);
        } else {
            // Nothing happens; Player remains in original position
            return;
        }
        
        // ========== Skin Management ==========
        // For skins
        // To make boulder skin show over switch skin
        if (
            dungeon.getEntities(newPos).size() == 1 && 
            dungeon.getEntity(newPos).getcurrBlockingState() instanceof UnblockState
        ) {
            dungeon.skinsOrdering(this, dungeon.getEntities(newPos).get(0));
        }

        // ========== Movement onto tile ==========
        Player player_entity = (Player) entity;

        if (
            dungeon.getEntities(newPos).isEmpty() || 
            (dungeon.getEntities(newPos).size() == 1 && 
            dungeon.getEntity(newPos).getcurrBlockingState() instanceof UnblockState) &&
            !(dungeon.getEntity(newPos) instanceof CollectableEntity)
        ) {
            player_entity.setPrev_position(getCurr_position());
            player_entity.setCurr_position(this.getCurr_position());
            if (dungeon.getEntity(newPos) instanceof Portal) {
                ((Portal) dungeon.getEntity(newPos)).doInteract(this, dungeon);
            }
            else this.setCurr_position(newPos);
        }
        dungeon.updateEntityResponse(this);
        return;
    }

    @Override
    public void doBlock() {
        return;
    }
}
