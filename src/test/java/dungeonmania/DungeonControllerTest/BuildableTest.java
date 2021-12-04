package dungeonmania.DungeonControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;

public class BuildableTest {
    /*** 
    * Testing exceptions for buildable
    ***/

    @Test
    public void basicBuildableTest1() {
        /*** 
        * Insufficient items
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        dungeonStart.newGame("advanced-2", "Peaceful");

        // Not enough material
        assertThrows(InvalidActionException.class, () -> dungeonStart.build("bow"));
    }

    @Test
    public void basicBuildableTest2() {
        /*** 
        * Buildable does not exist
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        dungeonStart.newGame("advanced-2", "Peaceful");

        // Not enough material
        assertThrows(IllegalArgumentException.class, () -> dungeonStart.build("MachineGun"));
    }
}
