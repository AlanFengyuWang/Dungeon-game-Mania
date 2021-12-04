package dungeonmania;

import java.beans.Transient;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


/**
 * Collectables:
 * - 
 */

public class CollectabilityTest {

    @Test
    public void test_CollectInvincibilityPotion() {
        // String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        // String[] cars2 = {"Volvo", "BMW", "Ford", "Mazda"};
        // assertEquals(cars, cars2);
        DungeonManiaController dungeonStart = new DungeonManiaController();
        DungeonResponse dungeon =  dungeonStart.newGame("advanced.json", "Standard");
        EntityResponse player = null;
        for (EntityResponse entity: dungeon.getEntities()) {
            if (entity.getType() == "player") {
                player = entity;
                // System.out.println(player);
            }
        }
        // ====== Picks Up Invincibility Potion and Consumes it ======
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.RIGHT);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        dungeon = dungeonStart.tick(null, Direction.DOWN);
        // Check if player is in the right position
        Position exp_position = new Position(11,10,0);
        assertEquals(exp_position, player.getPosition());
        // Player picks up invisibility potion
        // Check inventory for potion
        // assertTrue(newDungeon.getInventory().get(0).equal(invincibility_potion));
        // Player consumes potion
        

    }
    
    @Test
    public void test_inventory() {
        //test picking up one item
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("inventory-advance", "Standard");

        //player is approaching the sword
        dr = controller.tick(null, Direction.RIGHT);
        dr = controller.tick(null, Direction.RIGHT);
        dr = controller.tick(null, Direction.RIGHT);
        dr = controller.tick(null, Direction.RIGHT);
        dr = controller.tick(null, Direction.RIGHT);
        //player picks the sword now

        //check only the sword exist
        List<ItemResponse> inventory = dr.getInventory();
        ItemResponse sword = inventory.stream().filter(i -> i.getType().contains("sword")).findAny().orElse(null);
        
        assertEquals(true, sword.getType().contains("sword")); 
        assertEquals(1, inventory.size()); 
        //check the sword is not in the map
        List<EntityResponse> er = dr.getEntities();

        //debg
        List <EntityResponse> sword_1 = er.stream().filter(i -> i.getType().contains("sword_1")).collect(Collectors.toList()); 
        // er.stream().forEach(i -> System.out.println(i.getType()));

        // EntityResponse sword_1 = er.stream().filter(i -> i.getType().contains("sword_1")).findAny().orElse(null);
        assertEquals(true, sword_1.isEmpty());


        //move left and then pick up two swords
        dr = controller.tick(null, Direction.RIGHT);
        dr = controller.tick(null, Direction.RIGHT);
        dr = controller.tick(null, Direction.RIGHT);
        inventory = dr.getInventory();
        List <ItemResponse> swords = inventory.stream().filter(i -> i.getType().contains("sword")).collect(Collectors.toList());

        //debg
        // swords.stream().forEach(s -> System.out.println(s.getType()));;
        assertEquals(2, inventory.size()); 
        

    }
}
