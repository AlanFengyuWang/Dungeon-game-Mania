package dungeonmania.LogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicXorTest {
    // Milestone 3 tests with Logic
    /*** 
    * Testing logic of XOR
    ***/

    @Test
    public void basicLogicXorTest1() {
        /*** 
        * 1 switch connected to Xor Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test16", "Peaceful");

        // Check if door is locked
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();
        // Locked since no activated switches
        assertNotEquals(oldPos, newPos);
        // Push boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Check if door is opened
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Opened since there is an activated switches
        assertEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicXorTest2() {
        /*** 
        * XOR switch connected to Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test17", "Peaceful");

        // Check if door is locked
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();
        // Locked since no activated switches
        assertNotEquals(oldPos, newPos);
        // Push boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Check if door is opened
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Opened since there is an activated switches
        assertEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicXorTest3() {
        /*** 
        * 3 switch connected to OR Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test18", "Peaceful");

        // Check if door is locked
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();
        // Locked since no activated switches
        assertNotEquals(oldPos, newPos);
        // Push boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Check if door is opened
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Opened since there is an activated switch
        assertEquals(oldPos, newPos);
        // Push another boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Check if door is opened
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Closed since there is more than 1 activated switches
        assertNotEquals(oldPos, newPos);
        // Close previous switch
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Check if door is opened
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Opened since there is at only 1 activated switch
        assertEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicXorTest4() {
        /*** 
        * OR switch connected to Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test19", "Peaceful");

        // Check if door is locked
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();
        // Locked since no activated switches
        assertNotEquals(oldPos, newPos);
        // Push boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Check if door is opened
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Opened since there is an activated switch
        assertEquals(oldPos, newPos);
        // Push another boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Check if door is opened
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Opened since there is an activated switches
        assertNotEquals(oldPos, newPos);
        // Close previous switch
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Check if door is opened
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Opened since there is at only 1 activated switch
        assertEquals(oldPos, newPos);
    }
}