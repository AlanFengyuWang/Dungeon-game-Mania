package dungeonmania.LogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicAndTest {
    // Milestone 3 tests with Logic
    /*** 
    * Testing logic of AND
    ***/

    @Test
    public void basicLogicAndTest1() {
        /*** 
        * 1 switch connected to And Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test1", "Peaceful");

        // Check if door is locked
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Compare positions of door and player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();
        // Blocked since door does not open
        assertNotEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicAndTest1v2() {
        /*** 
        * 1 switch connected to And Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test1", "Peaceful");

        // Push boulder to trigger switch
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Try to go through door
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = player.getPosition();
        // Blocked since door does not open
        assertEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicAndTest2() {
        /*** 
        * 1 switch connected to AND switch connected to OR Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test2", "Peaceful");

        // Push boulder to trigger switch
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Try to go through door
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = player.getPosition();
        // Blocked since door does not open
        assertEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicAndTest3() {
        /*** 
        * 1 switch connected to AND switch connected to OR Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test3", "Peaceful");

        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Push boulder to trigger switch 1
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to trigger switch 2
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Try to go through door
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Door opens
        assertEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicAndTest3v2() {
        /*** 
        * Part 2 of basicLogicAndTest3
        * Push boulder off a switch and door closes
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test3", "Peaceful");

        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Push boulder to trigger switch 1
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to trigger switch 2
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Try to go through door
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Door opens
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Push boulder off switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Door closes
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Blocked
        assertNotEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicAndTest4() {
        /*** 
        * 2 switches connected to And Switch
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test4", "Peaceful");

        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Push boulder to trigger switch 1
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to trigger switch 2
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Try to go through door
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Door opens
        assertEquals(oldPos, newPos);
        // Push boulder off switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Door closes
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        // Blocked
        assertNotEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicAndTest5() {
        /*** 
        * 3 switches connected to And Door
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test5", "Peaceful");

        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Push boulder to trigger switch 1
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to trigger switch 2
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to trigger switch 3
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Go through door
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Door opens
        assertEquals(oldPos, newPos);
        // Push boulder off switch
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Door closes since all switches must be activated
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        assertNotEquals(oldPos, newPos);
    }

    @Test
    public void basicLogicAndTest6() {
        /*** 
        * 3 switches connected to And Switch
        ***/

        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test6", "Peaceful");

        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch_door")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();

        // Push boulder to trigger switch 1
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to trigger switch 2
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Push boulder to trigger switch 3
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Go through door
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        // Door opens
        assertEquals(oldPos, newPos);
        // Push boulder off switch
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Door closes since all switches must be activated
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        assertNotEquals(oldPos, newPos);
    }
}
