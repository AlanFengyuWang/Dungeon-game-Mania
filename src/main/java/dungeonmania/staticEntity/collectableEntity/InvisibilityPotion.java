package dungeonmania.staticEntity.collectableEntity;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.TickItemUse;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity implements TickItemUse{
    /**
     * Constructor for InvisibilityPotion
     * @param posix_entityType is the string that is after the entityType "InvisibilityPotion_1"
     */


    public InvisibilityPotion(int id, Position curr_position) {
        super(id, "invisibility_potion", curr_position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        
    }

    @Override
    public void doUse(Entity playerEntity) {
        Player player = (Player) playerEntity;
        //turn on the invisible state
        player.setCurrBattleState(player.getInvisibleState());

        //update the inventory
        player.getInventory().removeItem((StaticEntity)this);
    }

    @Override
    public void doItemUse(Player player, Dungeon dungeon) {
        doUse(player);
    }
}
