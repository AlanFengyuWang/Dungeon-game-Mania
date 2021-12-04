package dungeonmania.staticEntity.collectableEntity.buildableEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.EntityCreator;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public abstract class BuildableEntity extends StaticEntity{

    public BuildableEntity(int id, String entityType, Position position) {
        super(id, entityType, position);
    }

    public static List<String> allBuildableEntities() {
        List<String> buildableList = new ArrayList<>();
        buildableList.add("shield");
        buildableList.add("bow");
        buildableList.add("sceptre");
        buildableList.add("midnight_armour");
        return buildableList;
    }

    // Check if items are buildable when there is enough ingredients
    public static void createable(Dungeon dungeon, Inventory player_inventory) {
        List<StaticEntity> player_bag = player_inventory.getStaticEntities();
        EntityCreator entityCreator = new EntityCreator();

        for (String buildable: allBuildableEntities()) {
            Entity buildableItem = entityCreator.createEntity(dungeon.uniqueEntityID(), buildable, "-2", "-2", 0);
            // add buildable items when bag has enough ingredients
            if (((BuildableEntity) buildableItem).canCraft(dungeon, player_bag)) {
                player_inventory.addBuildableItem((BuildableEntity) buildableItem);
            }
            else unbuildable (player_inventory, buildable);
        }
    }

    // The materials were used up
    public static void unbuildable (Inventory player_inventory, String buildItem) {
        List<BuildableEntity> buildableList = player_inventory.getBuildableEntities();
        BuildableEntity buildable = buildableList.stream().filter(e -> e.getEntityType().contains(buildItem)).findAny().orElse(null);
        player_inventory.removeBuildableEntity(buildable);
    }

    // Precondition: Buildable is a valid and correct input
    public static void craft(Dungeon dungeon, Player player_entity, String buildable) {
        Inventory player_inventory = player_entity.getInventory();
        Entity entity = player_inventory.getBuildableEntities().stream().filter(e -> e.getEntityType().equals(buildable)).findAny().orElse(null);
        ((BuildableEntity) entity).usedMaterials(player_inventory);

        //after crafting, store to the inventory
        player_inventory.storeToInventory((StaticEntity) entity);
        player_inventory.addBuildableItem((BuildableEntity) entity);
        player_inventory.removeBuildableEntity((BuildableEntity) entity);

        // Add stats
        ((BuildableEntity) entity).doInteract(player_entity, dungeon);
    }

    public abstract boolean canCraft(Dungeon dungeon, List<StaticEntity> player_bag);
    public abstract void usedMaterials(Inventory player_inventory);
}
