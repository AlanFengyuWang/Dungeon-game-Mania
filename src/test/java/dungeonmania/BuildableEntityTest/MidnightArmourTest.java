package dungeonmania.BuildableEntityTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class MidnightArmourTest {
    /*** 
    * Must have 1 armour + 1 sunstone
    * To craft midnight armour
    ***/

    @Test
    public void basicMidnightArmourTest1() {
        /*** 
        * Must have 1 armour + 1 sunstone
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("midnight_armour_test1", "Standard");

        // ========== Collecting Items ==========
        // Move player right x 1
        // Pick up 1 armour + 1 sun stone
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 2);
        // Must have no zombies to build midnight armour
        assertTrue(newDungeon.getBuildables().size() == 0);

        // Kill the zombie
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("midnight_armour");
        assertTrue(newDungeon.getInventory().size() == 1);
    }

    @Test
    public void basicMidnightArmourTest2() {
        /*** 
        * When a zombie spawns, cannot craft midnight armour
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("midnight_armour_test2", "Peaceful");

        // ========== Collecting Items ==========
        // Move player right x 1
        // Pick up 1 armour + 1 sun stone
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 2);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // Wait until zombie spawn
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        assertTrue(newDungeon.getBuildables().size() == 0);

    }   
}
