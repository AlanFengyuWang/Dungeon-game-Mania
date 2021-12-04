package dungeonmania.DungeonControllerTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;

public class NewGameTest {
    /*** 
    * Testing exceptions for new game
    ***/

    @Test
    public void basicNewGameTest1() {
        /*** 
        * Not a valid game mode
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dungeonStart.newGame("advanced-2", "Hardcore"));
    }

    @Test
    public void basicNewGameTest2() {
        /*** 
        * Dungeon does not exist
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dungeonStart.newGame("cyberpunk 2077", "Peaceful"));
    }
}
