package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastTest {
    @Test
    public void testZombieToastCreate() {
        // dungeon format:
        //  ----*----*----*    . = zombie test location (8, 3)
        // |x              |   x = player
        // |               |
        // |       .       |
        // |               |
        // |               |
        //  ----*----*----*

        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr =  dc.newGame("testZombieMovement", "Standard");
        EntityResponse zt = TestHelpers.getEntityResponse(dr, "zombie_toast");

        // check zombieToast has correct starting location according to the test map
        assertEquals(new Position(8, 3), zt.getPosition());

        // use a zombie spawner??
        // test that its health is 10 (??)
        // check that its attackDamage = 5
    }

    @Test
    public void testZombieToastMovementRandom() {
        // same dungeon format as testZombieToastCreate()
        // player at (0, 0), zombieToast at (8, 3)
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr;
        EntityResponse zt;

        Position zLocation;
        Position start = new Position(8, 3);
        Position left = new Position(7, 3);
        Position right = new Position(9, 3);
        Position up = new Position(8, 2);
        Position down = new Position(8, 4);
        Boolean visitedLeft = false, visitedRight = false, visitedUp = false, visitedDown = false, visitedInvalid = false;

        for (int i = 0; i < 100; i++) {
            dr = dc.newGame("testZombieMovement", "Standard");
            zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
            assertEquals(start, zt.getPosition());
            dr = dc.tick(null, Direction.RIGHT);
            zLocation = TestHelpers.getEntityResponse(dr, "zombie_toast").getPosition();

            // check that the zombie only moves left, right, up or down
            // and take note when it has gone a particualr direction
            if (zLocation.equals(left)) {
                visitedLeft = true;
            } else if (zLocation.equals(right)) {
                visitedRight = true;
            } else if (zLocation.equals(up)) {
                visitedUp = true;
            } else if (zLocation.equals(down)) {
                visitedDown = true;
            } else {
                visitedInvalid = true;
            }
        }
        assertTrue(visitedLeft);
        assertTrue(visitedRight);
        assertTrue(visitedUp);
        assertTrue(visitedDown);
        assertFalse(visitedInvalid);
    }

    @Test
    public void testZombieToastMovementMapInteraction1() {

        // dungeon format:
        //  ----*----*----*    . = zombie test locations
        // |        w      |   x = player
        // |       d.Dkx   |   d = closed door
        // |        z      |   D = open door
        // |               |   every other item uses their first letter
        //  ----*----*----*

        // testing map interaction 1 with wall, doors and zombieToasSpawner
        // should only be able to move right after door is unlocked
            // can go to open door
            // blocked by closed door, wall and zombieToastSpawner
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr;
        EntityResponse zt;
        Position start = new Position(8, 1);
        Position right = new Position(9, 1);

        for (int i = 0; i < 100; i++) {
            dr = dc.newGame("testZombieMap1", "Peaceful");
            zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
            assertEquals(start, zt.getPosition());

            // player moves left to pick up key, zombie should remain in same position
            dr = dc.tick(null, Direction.LEFT);
            zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
            assertEquals(start, zt.getPosition());

            // player moves left to unlock door
            // ?? should zombie also move to unlocked door space? or stay in place till next tick?
            // i think stay in place atm
            dr = dc.tick(null, Direction.LEFT);
            zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
            assertNull(zt);
            assertNotNull(TestHelpers.getEntityResponse(dr, "player"));
        }
    }

    @Test
    public void testZombieToastMovementMapInteraction2() {

        // dungeon format:
        //  ----*----*----*    . = zombie test locations
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
        EntityResponse zt;

        Position zLocation;
        Position start = new Position(8, 1);
        Position left = new Position(7, 1);
        Position up = new Position(8, 0);
        Position down = new Position(8, 2);
        Boolean visitedLeft = false, visitedUp = false, visitedDown = false, visitedInvalid = false;

        for (int i = 0; i < 100; i++) {
            dr = dc.newGame("testZombieMap2", "Peaceful");
            zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
            assertEquals(start, zt.getPosition());

            // player moves to generate tick
            dr = dc.tick(null, Direction.RIGHT);
            zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
            zLocation = zt.getPosition();

            if (zLocation.equals(left)) {
                visitedLeft = true;
            } else if (zLocation.equals(up)) {
                visitedUp = true;
            } else if (zLocation.equals(down)) {
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
    public void testZombieToastMovementInvincibilityPotion() {
        // dungeon format:
        // --------   - = wall
        // -pxkd z-   x = player
        // --------   p = invincibility potion
        // every other item uses their first letter
        Position rightMostSquare = new Position(6, 1);
        Position leftOfRightMost = new Position(5, 1);
        
        // test that WITHOUT the invincibility potion the zombieToast
        // moves both left and right
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr = dc.newGame("testZombieBattle", "Standard");
        EntityResponse zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        
        // move right, zombieToast moves left and right
        dr = dc.tick(null, Direction.RIGHT);
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(leftOfRightMost, zt.getPosition());
        dr = dc.tick(null, Direction.RIGHT); // unlocks door
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(rightMostSquare, zt.getPosition());
        dr = dc.tick(null, Direction.LEFT);
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(leftOfRightMost, zt.getPosition());

        // test that WITH the invincibility potion the zombieToast
        // moves only to the right or doesnt move to avoid the player
        dr = dc.newGame("testZombieBattle", "Standard");
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");

        // get invincibility potion and use, zombieToast remains at the rightMostSquare
        // while invincbility is active on the player
        dr = dc.tick(null, Direction.LEFT); // picks up invincibility potion
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(leftOfRightMost, zt.getPosition());

        // TODO use invincibility potion here
        dr = dc.tick(null, Direction.RIGHT);
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(rightMostSquare, zt.getPosition());

        // test that the ZombieToast stays at the right most square
        dr = dc.tick(null, Direction.RIGHT);
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(rightMostSquare, zt.getPosition());
        dr = dc.tick(null, Direction.RIGHT); // unlocks door
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(rightMostSquare, zt.getPosition());
        dr = dc.tick(null, Direction.RIGHT);
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(rightMostSquare, zt.getPosition());
        dr = dc.tick(null, Direction.LEFT); // back to door
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(rightMostSquare, zt.getPosition());
        dr = dc.tick(null, Direction.RIGHT);
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");
        assertEquals(rightMostSquare, zt.getPosition());

        // test battle with zombieToast while invincibility potion is active
        // battle the zombieToast, winning immediately
        dr = dc.tick(null, Direction.RIGHT); // reach the zombieToast
        assertNull(TestHelpers.getEntityResponse(dr, "zombie_toast"));
        assertNotNull(TestHelpers.getEntityResponse(dr, "player"));

        // test that zombie returns to regular random movement once
        // invincibility potion wears off
        // TODO figure out how many ticks the potion lasts and test it
        dr = dc.newGame("testZombieBattle", "Standard");
        zt = TestHelpers.getEntityResponse(dr, "zombie_toast");

        // pick up invincibility potion and use
        dr = dc.tick(null, Direction.LEFT);
        // TODO use invincibility potion
        // then for loop right-left until invincibility potion wears off
        // then test zombie resumes regular movement
    }

    @Test
    public void testZombieToastBattle() {

    }

    @Test
    public void testZombieToastSpawnWithArmour() {
        // ?
    }

    @Test
    public void testZombieToastDropArmour() {
        // ?
    }

    // @Test
    // public void testZombieToastPeacefulMode() {

    // }

    // @Test
    // public void testZombieToastHardMode() {

    // }
}
