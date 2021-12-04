package dungeonmania.staticEntity.collectableEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends StaticEntity {
    
    public CollectableEntity(int id, String entityType, Position curr_position) {
        super(id, entityType, curr_position);
        // this.currBlockingState = unblockingState;
    }

    /**
     * Collect the items
     */
    public void doCollect(Entity entity, Dungeon dungeon) {
        // Has to be player to collect
        if (!(entity instanceof Player)) return;
        //player picks up the Collectable entity and then store to the inventory
        Player player = (Player) entity;
        Inventory player_inventory = player.getInventory();

        // Can only have 1 key at a time
        if (this instanceof Key && player_inventory.hasItem("key")) {
            dungeon.skinsOrdering(entity, this);
            return;
        }
        //collect the item if the space is not full
        else if(player_inventory.getCurrentInventorySpace() < player_inventory.getSpace()) {
            player_inventory.storeToInventory(this);
        }

        //remove the collectable entity after picked up
        dungeon.deleteEntity(this);
    }

    /**
     * Use the item
     * @author Alan
     * @param playerEntity
     */
    public abstract void doUse(Entity playerEntity);

}
