package dungeonmania.StaticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LightbulbTest {
    /**
     * Lightbulb with Or logic
     */
    @Test
    public void basicLightbulbTest1() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("lightbulb_test1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        EntityResponse bulbOff = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_off")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = bulbOff.getPosition();
        // Push boulder right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse bulbOn = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = bulbOn.getPosition();
        assertEquals(oldPos, newPos);
    }

    /**
     * Lightbulb with And logic
     */
    @Test
    public void basicLightbulbTest2() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("lightbulb_test2", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        EntityResponse bulbOff = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_off")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = bulbOff.getPosition();
        // Push boulder right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Switch is not on
        assertNull(newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null));
        // Trigger other switch
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        EntityResponse bulbOn = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = bulbOn.getPosition();
        assertEquals(oldPos, newPos);
    }

    /**
     * Lightbulb with Not logic
     */
    @Test
    public void basicLightbulbTest3() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("lightbulb_test3", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // Connected with not, switch on
        assertNull(newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_off")).findFirst().orElse(null));
        EntityResponse bulbOn = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = bulbOn.getPosition();
        // Push boulder right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        EntityResponse bulbOff = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_off")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = bulbOff.getPosition();
        assertNull(newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null));
        assertEquals(oldPos, newPos);
    }

    /**
     * Lightbulb with Xor logic
     */
    @Test
    public void basicLightbulbTest4() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("lightbulb_test4", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        EntityResponse bulbOff = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_off")).findFirst().orElse(null); //here it return null if it does not find anything
        Position oldPos = bulbOff.getPosition();
        assertNull(newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null));
        // Push boulder right
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Only 1 activate switch hence light bulb is activated
        assertNull(newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_off")).findFirst().orElse(null));
        EntityResponse bulbOn = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null); //here it return null if it does not find anything
        Position newPos = bulbOn.getPosition();
        assertEquals(oldPos, newPos);
        // Trigger other switch
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        // Lightbulb not activated since can only be 1 active switch
        assertNull(newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null));
        // Untrigger other switch
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.UP);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Lightbulb switched on
        assertNull(newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_off")).findFirst().orElse(null));
        bulbOn = newDungeon.getEntities().stream().filter(e -> e.getType().equals("light_bulb_on")).findFirst().orElse(null); //here it return null if it does not find anything
        newPos = bulbOn.getPosition();
    }
}
