package dungeonmania.staticEntity.collectableEntity;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements DurableEquipment {
    /**
     * @author Alan
     * @date 31.10.2021
     */
    private int damage_enhance_multiply = 2;
    private int durability = 2;
    private boolean isEquipped = false;
    Sword using = null;
    /**
     * Constructor for Sword
     * @param posix_entityType is the string that is after the entityType "Sword_1"
     */
    public Sword(int id, Position curr_position) {
        super(id, "sword", curr_position);
    }

    public Sword(int id, String entityType, Position curr_position) {
        super(id, entityType, curr_position);
    }
    /**
     * Here it's doing the job of increasing the stats of the player by double
     */
    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
       /**
         * @author William
         * @date 10/11/2021
        */
        if (!(entity instanceof Player)) return;
        Player mvEntity = (Player) entity;
        int arrows_in_inventory = mvEntity.getInventory().getStaticEntities().stream().filter(se -> se instanceof Arrows).collect(Collectors.toList()).size();
        if (mvEntity.getInventory().getEquippedWeapon("sword") != null || (arrows_in_inventory > 0 && mvEntity.getInventory().getEquippedWeapon("bow") != null))  {
            // Do nothing if another sword is being used
        } else {
            mvEntity.setAttackDamage(mvEntity.getAttackDamage() * damage_enhance_multiply);
            mvEntity.getInventory().storeEquippedWeapon(this);
        }
    }

    @Override
    public void doUse(Entity playerEntity) {
        return;
        
    }

    public boolean getIsEquipped() {
        return this.isEquipped;
    }

    public void setIsEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

    public int getDurability() {
        return this.durability;
    }

    @Override
    public void updateDurability(int durability, int reduction) {
        this.durability = this.durability - reduction;
        
    }

    @Override
    public void updatePlayerItemDurability(Player defender, Dungeon dungeon) {
        System.out.println("sword dur -> " + this.getDurability());
        if (defender.getInventory().getEquippedWeapons().contains(this)) {
            this.updateDurability(this.getDurability(), 1);
            if (this.getDurability() <= 0) {
                defender.setAttackDamage(defender.getDefaultDamage());
                defender.getInventory().removeItem(this);
                defender.getInventory().removeEquippedWeapon(this);
            }
        }
    }

    @Override
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void checkDurability(Player player, Dungeon dungeon) {
        if (player.getInventory().getEquippedWeapons().contains(this)) {
            // Do nothing if another sword is being used
        } else {
            doInteract(player, dungeon);
        }
    }
}
