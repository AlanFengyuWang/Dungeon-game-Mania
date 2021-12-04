package dungeonmania.LogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicNotTest {
    // Milestone 3 tests with Logic
    /*** 
    * Testing logic of NOT
    ***/

    @Test
    public void basicLogicNotTest1() {
        /*** 
        * 1 switch connected to Not Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test7", "Peaceful");
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Check if door opens with no activated switches
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Opened since no activated switches
        assertEquals(oldPos, newPos);
        // Push boulder on switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Door closes
        assertNotEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicNotTest2() {
        /*** 
        * 0 switch connected to Not Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test8", "Peaceful");
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Check if door opens with no activated switches
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Opened since no activated switches
        assertEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicNotTest3() {
        /*** 
        * Not switch connected to Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test9", "Peaceful");
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().contains("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Check if door opens with no activated switches
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Opened since no activated switches
        assertEquals(oldPos, newPos);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        assertNotEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicNotTest4() {
        /*** 
        * 3 switch connected to Not Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test10", "Peaceful");
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().contains("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Check if door opens with no activated switches
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Opened since no activated switches
        assertEquals(oldPos, newPos);
        // Push boulders to switches
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Move to door
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Door is locked
        assertNotEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicNotTest5() {
        /*** 
        * 3 switch connected to Not switch connected to Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test11", "Peaceful");
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().contains("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Check if door opens with no activated switches
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Opened since no activated switches
        assertEquals(oldPos, newPos);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Door is locked
        assertNotEquals(oldPos, newPos);
    }
}
