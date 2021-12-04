package dungeonmania.staticEntity.collectableEntity;


import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public class Armour extends CollectableEntity implements DurableEquipment{
    /**
     * @author William
     * @date 10/11/2021
    */
    private int durability = 60;
    private boolean isEquipped = false;
    Armour using = null;

    public Armour(int id, Position curr_position) {
        super(id, "armour", curr_position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        /**
         * @author William
         * @date 03/11/2021
         */
        if (!(entity instanceof Player)) return;

        Player mvEntity = (Player) entity;
        if (mvEntity.getInventory().getEquippedDefence("armour") != null)  {
            // Do nothing if another armour is being used
        } else {
            // mvEntity.setHealth(mvEntity.getHealth()+durability);
            mvEntity.setDefense(durability);
            mvEntity.getInventory().storeEquippedDefence(this);
        }
    }

    /**
     * Constructor for Armour
     * @param posix_entityType is the string that is after the entityType "Armour_1"
     */

    @Override
    public void doUse(Entity playerEntity) {
        
    }

    @Override
    public void updateDurability(int durability, int reduction) {
        this.durability = this.durability - reduction;
    }

    public int getDurability() {
        return this.durability;
    }

    public boolean getIsEquipped() {
        return this.isEquipped;
    }

    public void setIsEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

    @Override
    public void updatePlayerItemDurability(Player defender, Dungeon dungeon) {
        if (defender.getInventory().getEquippedDefence("armour") != null) {
            if (defender.getDefense() <= 0) {
                defender.getInventory().removeEquippedDefence(this);
                defender.getInventory().removeItem(this);   
            }
        }
    }

    @Override
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void checkDurability(Player player, Dungeon dungeon) {
        if (player.getInventory().getEquippedDefences().contains(this)) {
            // Do nothing if another armour is being used
        } else {
            doInteract(player, dungeon);
        }
    }
}
