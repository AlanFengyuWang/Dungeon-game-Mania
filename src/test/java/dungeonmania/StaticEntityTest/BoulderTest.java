package dungeonmania.StaticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BoulderTest {
    /**
    * Boulder can be pushed on empty tile
    * Boulders cannot be pushed onto walls/boulders
    **/
    @Test
    public void basicBoulderTest1() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("boulder_test1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        Position player_pos = new Position(1,1);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Push boulder to the right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // push again but push to wall so player and boulder do not move
        assertEquals(player_pos, player.getPosition());

        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player_pos = new Position(1, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player_pos = new Position(1, 2);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // push again but push to wall so player and boulder do not move
        assertEquals(player_pos, player.getPosition());
    }

    /**
    * Boulder can be pushed on switches/exit
    **/
    @Test
    public void basicBoulderTest2() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("boulder_test2", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        EntityResponse boulder = newDungeon.getEntities().stream().filter(e -> e.getType().equals("boulder")).findFirst().orElse(null); //here it return null if it does not find anything
        EntityResponse floorSwitch = newDungeon.getEntities().stream().filter(e -> e.getType().equals("switch")).findFirst().orElse(null); //here it return null if it does not find anything
        EntityResponse exit = newDungeon.getEntities().stream().filter(e -> e.getType().equals("exit")).findFirst().orElse(null); //here it return null if it does not find anything
        Position switchPos = floorSwitch.getPosition();
        Position exitPos = exit.getPosition();

        // Push boulder to the right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // On switch
        boulder = newDungeon.getEntities().stream().filter(e -> e.getType().equals("boulder")).findFirst().orElse(null);
        Position boulderPos = boulder.getPosition();
        assertEquals(switchPos, boulderPos);
        // Push boulder to the right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // On exit
        boulder = newDungeon.getEntities().stream().filter(e -> e.getType().equals("boulder")).findFirst().orElse(null);
        boulderPos = boulder.getPosition();
        assertEquals(exitPos, boulderPos);
    }

    /**
    * Boulder cannot be pushed onto collectable entities or moving
    **/
    @Test
    public void basicBoulderTest3() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("boulder_test4", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        EntityResponse boulder = newDungeon.getEntities().stream().filter(e -> e.getType().equals("boulder")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = boulder.getPosition();

        // Push boulder to the right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        Position newPos = boulder.getPosition();
        assertEquals(oldPos, newPos);
        // Collectable entity blocks it
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Moving entity blocks it
        newPos = boulder.getPosition();
        assertEquals(oldPos, newPos);
    }
}
