package dungeonmania.staticEntity.collectableEntity;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.TickItemUse;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity implements TickItemUse{
    /**
     * Constructor for HealthPotion
     * @param posix_entityType is the string that is after the entityType "HealthPotion_1"
     */

    public HealthPotion(int id, Position curr_position) {
        super(id, "health_potion", curr_position);
    }

    /**
     * Note: it will be picked up in the player class
     */
    @Override   
    public void doInteract(Entity entity, Dungeon dungeon) {
    }

    public void restoreToFullHealth(Player player) {
        player.setHealth(player.getFull_health());
    }

    @Override
    public void doUse(Entity playerEntity) {
        Player player = (Player) playerEntity;
        Inventory inventory = player.getInventory();

        //increase the health once only
        restoreToFullHealth(player);

        //update the inventory
        inventory.removeItem((StaticEntity)this);
    }

    @Override
    public void doItemUse(Player player, Dungeon dungeon) {
        doUse((Entity)player);
    }
}
