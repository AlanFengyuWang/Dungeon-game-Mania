package dungeonmania.BuildableEntityTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class ShieldTest {
    @Test
    public void basicShieldTest1() {
        /*** 
        * You can craft shield with 2 wood + 1 treasure
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("shield_test1", "Peaceful");

        // ========== Collecting Items ==========
        // Move player right x 3
        // Pick up 2 wood + 1 treasure
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 3);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("shield");
        assertTrue(newDungeon.getInventory().size() == 1);
    }

    @Test
    public void basicShieldTest2() {
        /*** 
        * You can craft shield with 2 wood + 1 key
        ***/
        
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("shield_test2", "Peaceful");

        // ========== Collecting Items ==========
        // Move player right x 3
        // Pick up 2 wood + 1 key
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 3);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("shield");
        assertTrue(newDungeon.getInventory().size() == 1);
    }

    @Test
    public void basicShieldTest3() {
        /*** 
        * You can craft shield with 2 wood + 1 key
        ***/
        
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("shield_test3", "Peaceful");

        // ========== Collecting Items ==========
        // Move player right x 3
        // Pick up 2 wood + 1 sun stone
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 3);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("shield");
        assertTrue(newDungeon.getInventory().size() == 1);
    }
}
