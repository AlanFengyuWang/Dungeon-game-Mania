package dungeonmania.StaticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class WallTest {
    /**
    * Wall blocks player movements
    **/
    @Test
    public void basicWallTest1() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("wall_test1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // ========== Wall blocks movement ==========
        Position start = new Position(1,1);
        // Move player up x 1
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Same position, moves into wall, stays in same position 
        assertEquals(start, player.getPosition());
        // Move player left x 1
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertEquals(start, player.getPosition());
        // Move player left x 1
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        assertEquals(start, player.getPosition());
        // Move player left x 1
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        assertEquals(start, player.getPosition());
    }

    /**
    * Wall blocks zombie toast movements
    **/
    @Test
    public void basicWallTest2() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("wall_test2", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        EntityResponse zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null); //here it return null if it does not find anything

        // ========== Wall blocks movement ==========
        Position start = zombie.getPosition();
        // Move player
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null);
        Position newPos = zombie.getPosition();
        // Zombie is stuck at same position
        assertEquals(start, newPos);
        // Move player
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null);
        newPos = zombie.getPosition();
        assertEquals(start, newPos);
    }

    /**
    * Wall blocks mercenary movements
    **/
    @Test
    public void basicWallTest3() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("wall_test3", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        EntityResponse mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything

        // ========== Wall blocks movement ==========
        Position start = mercenary.getPosition();
        // Move player
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null);
        Position newPos = mercenary.getPosition();
        // Zombie is stuck at same position
        assertEquals(start, newPos);
        // Move player
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null);
        newPos = mercenary.getPosition();
        assertEquals(start, newPos);
    }
}
