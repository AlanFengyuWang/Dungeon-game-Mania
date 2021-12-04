package dungeonmania.StaticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwitchTest {
    /**
    * Switch does not block player or boulder
    **/
    @Test
    public void basicSwitchTest() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("switch_test1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        Position player_pos = new Position(2,1);
        // Nothing happens when player stands on switch 
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());

        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        player_pos = new Position(1, 1);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player_pos = new Position(1, 2);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        player_pos = new Position(1, 3);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        player_pos = new Position(2, 3);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        assertEquals(player_pos, player.getPosition());
        newDungeon = dungeonStart.tick(null, Direction.UP);
        player_pos = new Position(2, 2);
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null);
        // Game clear since boulder is on switch
        assertEquals(player_pos, player.getPosition());
    }

    /**
     * Test for fun
     */
    // @Test
    // public void advancedSwitchTest() {
    //     // ClearPath = r1, u1, r2, d1, l3, r3, d3, l1, d1, r1, u4, l2, d1, r1, d1, r1, d2, l2, d1, l2, u2, d1, r1
    //     // Create a new game
    //     DungeonManiaController dungeonStart = new DungeonManiaController();
    //     // Create dungeon from exisitng json file
    //     DungeonResponse newDungeon =  dungeonStart.newGame("boulders", "Peaceful");
        
    //     // Movement to solve puzzle
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.UP);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.UP);
    //     newDungeon = dungeonStart.tick(null, Direction.UP);
    //     newDungeon = dungeonStart.tick(null, Direction.UP);
    //     newDungeon = dungeonStart.tick(null, Direction.UP);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.LEFT);
    //     newDungeon = dungeonStart.tick(null, Direction.UP);
    //     newDungeon = dungeonStart.tick(null, Direction.UP);
    //     newDungeon = dungeonStart.tick(null, Direction.DOWN);
    //     newDungeon = dungeonStart.tick(null, Direction.RIGHT);
    // }
}
