package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderTest {
    @Test
    public void testSpiderCreate() {
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr;
        List<EntityResponse> sList = new ArrayList<EntityResponse>();

        for (int i = 0; i < 100; i++) {
            dr = dc.newGame("testSpiderMovement", "Standard");
            sList = dr.getEntities().stream().filter(e -> e.getType().equals("spider")).map(e -> e).collect(Collectors.toList());
            assertTrue(sList.size() <= 4 && sList.size() > 0);
        }
    }

    @Test
    public void testSpiderMovement() {
        // dungeon format
        //  0123
        // 0p
        // 1
        // 2  s
        // 3
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr = dc.newGame("testSpiderMovement", "Standard");
        EntityResponse s = dr.getEntities().stream().filter(e -> e.getType().equals("spider")).findFirst().orElse(null);

        assertEquals(new Position(2, 2), s.getPosition());

        // spider is at (2, 2)
        List<Position> expectedPath = new ArrayList<Position>(Arrays.asList(new Position(2, 1), new Position(3, 1),
            new Position(3, 2), new Position(3, 3), new Position(2, 3), new Position(1, 3), new Position(1, 2), new Position(1, 1)));
        int numValidMoves = 8;

        // check the spider follows the valid path for 3 cycles
        for (int i = 8; i < 33; i++) {
            dr = dc.tick(null, Direction.NONE);
            s = dr.getEntities().stream().filter(e -> e.getType().equals("spider")).findFirst().orElse(null);
            assertEquals(expectedPath.get(i % numValidMoves), s.getPosition());
        }
    }

    @Test
    public void testSpiderMovementMapInteraction() {
        // dungeon format
        //  0123
        // 0x      x = player
        // 1 bwp
        // 2 esf
        // 3 wpd
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr = dc.newGame("testSpiderMap", "Standard");
        EntityResponse s = dr.getEntities().stream().filter(e -> e.getType().equals("spider")).findFirst().orElse(null);

        List<Position> expectedPath = new ArrayList<Position>(Arrays.asList(new Position(2, 1), new Position(3, 1),
            new Position(3, 2), new Position(3, 3), new Position(2, 3), new Position(1, 3), new Position(1, 2),
            new Position(1, 3), new Position(2, 3), new Position(3, 3), new Position(3, 2), new Position(3, 1),
            new Position(2, 1), new Position(3, 1), new Position(3, 2), new Position(3, 3)));

        for (int i = 0; i < 16; i++) {
            dr = dc.tick(null, Direction.NONE);
            s = dr.getEntities().stream().filter(e -> e.getType().equals("spider")).findFirst().orElse(null);
            assertEquals(expectedPath.get(i), s.getPosition());
        }
    }


    @Test
    public void testSpiderBattle() {
        // dungeon format
        //  0123
        // 0p
        // 1
        // 2  s
        // 3
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr = dc.newGame("testSpiderMovement", "Standard");

        dr = dc.tick(null, Direction.NONE);
        dr = dc.tick(null, Direction.DOWN);
        dr = dc.tick(null, Direction.DOWN);
        dr = dc.tick(null, Direction.DOWN);
        dr = dc.tick(null, Direction.RIGHT); // should meet spider at (2, 3)

        assertNotNull(dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null));
        assertNull(dr.getEntities().stream().filter(e -> e.getType().equals("spider")).findFirst().orElse(null));
    }
}
