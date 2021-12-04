package dungeonmania.DungeonControllerTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;

public class SaveAndLoadTest {
    /*** 
    * Testing save and load
    ***/

    @Test
    public void basicSaveTest1() {
        /*** 
        * Testing save file
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("advanced-2", "Peaceful");

        // Save game
        assertDoesNotThrow(() -> dungeonStart.saveGame(newDungeon.getDungeonName()));
    }

    @Test
    public void basicLoadTest1() {
        /*** 
        * Testing loading incorrect file
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Load game that does not exist
        assertThrows(IllegalArgumentException.class, () -> dungeonStart.loadGame("fakeGame"));
    }

    @Test
    public void basicLoadTest2() {
        /*** 
        * Testing loading saved file
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("advanced-2", "Peaceful");

        // Save game
        assertDoesNotThrow(() -> dungeonStart.saveGame(newDungeon.getDungeonName()));
        List<String> savedExist = dungeonStart.allGames();
        // Load save game
        assertDoesNotThrow(() -> dungeonStart.loadGame(savedExist.get(0)));
    }

    @Test
    public void basicLoadTest3() {
        /*** 
        * Testing loading persistent saved file
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("advanced-2", "Peaceful");

        // Save game
        assertDoesNotThrow(() -> dungeonStart.saveGame(newDungeon.getDungeonName()));

        // Close previous dungeon mania controller and open a new one
        DungeonManiaController newDungeonStart = new DungeonManiaController();
        List<String> savedExist = newDungeonStart.allGames();
        // Load save game
        assertDoesNotThrow(() -> newDungeonStart.loadGame(savedExist.get(0)));
        DungeonResponse secondDungeon = newDungeonStart.loadGame(savedExist.get(0));
        // May load some new variables
        assertEquals(newDungeon.getEntities().size(), secondDungeon.getEntities().size());
    }
} 
