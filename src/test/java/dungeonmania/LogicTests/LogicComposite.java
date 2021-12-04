package dungeonmania.LogicTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicComposite {
    // Milestone 3 tests with Logic
    /*** 
    * Testing logic with multiple logics
    ***/

    @Test
    public void basicLogicCompositeTest1() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("logic_test20", "Peaceful");

        // Check if door is locked
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Compare positions of door and player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = player.getPosition();
        EntityResponse door = newDungeon.getEntities().stream().filter(e -> e.getId().equals("5")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = door.getPosition();
        // Blocked since door does not open
        assertNotEquals(oldPos, newPos);
        // Push boulder to switch
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Blocked since door does not open
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        assertNotEquals(oldPos, newPos);
        // Check other door
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        EntityResponse door2 = newDungeon.getEntities().stream().filter(e -> e.getId().equals("7")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos2 = door2.getPosition();
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        oldPos = player.getPosition();
        assertEquals(oldPos, newPos2);
    }
}
