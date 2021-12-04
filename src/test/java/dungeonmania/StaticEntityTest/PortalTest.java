package dungeonmania.StaticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PortalTest {
    @Test
    public void basicPortalTest() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("portals copy", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        Position player_pos = new Position(0, 0);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Teleport right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(5, 0);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());

        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);

        player_pos = new Position(4, -1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Teleport down
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player_pos = new Position(1, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);

        player_pos = new Position(2, 0);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Teleport left
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player_pos = new Position(3, 0);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());

        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        player_pos = new Position(4, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Teleport up
        newDungeon = dungeonStart.tick(null, Direction.UP);
        player_pos = new Position(1, -1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
    }  

    @Test
    public void advancedPortalTest1() {
        /*
        * Boulder obstruction when teleporting
        */
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("portal_test1", "Peaceful");

        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // If path blocked, portal does not teleport
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Position of first portal
        Position player_pos = new Position(2, 2);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Position of first portal
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Position of first portal
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player_pos = new Position(5, 2);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Pushed boulder down
        assertEquals(player_pos, player.getPosition());
    }

    @Test
    public void advancedPortalTest2() {
        /*
        * Obstructions when teleporting
        */
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("portal_test2", "Peaceful");

        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        Position player_pos = new Position(1, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        // Portals only teleport entities with their only another portal with corresponding colour
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        player_pos = new Position(5, 2);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.UP);
        player_pos = new Position(2, 2);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());


        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Boulder is at Position(3,3)
        Position boulder_pos = new Position(3, 3);
        EntityResponse boulder = newDungeon.getEntities().stream().filter(e -> e.getType().equals("boulder")).findFirst().orElse(null); //here it return null if it does not find anything
        assertEquals(boulder_pos, boulder.getPosition());
    }

    @Test
    public void advancedPortalTest3() {
        /*
        * If there is an entity on the portal, interact with that entity first
        * Only player can push a boulder when teleporting
        */
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("portal_test3", "Peaceful");

        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        List<EntityResponse> ers = newDungeon.getEntities().stream().filter(e -> e.getType().equals("boulder")).collect(Collectors.toList()); 
        System.out.println(ers);

        Position player_pos = new Position(1, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        Position boulder_pos = new Position(3, 1);
        EntityResponse boulder1 = newDungeon.getEntities().stream().filter(e -> e.getId().equals(ers.get(0).getId())).findFirst().orElse(null); //here it return null if it does not find anything
        // Boulder cannot teleport as there is another boulder in the way
        assertEquals(boulder_pos, boulder1.getPosition());

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(5, 3);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        boulder_pos = new Position(6, 3);
        EntityResponse boulder2 = newDungeon.getEntities().stream().filter(e -> e.getId().equals(ers.get(1).getId())).findFirst().orElse(null); //here it return null if it does not find anything
        // Boulder cannot teleport as there is another boulder in the way
        assertEquals(boulder_pos, boulder2.getPosition());
    }

    @Test
    public void advancedPortalTest4() {
        /*
        * No entity can teleport if there is no linked portals
        */
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("portal_test4", "Peaceful");

        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");
    
        Position player_pos = new Position(3, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player_pos = new Position(2, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Do not teleport since there is no linked portal
        assertEquals(player_pos, player.getPosition());
    }
}
