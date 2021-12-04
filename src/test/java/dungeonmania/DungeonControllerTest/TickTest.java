package dungeonmania.DungeonControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TickTest {
    /*** 
    * Testing exceptions for tick
    ***/

    @Test
    public void basicTickTest1() {
        /*** 
        * Testing 2 actions; move and use item at once
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        dungeonStart.newGame("advanced-2", "Peaceful");

        // Can not perform 2 actions at once
        assertThrows(InvalidActionException.class, () -> dungeonStart.tick("1", Direction.UP));
    }

    @Test
    public void basicTickTest2() {
        /*** 
        * Does not have item
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        dungeonStart.newGame("advanced-2", "Peaceful");

        // The player does not have item
        assertThrows(InvalidActionException.class, () -> dungeonStart.tick("1", Direction.NONE));
    }

    @Test
    public void basicTickTest3() {
        /*** 
        * Cannot use this item
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon = dungeonStart.newGame("advanced-2", "Peaceful");
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        // The player cannot use this item
        assertThrows(IllegalArgumentException.class, () -> dungeonStart.tick("21", Direction.NONE));
    }
}
