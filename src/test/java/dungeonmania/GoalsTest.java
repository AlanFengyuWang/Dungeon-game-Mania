package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class GoalsTest {
    // TODO add a visual layout of each test dungeon
    @Test
    public void testGoalExit() {
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr =  dc.newGame("testGoalsExit", "Standard");

        assertEquals(":exit", dr.getGoals());

        // move player to exit
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals("", dr.getGoals());
    }

    @Test
    public void testGoalEnemies() {
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr =  dc.newGame("testGoalsEnemies", "Standard");
        assertEquals(":enemies(1)", dr.getGoals());

        // pick up sword
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals(":enemies(1)", dr.getGoals());

        // meet mercenary and defeat it
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals("", dr.getGoals());

        // TO DO add spider and zombieToast
    }

    @Test
    public void testGoalBoulders() {
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr =  dc.newGame("testGoalsBoulders", "Standard");

        assertEquals(":boulders(3)/:switch(3)", dr.getGoals());

        // push 1/3 boulder onto switch
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals(":boulders(2)/:switch(2)", dr.getGoals());

        // push 2/3 boulder onto switch
        dr = dc.tick(null, Direction.DOWN);
        assertEquals(":boulders(1)/:switch(1)", dr.getGoals());

        // push 3/3 boulder onto switch
        dr = dc.tick(null, Direction.LEFT);
        dr = dc.tick(null, Direction.DOWN);
        assertEquals("", dr.getGoals());
    }

    @Test
    public void testGoalTreasure() {
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr =  dc.newGame("testGoalsTreasure", "Standard");

        assertEquals(":treasure(3)", dr.getGoals());

        // collect the three treasures and check goal each time
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals(":treasure(2)", dr.getGoals());
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals(":treasure(1)", dr.getGoals());
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals("", dr.getGoals());
    }

    @Test
    public void testGoalAnd() {
        DungeonManiaController dc = new DungeonManiaController();
        DungeonResponse dr =  dc.newGame("testGoalsAnd", "Standard");

        assertEquals(":exit AND :treasure(1)", dr.getGoals());

        // move player to stand on exit
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals(":exit AND :treasure(1)", dr.getGoals());

        // move player to leave exit and collect treasure
        dr = dc.tick(null, Direction.RIGHT);
        assertEquals(":exit AND :treasure(0)", dr.getGoals());

        // move player back to exit having collected all treasure
        dr = dc.tick(null, Direction.LEFT);
        assertEquals("", dr.getGoals());
    }

    @Test
    public void testGoalOr() {
        // test only completing exit
        DungeonManiaController dc1 = new DungeonManiaController();
        DungeonResponse dr1 =  dc1.newGame("testGoalsOr", "Standard");

        assertEquals(":exit OR :treasure(1)", dr1.getGoals());

        // move player to stand on exit
        dr1 = dc1.tick(null, Direction.RIGHT);
        assertEquals("", dr1.getGoals());

        // test only completing treasure
        DungeonManiaController dc2 = new DungeonManiaController();
        DungeonResponse dr2 =  dc2.newGame("testGoalsOr", "Standard");

        assertEquals(":exit OR :treasure(1)", dr2.getGoals());

        // move player to collect treasure
        dr2 = dc2.tick(null, Direction.DOWN);
        assertEquals("", dr2.getGoals());
    }
}

// how goals are constructed in the dungeon json

// "goal-condition": {
//     "goal": "AND",
//     "subgoals": [
//       {
//         "goal": "enemies"
//       },
//       {
//         "goal": "treasure"
//       }
//     ]
//   }

//   "goal-condition": {
//     "goal": "exit"
//   }
