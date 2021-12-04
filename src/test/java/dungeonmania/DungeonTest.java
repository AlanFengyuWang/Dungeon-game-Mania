package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import dungeonmania.*;
import dungeonmania.entity.Entity;


public class DungeonTest {
    // public ArrayList<Entity> createEntityList() {
    //     // create entity list with 1 Player, 1 Exit and 6 Walls
    //     ArrayList<Entity> listEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Player(), new Exit(), new Wall(), new Wall(), new Wall(), new Wall()));

    //     return listEntities;
    // }

    // public Dungeon createDungeon() {
    //     Dungeon d = new Dungeon(100000, 15, 10, "standard", "exit", createEntityList());
    //     return d;
    // }

    // @Test
    // public void testCreateDungeon() {
    //     Dungeon d1 = createDungeon();
    //     assertEquals(d1.getId(), 100000); // not sure if necessary
    //     assertEquals(d1.getWidth(), 15);
    //     assertEquals(d1.getHeight(), 10);
    //     assertEquals(d1.getGameMode(), "standard");
    //     d1.getPlayer().setLocation(new Position(1, 1));
    //     assertEquals(d1.getPlayer().getLocation(), new Position(1, 1));
    //     assertTrue(d1.getGoal() instanceof GoalExit);

    //     Dungeon d2 = new Dungeon(100001, 60, 16, "peaceful", "treasure", createEntityList());
    //     assertEquals(d2.getId(), 100001);
    //     assertEquals(d2.getWidth(), 60);
    //     assertEquals(d2.getHeight(), 16);
    //     assertEquals(d2.getGameMode(), "peaceful");
    //     d2.getPlayer().setLocation(new Position(42, 9));
    //     assertEquals(d2.getPlayer().getLocation(), new Position(42, 9));
    //     assertTrue(d2.getGoal() instanceof GoalTreasure);

    //     Dungeon d3 = new Dungeon(147231, 400, 350, "hard", "{goal: OR, subgoals:" +
    //         "[{goal: enemies}, {goal: boulders}]}", createEntityList());
    //     assertEquals(d3.getId(), 147231);
    //     assertEquals(d3.getWidth(), 400);
    //     assertEquals(d3.getHeight(), 350);
    //     assertEquals(d3.getGameMode(), "hard");
    //     d3.getPlayer().setLocation(new Position(14, 9));
    //     assertEquals(d3.getPlayer().getLocation(), new Position(14, 9));
    //     assertTrue(d3.getGoal() instanceof CompositeGoalOr);
    //     assertTrue(d3.getGoal().getGoalLeft() instanceof GoalEnemies);
    //     assertTrue(d3.getGoal().getGoalRight() instanceof GoalBoulders);
    // }

    // @Test
    // public void testAddEntity() {
    //     Dungeon d = createDungeon();
    //     int numInitEntities = d.getListEntities().size();

    //     // TESTING NONCOLLECTABLE STATIC ENTITIES
    //     // create an ArrayList of all the noncollectable entities
    //     ArrayList<Entity> nonCollectableEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Wall(), new Exit(), new Boulder(), new Door(), new Portal(),
    //         new FloorSwitch(), new ZombieToastSpawner()));
    //     int numEntities = nonCollectableEntities.size() + numInitEntities;

    //     // add each of the entities using Dungeon.addEntity() then check its success
    //     for (Entity e : nonCollectableEntities) {
    //         d.addEntity(e);
    //         assertTrue(d.getListEntities().contains(e));
    //     }
    //     assertEquals(d.getListEntities().size(), numEntities);

    //     // TESTING COLLECTABLE/BUILDABLE STATIC ENTITIES
    //     // perform same test for all collectable entities
    //     ArrayList<Entity> collectableEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Treasure(), new Key(), new Wood(), new HealthPotion(), new InvisibilityPotion(),
    //         new InvincibilityPotion(), new Arrows(), new Bomb(), new Armour(), new Sword(), new Bow(),
    //         new Shield(), new TheOneRing()));
    //     numEntities += collectableEntities.size();

    //     for (Entity e : collectableEntities) {
    //         d.addEntity(e);
    //         assertTrue(d.getListEntities().contains(e));
    //     }
    //     assertEquals(d.getListEntities().size(), numEntities);

    //     // TESTING MOVING ENTITIES
    //     // perform same test for all moving entities
    //     ArrayList<Entity> movingEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Spider(), new ZombieToast(), new Mercenary()));
    //     numEntities += movingEntities.size();

    //     for (Entity e : movingEntities) {
    //         d.addEntity(e);
    //         assertTrue(d.getListEntities().contains(e));
    //     }
    //     assertEquals(d.getListEntities().size(), numEntities);
    // }
    
    // @Test
    // public void testRemoveEntity() {
    //     Dungeon d = createDungeon();
    //     int numInitEntities = d.getListEntities().size();

    //     // TESTING NONCOLLECTABLE STATIC ENTITIES
    //     // create an ArrayList of all the noncollectable entities
    //     ArrayList<Entity> nonCollectableEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Wall(), new Exit(), new Boulder(), new Door(), new Portal(),
    //         new FloorSwitch(), new ZombieToastSpawner()));
    //     int numEntities = nonCollectableEntities.size() + numInitEntities;

    //     // add each of the entities using Dungeon.addEntity()
    //     for (Entity e : nonCollectableEntities) {
    //         d.addEntity(e);
    //     }

    //     // remove each of the entities using Dungeon.removeEntity() then check its success
    //     for (Entity e : nonCollectableEntities) {
    //         d.removeEntity(e);
    //         numEntities--;
    //         assertFalse(d.getListEntities().contains(e));
    //         assertEquals(d.getListEntities().size(), numEntities);
    //     }
    //     assertEquals(d.getListEntities().size(), numInitEntities);

    //     // TESTING FOR COLLECTABLE/BUILDABLE STATIC ENTITIES
    //     // perform same test for all collectable entities
    //     ArrayList<Entity> collectableEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Treasure(), new Key(), new Wood(), new HealthPotion(), new InvisibilityPotion(),
    //         new InvincibilityPotion(), new Arrows(), new Bomb(), new Armour(), new Sword(), new Bow(),
    //         new Shield(), new TheOneRing()));
    //     numEntities += collectableEntities.size();

    //     for (Entity e : collectableEntities) {
    //         d.addEntity(e);
    //     }

    //     for (Entity e : collectableEntities) {
    //         d.removeEntity(e);
    //         numEntities--;
    //         assertFalse(d.getListEntities().contains(e));
    //         assertEquals(d.getListEntities().size(), numEntities);
    //     }
    //     assertEquals(d.getListEntities().size(), numInitEntities);

    //     // TESTING MOVING ENTITIES
    //     // perform same test for all moving entities
    //     ArrayList<Entity> movingEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Spider(), new ZombieToast(), new Mercenary()));
    //     numEntities += movingEntities.size();

    //     for (Entity e : movingEntities) {
    //         d.addEntity(e);
    //     }

    //     for (Entity e : movingEntities) {
    //         d.removeEntity(e);
    //         numEntities--;
    //         assertFalse(d.getListEntities().contains(e));
    //         assertEquals(d.getListEntities().size(), numEntities);
    //     }
    //     assertEquals(d.getListEntities().size(), numInitEntities);
    // }

    // @Test
    // public void testGetEntitiesByPosition() {
    //     Dungeon d = createDungeon();
        
    //     // create 2 entities at (6, 5)
    //     Position p1 = new Position(6, 5);
    //     ArrayList<Entity> listEntities1 = new ArrayList<Entity>(Arrays.asList(
    //         new Spider(), new Wall()));

    //     for (Entity e : listEntities1) {
    //         e.setLocation(p1);
    //         d.addEntity(e);
    //     }

    //     // create 4 entities at (12, 2)
    //     Position p2 = new Position(12, 2);
    //     ArrayList<Entity> listEntities2 = new ArrayList<Entity>(Arrays.asList(
    //         new Treasure(), new Key(), new HealthPotion(), new FloorSwitch()));

    //     for (Entity e : listEntities2) {
    //         e.setLocation(p2);
    //         d.addEntity(e);
    //     }

    //     // check for a position with no entities
    //     assertTrue(d.getEntitiesByPosition(new Position(4, 4)).isEmpty());

    //     // check for the starting position of the player at (1, 1) with no other entities
    //     assertEquals(d.getEntitiesByPosition(new Position(1, 1)).size(), 1);
    //     assertTrue(d.getEntitiesByPosition(new Position(1, 1)).contains(d.getPlayer()));

    //     // check for position (6, 5)
    //     assertEquals(d.getEntitiesByPosition(p1).size(), 2);
    //     for (Entity e : listEntities1) {
    //         assertTrue(d.getEntitiesByPosition(p1).contains(e));
    //     }

    //     // check for position (12, 2)
    //     assertEquals(d.getEntitiesByPosition(p2).size(), 4);
    //     for (Entity e : listEntities2) {
    //         assertTrue(d.getEntitiesByPosition(p2).contains(e));
    //     }
    // }

    // @Test
    // public void testGetEntitiesByClass() {
    //     Dungeon d = createDungeon();

    //     // creating a mix of entities of different subclasses in the dungeon
    //     ArrayList<Entity> staticEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Boulder(), new Key(), new HealthPotion(), new Arrows()));

    //     ArrayList<Entity> treasureEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Treasure(), new Treasure(), new Treasure(), new Treasure()));

    //     ArrayList<Entity> buildableEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Bow(), new Shield()));

    //     ArrayList<Entity> movingEntities = new ArrayList<Entity>(Arrays.asList(
    //         new Spider(), new ZombieToast(), new Mercenary()));

    //     ArrayList<Entity> allEntities = new ArrayList<Entity>();
    //     allEntities.addAll(staticEntities);
    //     allEntities.addAll(treasureEntities);
    //     allEntities.addAll(buildableEntities);
    //     allEntities.addAll(movingEntities);

    //     for (Entity e : allEntities) {
    //         d.addEntity(e);
    //     }

    //     // test the class Bomb for 0 entities
    //     ArrayList<Bomb> gotBomb = d.getEntitiesByClass(Bomb.class);
    //     assertTrue(gotBomb.isEmpty());

    //     // test the class StaticEntity for 10 entities
    //     ArrayList<StaticEntity> gotStaticEntities = d.getEntitiesByClass(StaticEntity.class);
    //     assertEquals(gotStaticEntities.size(), 10);
    //     staticEntities.addAll(treasureEntities);
    //     staticEntities.addAll(buildableEntities);
    //     for (StaticEntity se : staticEntities) {
    //         assertTrue(gotStaticEntities.contains(se));
    //     }

    //     // test the class Treasure for 4 entities
    //     ArrayList<Treasure> gotTreasure = d.getEntitiesByClass(Treasure.class);
    //     assertEquals(gotTreasure.size(), 4);
    //     for (Treasure t : treasureEntities) {
    //         assertTrue(gotTreasure.contains(t));
    //     }

    //     // test the class BuildableEntity for 2 entities
    //     ArrayList<BuildableEntity> gotBuildableEntities = d.getEntitiesByClass(BuildableEntity.class);
    //     assertEquals(gotBuildableEntities.size(), 2);
    //     for (BuildableEntity be : buildableEntities) {
    //         assertTrue(gotBuildableEntities.contains(be));
    //     }

    //     // test the class MovingEntity for 3 entities
    //     ArrayList<MovingEntity> gotMovingEntities = d.getEntitiesByClass(MovingEntity.class);
    //     assertEquals(gotMovingEntities.size(), 3);
    //     for (MovingEntity me : movingEntities) {
    //         assertTrue(gotMovingEntities.contains(me));
    //     }
    // }

    // @Test
    // public void testSetGoal() {
    //     // test initial goal of "exit"
    //     Dungeon d = createDungeon();
    //     assertTrue(d.getGoal() instanceof GoalExit);

    //     // test new goals have been set after changing them
    //     d.setGoal(new JSONObject("{goal: treasure}"));
    //     assertTrue(d.getGoal() instanceof GoalTreasure);

    //     d.setGoal(new JSONObject("{goal: boulders}"));
    //     assertTrue(d.getGoal() instanceof GoalBoulders);

    //     d.setGoal(new JSONObject("{goal: enemies}"));
    //     assertTrue(d.getGoal() instanceof GoalEnemies);

    //     d.setGoal(new JSONObject("{goal: AND," +
    //         "subgoals: [{goal: treasure}, {goal: enemies}]}"));
    //     assertTrue(d.getGoal() instanceof CompositeGoalAnd);
    //     assertTrue(d.getGoal().getGoalLeft() instanceof GoalTreasure);
    //     assertTrue(d.getGoal().getGoalRight() instanceof GoalEnemies);

    //     d.setGoal(new JSONObject("{goal: OR," +
    //         "subgoals: [{goal: exit}, {goal: boulders}]}"));
    //     assertTrue(d.getGoal() instanceof CompositeGoalOr);
    //     assertTrue(d.getGoal().getGoalLeft() instanceof GoalExit);
    //     assertTrue(d.getGoal().getGoalRight() instanceof GoalBoulders);
    // }

    // // // simple goals
    // // JSONObject gTreasure = new JSONObject("{goal: treasure}");
    // // JSONObject gBoulders = new JSONObject("{goal: boulders}");
    // // JSONObject gEnemies = new JSONObject("{goal: enemies}");

    // // // complex goals
    // // JSONObject gAnd = new JSONObject("{goal: AND," +
    // //     "subgoals: [{goal: treasure}, {goal: enemies}]}");
    // // JSONObject gOr = new JSONObject("{goal: OR," +
    // //     "subgoals: [{goal: exit}, {goal: boulders}]}");

    // // "goal-condition": {
    // //     "goal": "AND",
    // //     "subgoals": [
    // //       {
    // //         "goal": "enemies"
    // //       },
    // //       {
    // //         "goal": "treasure"
    // //       }
    // //     ]
    // // }

    // // basic check for simple and complex goals
    // // goal evaluation is tested more thoroughly in GoalsTest
    // @Test
    // public void testCheckGoal() {
    //     // simple goal check
    //     Dungeon d = createDungeon();
    //     assertFalse(d.checkGoal());

    //     d.getPlayer().setLocation(new Position(14, 9));
    //     assertTrue(d.checkGoal());

    //     // complex goal check
    //     Boulder b = new Boulder();
    //     b.setLocation(new Position(3, 3));
    //     FloorSwitch b = new FloorSwitch();
    //     b.setLocation(new Position(3, 2));

    //     d.addEntity(b);
    //     d.addEntity(fs);

    //     d.setGoal(new JSONObject("{goal: AND," +
    //         "subgoals: [{goal: exit}, {goal: boulders}]}"));
    //     assertFalse(d.checkGoal());

    //     b.setLocation(new Position(3, 2));
    //     assertTrue(d.checkGoal());
    // }    
}
