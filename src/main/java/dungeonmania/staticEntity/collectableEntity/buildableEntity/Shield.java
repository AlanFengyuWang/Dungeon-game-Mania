package dungeonmania.staticEntity.collectableEntity.buildableEntity;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public class Shield extends BuildableEntity implements DurableEquipment {
    private int durability = 40;
    private boolean isEquipped = false;
    Shield using = null;

    public Shield(int id, Position position) {
        super(id, "shield", position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        /**
         * @author William
         * @date 03/11/2021
         */
        if (!(entity instanceof Player)) return;
        
        Player mvEntity = (Player) entity;
        if (mvEntity.getInventory().getEquippedDefence("shield") != null)  {
            // Do nothing if another shield is being used
        } else {
            mvEntity.setDefense(durability);
            mvEntity.getInventory().storeEquippedDefence(this);
        }
    }

    // Must have 2 wood + (1 treasure OR 1 used key)
    @Override
    public boolean canCraft(Dungeon dungeon, List<StaticEntity> player_bag) {
        if (
            player_bag.stream().filter(e -> e.getEntityType().contains("wood")).collect(Collectors.toList()).size() >= 2 &&
            (player_bag.stream().filter(e -> e.getEntityType().contains("sun_stone")).collect(Collectors.toList()).size() >= 1 ||
                player_bag.stream().filter(e -> e.getEntityType().contains("treasure")).collect(Collectors.toList()).size() >= 1 ||
            player_bag.stream().filter(e -> e.getEntityType().contains("key")).collect(Collectors.toList()).size() >= 1 )
        ) {
            return true;
        }
        return false;
    }

    @Override
    public void usedMaterials(Inventory player_inventory) {
        player_inventory.removecraftedEntity("wood");
        player_inventory.removecraftedEntity("wood");
        // Craft least valuable material first: Treasure, Key, Sun Stone
        if (player_inventory.getStaticEntities().stream().filter(e -> e.getEntityType().contains("treasure")).collect(Collectors.toList()).size() >= 1) {
            player_inventory.removecraftedEntity("treasure");
        }
        else if (player_inventory.getStaticEntities().stream().filter(e -> e.getEntityType().contains("key")).collect(Collectors.toList()).size() >= 1) {
            player_inventory.removecraftedEntity("key");
        } 
        else {
            player_inventory.removecraftedEntity("sun_stone");
        }
    }

    @Override
    public void updatePlayerItemDurability(Player defender, Dungeon dungeon) {
        if (defender.getInventory().getEquippedDefences().contains(this)) {
            if (defender.getDefense() <= 0) {
                defender.getInventory().removeEquippedDefence(this);
                defender.getInventory().removeItem(this);   
            }
        }
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
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void checkDurability(Player player, Dungeon dungeon) {
        if (this.getIsEquipped()) {

        } else {
            doInteract(player, dungeon);
        }
    }
}