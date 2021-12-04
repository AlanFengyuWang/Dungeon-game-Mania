package dungeonmania.staticEntity.collectableEntity.buildableEntity;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.ZombieToast;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public class MidnightArmour extends BuildableEntity {

    public MidnightArmour(int id, Position position) {
        super(id, "midnight_armour", position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        if (!(entity instanceof Player)) return;
        // Provides extra attack damage as well as protection.
        if (!(entity instanceof Player)) {
            return;
        }
        MovingEntity mvEntity = (MovingEntity) entity;
        mvEntity.setAttackDamage(mvEntity.getAttackDamage() * 2);
        mvEntity.setDefense(150);
    }

    // Must have 1 armour + 1 sunstone
    @Override
    public boolean canCraft(Dungeon dungeon, List<StaticEntity> player_bag) {
        // Can only be crafted if there is no zombies in the dungeon
        if (dungeon.getEntities().stream().filter(e -> e instanceof ZombieToast).collect(Collectors.toList()).size() > 0) return false;

        if (
            player_bag.stream().filter(e -> e.getFilteredEntityType().equals("armour")).collect(Collectors.toList()).size() >= 1  &&
            player_bag.stream().filter(e -> e.getEntityType().contains("sun_stone")).collect(Collectors.toList()).size() >= 1
        ) {
            return true;
        }
        return false;
    }

    @Override
    public void usedMaterials(Inventory player_inventory) {
        player_inventory.removecraftedEntity("armour");
        player_inventory.removecraftedEntity("sun_stone");
    }
}

