package dungeonmania.staticEntity.collectableEntity.buildableEntity;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public class Sceptre extends BuildableEntity {

    public Sceptre(int id, Position position) {
        super(id, "sceptre", position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        return;
    }

    // Must have 1 wood + (((1 treasure OR 1 used key OR 2 arrows) + 1 sunstone) OR 2 sunstone)
    @Override
    public boolean canCraft(Dungeon dungeon, List<StaticEntity> player_bag) {
        if (
            player_bag.stream().filter(e -> e.getEntityType().contains("wood")).collect(Collectors.toList()).size() >= 1 &&
            (((player_bag.stream().filter(e -> e.getEntityType().contains("treasure")).collect(Collectors.toList()).size() >= 1 ||
            player_bag.stream().filter(e -> e.getEntityType().contains("key")).collect(Collectors.toList()).size() >= 1 ||
            player_bag.stream().filter(e -> e.getEntityType().contains("arrow")).collect(Collectors.toList()).size() >= 2) &&
            player_bag.stream().filter(e -> e.getEntityType().contains("sun_stone")).collect(Collectors.toList()).size() >= 1) ||
            player_bag.stream().filter(e -> e.getEntityType().contains("sun_stone")).collect(Collectors.toList()).size() >= 2)
        ) {
            return true;
        }
        return false;
    }

    @Override
    public void usedMaterials(Inventory player_inventory) {
        player_inventory.removecraftedEntity("wood");
        player_inventory.removecraftedEntity("sun_stone");

        if (player_inventory.getStaticEntities().stream().filter(e -> e.getEntityType().contains("treasure")).collect(Collectors.toList()).size() >= 1) {
            player_inventory.removecraftedEntity("treasure");
        }
        else if (player_inventory.getStaticEntities().stream().filter(e -> e.getEntityType().contains("key")).collect(Collectors.toList()).size() >= 1) {
            player_inventory.removecraftedEntity("key");
        }
        else if (player_inventory.getStaticEntities().stream().filter(e -> e.getEntityType().contains("arrow")).collect(Collectors.toList()).size() >= 2) {
            player_inventory.removecraftedEntity("arrow");
            player_inventory.removecraftedEntity("arrow");
        }
        else {
            player_inventory.removecraftedEntity("sun_stone");
        }
    }
}
