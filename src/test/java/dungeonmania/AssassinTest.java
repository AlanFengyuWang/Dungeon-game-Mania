package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class AssassinTest {
    @Test
    public void testAssassinSpawn() {
        //  ----*----*----*
        // |wwwwwwwww
        // |w       b 
        // |w     w   
        // |wp    w
        // |wwwwwww

        // should spawn less than 30% of the time instead of a mercenary
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr =  dc.newGame("testAssassinSpawn", "Peaceful");
        Stream<EntityResponse> a = TestHelpers.getEntityResponseStream(dr, "assassin");
        Stream<EntityResponse> m = TestHelpers.getEntityResponseStream(dr, "mercenary");

        // dr = dc.tick(null, Direction.RIGHT);

        int countAssassin = 0;
        int countMercenary = 0;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j <30; j++) {
                dr = dc.tick(null, Direction.RIGHT);
            }
            countAssassin += TestHelpers.getEntityResponseStream(dr, "assassin").count();
            countMercenary += TestHelpers.getEntityResponseStream(dr, "mercenary").count();
        }
        // a = TestHelpers.getEntityResponseStream(dr, "assassin");
        // m = TestHelpers.getEntityResponseStream(dr, "mercenary");
        // assertEquals(100, countAssassin + countMercenary);
        assertEquals(20, countAssassin);
        // assertTrue(countAssassin < 40 && countAssassin > 0);
        // assertTrue(countMercenary < 80 && countMercenary > 0);
    }

    @Test
    public void testAssassinBattle() {
        // deals significantly more damage than a mercenary
    }

    @Test void testAssassinBribe() {
        // requires the same amount of gold as the mercenary but also requires the one ring
    }
}
