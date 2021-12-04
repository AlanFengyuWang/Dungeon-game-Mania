package dungeonmania.StaticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DoorTest {
    /**
    * Door does not open without key
    **/
    @Test
    public void basicDoorTest() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("door_test1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        Position player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // No key so door blocks movement
        assertEquals(player_pos, player.getPosition());

        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player_pos = new Position(1, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(3, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Door opens from correct key
        assertEquals(player_pos, player.getPosition());

        // Door does not block player anymore
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(4, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player_pos = new Position(3, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
    }

    /**
    * Key unlocks door
    **/
    @Test
    public void basicDoorTest1() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("door_test2", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        Position player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(3, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Unlock door
        assertEquals(player_pos, player.getPosition());
    }

    /**
    * Incorrect Key cannot unlock door
    * Can only pick 1 key up
    **/
    @Test
    public void basicDoorTest2() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("door_test3", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        Position player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(4, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Wrong key
        assertNotEquals(player_pos, player.getPosition());
    }

    /**
    * Mercenary and zombie toast cannot move pass locked doors
    **/
    @Test
    public void basicDoorTest3() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("door_test4", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");
        EntityResponse mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        EntityResponse zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null); //here it return null if it does not find anything
        Position mercenaryPos1 = mercenary.getPosition();
        Position zombiePos1 = zombie.getPosition();

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Zombie and mercenary are blocked by door
        mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null); //here it return null if it does not find anything
        Position mercenaryPos2 = mercenary.getPosition();
        Position zombiePos2 = zombie.getPosition();
        assertEquals(mercenaryPos1, mercenaryPos2);
        assertEquals(zombiePos1, zombiePos2);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Zombie and mercenary are blocked by door
        mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null); //here it return null if it does not find anything
        Position mercenaryPos3 = mercenary.getPosition();
        Position zombiePos3 = zombie.getPosition();
        assertEquals(mercenaryPos1, mercenaryPos3);
        assertEquals(zombiePos1, zombiePos3);
    }

    /**
    * Mercenary and zombie toast can move pass unlocked doors
    **/
    @Test
    public void basicDoorTest4() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("door_test5", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");
        EntityResponse mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        EntityResponse zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null); //here it return null if it does not find anything
        Position mercenaryPos1 = mercenary.getPosition();
        Position zombiePos1 = zombie.getPosition();

        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Zombie and mercenary are blocked by door
        mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null); //here it return null if it does not find anything
        Position mercenaryPos2 = mercenary.getPosition();
        Position zombiePos2 = zombie.getPosition();
        assertNotEquals(mercenaryPos1, mercenaryPos2);
        assertNotEquals(zombiePos1, zombiePos2);
    }

    /**
    * Sun stone unlocks door
    **/
    @Test
    public void basicDoorTest5() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("door_test6", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        Position player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(3, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Unlock door
        assertEquals(player_pos, player.getPosition());
    }
    
    @Test
    public void advancedDoorTest() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("advanced-2", "Peaceful");

        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // Move player right x 10
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        // Move player down x 9
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);

        // Move player left x 4
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);

        // Move player up x 1
        newDungeon = dungeonStart.tick(null, Direction.UP);

        // Move player left x 3
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);

        Position player_pos = new Position(4, 9);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Move player down x 1
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Door does not open from wrong key
        // Same position
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        

        // Move player right x 3
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        // Move player down x 1
        newDungeon = dungeonStart.tick(null, Direction.DOWN);

        // Move player right x 8
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        player_pos = new Position(15, 10);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Move player up x 1
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Door opens from correct key
        player_pos = new Position(15, 9);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
    }
}
