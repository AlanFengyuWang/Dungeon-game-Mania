package dungeonmania.entity;

import dungeonmania.movingEntity.*;
import dungeonmania.staticEntity.*;
import dungeonmania.staticEntity.collectableEntity.*;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.*;
import dungeonmania.util.Position;

public class EntityCreator {

     /**
     * create for MovingEntity using factory pattern.
     * @author Alan
     * @date 29.10.2021
     * @param MovingEntity
     * @pre movingEntity != null GivenENtityType does not have ""
     * @return Entity 
     */
    public Entity createEntity(int entityID, String GivenEntityType, String x, String y, int movement_factor) {
        //convert the entity type temporarily so we know which entity type we are looking at
        //the format of the type is "sword_1", we need to get "sword" instead of "sword_"
        String entityType = GivenEntityType;
        // String[] parts = null;

        //convert to int then get position
        Position pos = new Position(Integer.valueOf(x), Integer.valueOf(y));

        // System.out.println("EntityCreator/createEntity --> entityType = " + entityType);

        //check what type of entities we need to create using factory pattern
        if(entityType.contains("player")) {
            return (Entity) new Player(0, entityType, pos);
        }
        else if(entityType.contains("mercenary")) {
            return (Entity) new Mercenary(entityID, entityType, pos);
        }
        else if(entityType.contains("wall")) {
            return (Entity) new Wall(entityID, pos);
        }
        else if(entityType.contains("sword")) {
            return (Entity) new Sword(entityID, entityType, pos);
        }
        else if(entityType.contains("exit")) {
            return (Entity) new Exit(entityID, pos);
        }
        else if(entityType.contains("boulder")) {
            return (Entity) new Boulder(entityID, pos);
        }
        else if(entityType.contains("switch_door")) {
            return (Entity) new SwitchDoor(entityID, pos);
        }
        else if(entityType.contains("switch")) {
            return (Entity) new FloorSwitch(entityID, pos);
        }
        else if(entityType.contains("door_unlocked")) {
            return (Entity) new Door(entityID, "unlocked", pos);
        }
        else if(entityType.contains("door")) {
            return (Entity) new Door(entityID, pos);
        }
        else if(entityType.contains("portal")) {
            return (Entity) new Portal(entityID, pos);
        }
        else if(entityType.contains("zombie_toast_spawner")) {
            return (Entity) new ZombieToastSpawner(entityID, pos);
        }
        else if(entityType.contains("treasure")) {
            return (Entity) new Treasure(entityID, pos);
        }
        else if(entityType.contains("key")) {
            return (Entity) new Key(entityID, pos);
        }
        else if(entityType.contains("health_potion")) {
            return (Entity) new HealthPotion(entityID, pos);
        }
        else if(entityType.contains("invincibility_potion")) {
            System.out.println("EntityCreator --> invincibilityPotion = " + entityType);
            return (Entity) new InvincibilityPotion(entityID, pos);
        }
        else if(entityType.contains("invisibility_potion")) {
            return (Entity) new InvisibilityPotion(entityID, pos);
        }
        else if(entityType.contains("wood")) {
            return (Entity) new Wood(entityID, pos);
        }
        else if(entityType.contains("bomb")) {
            return (Entity) new Bomb(entityID, pos);
        }
        else if(entityType.contains("midnight_armour")) {
            return (Entity) new MidnightArmour(entityID, pos);
        }
        else if(entityType.contains("armour")) {
            return (Entity) new Armour(entityID, pos);
        }
        else if(entityType.contains("arrow")) {
            return (Entity) new Arrows(entityID, pos);
        }
        else if(entityType.contains("bow")) {
            return (Entity) new Bow(entityID, pos);
        }
        else if(entityType.contains("shield")) {
            return (Entity) new Shield(entityID, pos);
        }
        else if(entityType.contains("spider")) {
            return (Entity) new Spider(entityID, pos);
        }
        else if(entityType.contains("zombie_toast")) {
            return (Entity) new ZombieToast(entityID, pos);
        }
        else if(entityType.contains("hydra")) {
            return (Entity) new Hydra(entityID, pos);
        }
        else if(entityType.contains("assassin")) {
            return (Entity) new Assassin(entityID, pos);
        }
        else if(entityType.contains("one_ring")) {
            return (Entity) new TheOneRing(entityID, pos);
        }
        else if(entityType.contains("sceptre")) {
            return (Entity) new Sceptre(entityID, pos);
        }
        else if(entityType.contains("swamp_tile")) {
            return (Entity) new SwampTile(entityID, pos, movement_factor);
        } 
        else if(entityType.contains("sun_stone")) {
            return (Entity) new SunStone(entityID, pos);
        }
        else if(entityType.contains("anduril")) {
            return (Entity) new SunStone(entityID, pos);
        }
        else if (entityType.contains("wire")) {
            return (Entity) new Wire(entityID, pos);
        }
        else if (entityType.contains("light_bulb")) {
            return (Entity) new LightBulb(entityID, pos);
        }

        return null;
    }
}
