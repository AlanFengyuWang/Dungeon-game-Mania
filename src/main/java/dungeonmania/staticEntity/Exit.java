package dungeonmania.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class Exit extends StaticEntity {
    /**
     * Constructor for Exit
     * @param posix_entityType is the string that is after the entityType "Exit_1"
     */

    public Exit(int id, Position curr_position) {
        super(id, "exit", curr_position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // ========== Skin Management ==========
        if (entity instanceof MovingEntity || entity instanceof Boulder) {
            // For skins
            // To make Moving entites + Boulder skin show over door skin
            dungeon.skinsOrdering(entity, this);
        }

        // ========== Pre conditions ==========
        // Will have to interact with the other entity instead if more than 1 entity on same tile
        // Or already have interact hence, on same tile
        if (
            dungeon.getEntities(this.getCurr_position()).size() > 1 || 
            entity.getCurr_position() == this.getCurr_position()
        ) return;
        
        // ========== Movement onto tile ==========
        // All entities can tread on exit
        if (entity instanceof Player) {
            Player player_entity = (Player) entity;
            player_entity.setPrev_position(getCurr_position());
        }
        entity.setCurr_position(this.getCurr_position());
    }
}