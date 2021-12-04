package dungeonmania.staticEntity.collectableEntity.buildableEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.collectableEntity.Arrows;
import dungeonmania.util.Position;

public class Bow extends BuildableEntity implements DurableEquipment{
    private int durability = 2;
    private boolean isEquipped = false;
    Bow using = null;

    public Bow(int id, Position position) {
        super(id, "bow", position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        /**
         * @author William
         * @date 03/11/2021
         */
        if (!(entity instanceof Player)) return;

        Player mvEntity = (Player) entity;
        int arrows_in_inventory = mvEntity.getInventory().getStaticEntities().stream().filter(se -> se instanceof Arrows).collect(Collectors.toList()).size();
        int bows_in_inventory = mvEntity.getInventory().getStaticEntities().stream().filter(se -> se instanceof Bow).collect(Collectors.toList()).size();
        Optional<StaticEntity> arrow = mvEntity.getInventory().getStaticEntities().stream().filter(se -> se instanceof Arrows).findAny();
        if (mvEntity.getInventory().getEquippedWeapon("bow") != null)  {
            if (mvEntity.getInventory().getStaticEntity("arrow") != null) {
                if (mvEntity.getAttackDamage() != 50) {
                    attackWithArrow(arrows_in_inventory, mvEntity, arrow.get());
                }
            } 
        } else if (mvEntity.getInventory().getEquippedWeapon("sword") != null) {
            //do nothing
        } else {
            if (bows_in_inventory == 1) {
                if (mvEntity.getInventory().getStaticEntity("arrow") != null) {
                    attackWithArrow(arrows_in_inventory, mvEntity, arrow.get());
                    mvEntity.getInventory().storeEquippedWeapon(this);
                } 
            } else {
                mvEntity.getInventory().storeEquippedWeapon(this);
            }
        }
    }

    public void attackWithArrow(int arrowCount, Player entity, StaticEntity sEntity) {
        if (arrowCount > 0) {
            Arrows arrow = (Arrows) sEntity;
            entity.setAttackDamage(entity.getAttackDamage() + arrow.getDamage());
        }
    }

    // Must have 1 wood + 3 arrows
    @Override
    public boolean canCraft(Dungeon dungeon, List<StaticEntity> player_bag) {
        // Check if there is 1 wood + 3 arrows in inventory
        if (
            player_bag.stream().filter(e -> e.getEntityType().contains("wood")).collect(Collectors.toList()).size() >= 1 &&
            player_bag.stream().filter(e -> e.getEntityType().contains("arrow")).collect(Collectors.toList()).size() >= 3
        ) {
            return true;
        }
        return false;
    }

    @Override
    public void usedMaterials(Inventory player_inventory) {
        player_inventory.removecraftedEntity("wood");
        player_inventory.removecraftedEntity("arrow");
        player_inventory.removecraftedEntity("arrow");
        player_inventory.removecraftedEntity("arrow");
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
        if (defender.getInventory().getEquippedWeapons().contains(this)) {
            if (this.getDurability() <= 0) {
                defender.setAttackDamage(defender.getDefaultDamage());
                defender.getInventory().removeItem(this);
                defender.getInventory().removeEquippedWeapon(this);
            } else {
                if (defender.getInventory().getStaticEntity("arrow") != null) {
                    this.updateDurability(this.getDurability(), 1);
                    defender.getInventory().removeItem(defender.getInventory().getStaticEntity("arrow"));
                }
            }
        }
    }
    
    @Override
    public void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public void checkDurability(Player player, Dungeon dungeon) {
        if (this.getIsEquipped()) {
            int arrows_in_inventory = player.getInventory().getStaticEntities().stream().filter(a -> a instanceof Arrows).collect(Collectors.toList()).size();
            if (arrows_in_inventory <= 0) {
                player.setAttackDamage(30);
            } 
        } else {
            doInteract(player, dungeon);
        }     
    }
}

