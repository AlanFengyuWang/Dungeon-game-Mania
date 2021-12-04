package dungeonmania.BuildableEntityTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class AdvancedBuildableEntityTest {
    @Test
    public void advancedBuildableEntityTest() {
        /*** 
        * When you have enough materials to craft 1 buildable out of 
        * the possible 2. The other one disappears as well when you 
        * craft because you cannot craft it anymore.
        ***/
        
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("advanced-2", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // See reference implementation for example
        // https://cs2511-reference-implementation.azurewebsites.net/app/
        // Make advanced dungeon 

        // ========== Pick up items ==========
        // Move player right x 5
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        // ========== Check inventory ==========
        // 1 item is in inventory
        assertTrue(newDungeon.getInventory().size() == 1);
        // Check if pickup sword
        assertTrue(newDungeon.getInventory().get(0).getType().contains("sword"));

        // Move player right x 5
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        // Move player down x 3
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);

        // ========== Check inventory ==========
        // 1 items are in inventory
        assertTrue(newDungeon.getInventory().size() == 1);
        // Check if pickup sword should still be inventory   
        assertTrue(newDungeon.getInventory().get(0).getType().contains("sword"));

        // Move player down x 10
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // Shield now craftable
        assertTrue(newDungeon.getBuildables().size() == 1);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        newDungeon = dungeonStart.tick(null, Direction.DOWN);
        // 7 items are in inventory
        assertTrue(newDungeon.getInventory().size() == 6);

        // Move player right x 2
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Bow now craftable
        assertTrue(newDungeon.getBuildables().size() == 2);

        // ========== Crafting ==========
        // Try crafting
        // Craft a shield
        newDungeon = dungeonStart.build("shield");
        assertTrue(newDungeon.getInventory().size() == 5);
        // No materials left
        assertTrue(newDungeon.getBuildables().size() == 0);

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        assertTrue(newDungeon.getInventory().size() == 6);
        assertTrue(newDungeon.getBuildables().size() == 1);

        // Craft a bow
        newDungeon = dungeonStart.build("bow");
        assertTrue(newDungeon.getInventory().size() == 3);
        assertTrue(newDungeon.getBuildables().size() == 0);
    }
}
