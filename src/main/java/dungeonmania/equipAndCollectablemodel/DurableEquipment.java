package dungeonmania.equipAndCollectablemodel;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.Player;

/**
 * Durable Equipments:
 * Sword, Bow, Shield and Arrow
 */
public interface DurableEquipment {
    /**
     * @author William
     * @date 03/11/2021
     */
    public void updateDurability(int durability, int reduction); 
    public void updatePlayerItemDurability(Player defender, Dungeon dungeon);
    public boolean getIsEquipped();
    public void setIsEquipped(boolean isEquipped);
    public int getDurability();
    public void setDurability(int durability);
    public void checkDurability(Player player, Dungeon dungeon);
}
