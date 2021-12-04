package dungeonmania.BuildableEntityTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class SceptreTest {
    // Milestone 3 tests with buildable entities
    /*** 
    * Must have 1 wood + (((1 treasure OR 1 key OR 2 arrows) + 1 sunstone) OR 2 sunstone)
    * To craft sceptre
    ***/

    @Test
    public void basicSceptreTest1() {
        /*** 
        * Must have 1 wood + 2 arrows + 1 sunstone to craft sceptre
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("sceptre_test1", "Standard");

        // ========== Collecting Items ==========
        // Move player right x 4
        // Pick up 1 wood + 2 arrows + 1 sunstone
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 4);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("sceptre");
        assertTrue(newDungeon.getInventory().size() == 1);

        // ========== Mind Control ==========
        EntityResponse mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        // MercenaryId
        newDungeon = dungeonStart.interact(mercenary.getId());

        // Mind control lasts for 10 ticks
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Battle
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null);
        assertNotNull(player);
        assertNull(mercenary);
    }

    @Test
    public void basicSceptreTest2() {
        /*** 
        * Pick up 1 wood + 1 treasure + 1 sunstone
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("sceptre_test2", "Standard");

        // ========== Collecting Items ==========
        // Move player right x 3
        // Pick up 1 wood + 1 treasure + 1 sunstone
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 3);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("sceptre");
        assertTrue(newDungeon.getInventory().size() == 1);
    }

    @Test
    public void basicSceptreTest3() {
        /*** 
        * Pick up 1 wood + 1 key + 1 sunstone
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("sceptre_test3", "Standard");

        // ========== Collecting Items ==========
        // Move player right x 3
        // Pick up 1 wood + 1 key + 1 sunstone
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 3);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("sceptre");
        assertTrue(newDungeon.getInventory().size() == 1);
    }

    @Test
    public void basicSceptreTest4() {
        /*** 
        * Pick up 1 wood + 2 sunstone
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("sceptre_test4", "Standard");

        // ========== Collecting Items ==========
        // Move player right x 3
        // Pick up 1 wood + 2 sunstone
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 3);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("sceptre");
        assertTrue(newDungeon.getInventory().size() == 1);
    }
}
