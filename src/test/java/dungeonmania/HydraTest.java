package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HydraTest {
    @Test
    public void testHydraCreation() {
        DungeonManiaController dcHard = new DungeonManiaController();
        DungeonResponse drHard =  dcHard.newGame("testHydraSpawn", "Hard");
        Stream<EntityResponse> hHard = TestHelpers.getEntityResponseStream(drHard, "hydra");

        DungeonManiaController dcStandard = new DungeonManiaController();
        DungeonResponse drStandard =  dcStandard.newGame("testHydraSpawn", "Standard");
        Stream<EntityResponse> hStandard = TestHelpers.getEntityResponseStream(drStandard, "hydra");

        DungeonManiaController dcPeaceful = new DungeonManiaController();
        DungeonResponse drPeaceful =  dcPeaceful.newGame("testHydraSpawn", "Peaceful");
        Stream<EntityResponse> hPeaceful = TestHelpers.getEntityResponseStream(drPeaceful, "hydra");

        assertEquals(0, hHard.count());
        assertEquals(0, hStandard.count());
        assertEquals(0, hPeaceful.count());

        // check that hydra spawns 5 times after 250 ticks in hard mode
        // but 0 times in standard and peaceful modes
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j <50; j++) {
                drHard = dcHard.tick(null, Direction.NONE);
                drStandard = dcStandard.tick(null, Direction.NONE);
                drPeaceful = dcPeaceful.tick(null, Direction.NONE);
            }
            hHard = TestHelpers.getEntityResponseStream(drHard, "hydra");
            hStandard = TestHelpers.getEntityResponseStream(drStandard, "hydra");
            hPeaceful = TestHelpers.getEntityResponseStream(drPeaceful, "hydra");
            assertEquals(i, hHard.count());
            assertEquals(0, hStandard.count());
            assertEquals(0, hPeaceful.count());
        }
    }

    @Test
    public void testHydraMovementRandom() {
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr;
        EntityResponse h;

        Position hLocation;
        Position start = new Position(8, 3);
        Position left = new Position(7, 3);
        Position right = new Position(9, 3);
        Position up = new Position(8, 2);
        Position down = new Position(8, 4);
        Boolean visitedLeft = false, visitedRight = false, visitedUp = false, visitedDown = false, visitedInvalid = false;

        for (int i = 0; i < 100; i++) {
            dr = dc.newGame("testHydraSpawn", "Hard");
            h = TestHelpers.getEntityResponse(dr, "hydra");
            assertEquals(start, h.getPosition());
            
            for (int j = 0; j < 50; j++) {
                dr = dc.tick(null, Direction.NONE);
            }
            hLocation = TestHelpers.getEntityResponse(dr, "hydra").getPosition();

            // check that the hydra only moves left, right, up or down
            // and take note when it has gone a particualr direction
            if (hLocation.equals(left)) {
                visitedLeft = true;
            } else if (hLocation.equals(right)) {
                visitedRight = true;
            } else if (hLocation.equals(up)) {
                visitedUp = true;
            } else if (hLocation.equals(down)) {
                visitedDown = true;
            } else {
                visitedInvalid = true;
            }
        }
        // assertTrue(visitedLeft);
        assertTrue(visitedRight);
        assertTrue(visitedUp);
        assertTrue(visitedDown);
        assertFalse(visitedInvalid);
    }

    @Test
    public void testHydraToastMovementMapInteraction1() {

        // dungeon format:
        //  ----*----*----*    . = hydra test locations
        // |        w      |   x = player
        // |       d.Dkx   |   d = closed door
        // |        z      |   D = open door
        // |               |   every other item uses their first letter
        //  ----*----*----*

        // testing map interaction 1 with wall, doors and zombieToastSpawner
        // should only be able to move right after door is unlocked
            // can go to open door
            // blocked by closed door, wall and zombieToastSpawner
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr;
        EntityResponse h;
        Position start = new Position(8, 1);

        for (int i = 0; i < 100; i++) {
            dr = dc.newGame("testHydraMap1", "Hard");
            h = TestHelpers.getEntityResponse(dr, "hydra");
            assertEquals(start, h.getPosition());

            // player moves left to pick up key, hydra should remain in same position
            dr = dc.tick(null, Direction.LEFT);
            h = TestHelpers.getEntityResponse(dr, "hydra");
            assertEquals(start, h.getPosition());

            // player moves left to unlock door
            // hydra also moves to unlocked door space and the player defeats it in battle
            dr = dc.tick(null, Direction.LEFT);
            h = TestHelpers.getEntityResponse(dr, "hydra");
            assertNull(h);
            assertNotNull(TestHelpers.getEntityResponse(dr, "player"));
        }
    }

    @Test
    public void testHydraToastMovementMapInteraction2() {

        // dungeon format:
        //  ----*----*----*    . = hydra test locations
        // |x  p    p      |   x = player
        // |       f.b     |
        // |        e      |
        // |               |   every other item uses their first letter
        //  ----*----*----*

        // testing map interaction 2 with portals, floorSwitch, boulder and exit
        // should only be able to not move right due to the boulder
            // can go to floorSwitch, exit and portal (without being transported)
            // blocked by boulder
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr;
        EntityResponse h;

        Position hLocation;
        Position start = new Position(8, 1);
        Position left = new Position(7, 1);
        Position up = new Position(8, 0);
        Position down = new Position(8, 2);
        Boolean visitedLeft = false, visitedUp = false, visitedDown = false, visitedInvalid = false;

        for (int i = 0; i < 100; i++) {
            dr = dc.newGame("testHydraMap2", "Hard");
            h = TestHelpers.getEntityResponse(dr, "hydra");
            assertEquals(start, h.getPosition());

            // player moves to generate tick
            dr = dc.tick(null, Direction.RIGHT);
            h = TestHelpers.getEntityResponse(dr, "hydra");
            hLocation = h.getPosition();

            if (hLocation.equals(left)) {
                visitedLeft = true;
            } else if (hLocation.equals(up)) {
                visitedUp = true;
            } else if (hLocation.equals(down)) {
                visitedDown = true;
            } else {
                visitedInvalid = true;
            }
        }
        assertTrue(visitedLeft);
        assertTrue(visitedUp);
        assertTrue(visitedDown);
        assertFalse(visitedInvalid);
    }

    @Test
    public void testHydraToastMovementInvincibilityPotion() {
        // dungeon format:
        // --------   - = wall
        // -pxkd h-   x = player
        // --------   p = invincibility potion
        // every other item uses their first letter
        Position rightMostSquare = new Position(6, 1);
        Position leftOfRightMost = new Position(5, 1);
        
        // test that WITHOUT the invincibility potion the hydra
        // moves both left and right
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr = dc.newGame("testHydraBattle", "Standard");
        EntityResponse h = TestHelpers.getEntityResponse(dr, "hydra");
        
        // move right, hydra moves left and right
        dr = dc.tick(null, Direction.RIGHT);
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(leftOfRightMost, h.getPosition());
        dr = dc.tick(null, Direction.RIGHT); // unlocks door
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(rightMostSquare, h.getPosition());
        dr = dc.tick(null, Direction.LEFT);
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(leftOfRightMost, h.getPosition());

        // test that WITH the invincibility potion the hydra
        // moves only to the right or doesnt move to avoid the player
        dr = dc.newGame("testHydraBattle", "Standard");
        h = TestHelpers.getEntityResponse(dr, "hydra");

        // get invincibility potion and use, hydra remains at the rightMostSquare
        // while invincbility is active on the player
        dr = dc.tick(null, Direction.LEFT); // picks up invincibility potion
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(leftOfRightMost, h.getPosition());

        // TODO use invincibility potion here
        dr = dc.tick(null, Direction.RIGHT);
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(rightMostSquare, h.getPosition());

        // test that the hydra stays at the right most square
        dr = dc.tick(null, Direction.RIGHT);
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(rightMostSquare, h.getPosition());
        dr = dc.tick(null, Direction.RIGHT); // unlocks door
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(rightMostSquare, h.getPosition());
        dr = dc.tick(null, Direction.RIGHT);
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(rightMostSquare, h.getPosition());
        dr = dc.tick(null, Direction.LEFT); // back to door
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(rightMostSquare, h.getPosition());
        dr = dc.tick(null, Direction.RIGHT);
        h = TestHelpers.getEntityResponse(dr, "hydra");
        assertEquals(rightMostSquare, h.getPosition());

        // test battle with hydra while invincibility potion is active
        // battle the hydra, winning immediately
        dr = dc.tick(null, Direction.RIGHT); // reach the hydra
        assertNull(TestHelpers.getEntityResponse(dr, "hydra"));
        assertNotNull(TestHelpers.getEntityResponse(dr, "player"));

        // test that hydra returns to regular random movement once
        // invincibility potion wears off
        // TODO figure out how many ticks the potion lasts and test it
        dr = dc.newGame("testHydraBattle", "Standard");
        h = TestHelpers.getEntityResponse(dr, "hydra");

        // pick up invincibility potion and use
        dr = dc.tick(null, Direction.LEFT);
        // TODO use invincibility potion
        // then for loop right-left until invincibility potion wears off
        // then test hydra resumes regular movement
    }

    @Test
    public void testHydraBattlePlayer() {
        // TODO write test
        // When a hydra is attacked by the character or allies,
        // there is a 50% chance that its health will increase rather than decrease
        // by the attacking damage as two heads have grown back when one is cut off.
    }

    @Test
    public void testHydraBattleAlly() {
        // TODO write test
    }
}
