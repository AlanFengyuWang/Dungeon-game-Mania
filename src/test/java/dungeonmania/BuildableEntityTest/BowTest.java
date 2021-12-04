package dungeonmania.BuildableEntityTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class BowTest {
    // David's test with buildable entities

    @Test
    public void basicBowTest() {
        /*** 
        * You can craft shield with 1 wood + 3 arrows
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("bow_test", "Peaceful");

        // ========== Collecting Items ==========
        // Move player right x 4
        // Pick up 1 wood + 3 arrows
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 4);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // ========== Crafting ==========
        newDungeon = dungeonStart.build("bow");
        assertTrue(newDungeon.getInventory().size() == 1);
    }
}
