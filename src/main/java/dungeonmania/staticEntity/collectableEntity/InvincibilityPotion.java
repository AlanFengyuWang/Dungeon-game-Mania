package dungeonmania.staticEntity.collectableEntity;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.TickItemUse;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CollectableEntity implements TickItemUse{
    /**
     * Constructor for InvincibilityPotion
     * @param posix_entityType is the string that is after the entityType "InvincibilityPotion_1"
     */

    public InvincibilityPotion(int id, Position curr_position) {
        super(id, "invincibility_potion", curr_position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        
    }

    @Override
    public void doUse(Entity playerEntity) {
        Player player = (Player) playerEntity;

        //turn on the invincible state
        player.setCurrBattleState(player.getInvincibleState());

        //update the inventory
        player.getInventory().removeItem((StaticEntity)this);
        
    }

    @Override
    public void doItemUse(Player player, Dungeon dungeon) {
        doUse((Entity) player);
    }
}
