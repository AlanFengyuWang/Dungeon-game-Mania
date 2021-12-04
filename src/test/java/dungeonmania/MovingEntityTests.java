package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntityTests {

    @Test
    public void test_player_movements_withWalls_advanced() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("advanced", "Standard");    //in advanced there's no barrier on Right

        //Test move to right    
        dr = controller.tick(null, Direction.RIGHT);
        EntityResponse player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException();
        //check the entity position is on the RIGHT
        Position exp_position = new Position(2,1,0);
        assertEquals(exp_position, player.getPosition());

        //test move to left
        dr = controller.tick(null, Direction.LEFT);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,1,0);
        assertEquals(exp_position, player.getPosition());
        
        //test move to bottom
        dr = controller.tick(null, Direction.DOWN);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,2,0);
        assertEquals(exp_position, player.getPosition());

        //test move to up
        dr = controller.tick(null, Direction.UP);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,1,0);
        assertEquals(exp_position, player.getPosition());

        //player cannot move across a wall
        dr = controller.tick(null, Direction.UP);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,1,0);
        assertEquals(exp_position, player.getPosition());
    }

    @Test
    public void test_mercenary_movements_dijkstra_advanced() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("advanced", "Standard");   
        //mercenary located in (3,5)
        EntityResponse mercenary = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        if(mercenary == null) throw new IllegalAccessError("mercenary is not found");

        Position exp_position = new Position(3,5,0);
        assertEquals(exp_position, mercenary.getPosition());

        dr = controller.tick(null, Direction.RIGHT);
        mercenary = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        exp_position = new Position(2,5,0);       //expect merenary move up by one unit
        assertEquals(exp_position, mercenary.getPosition());

        dr = controller.tick(null, Direction.UP); //Here the player is not moving
        mercenary = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        //expect merenary move left by one unit
        exp_position = new Position(2,4,0);
        assertEquals(exp_position, mercenary.getPosition());

        dr = controller.tick(null, Direction.RIGHT); 
        mercenary = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        //expect merenary move left by one unit
        exp_position = new Position(2,3,0);
        assertEquals(exp_position, mercenary.getPosition());

        dr = controller.tick(null, Direction.RIGHT); 
        mercenary = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        //expect merenary move left by one unit
        exp_position = new Position(2,1,0);
        assertEquals(exp_position, mercenary.getPosition());

    }

    @Test
    public void test_battle_between_mercenary_player() {
        /**
         * player health: 100. attack: 10
         * Mercenary health: 30. attack 15
         */
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("battle_mercenary", "Standard");   

        //player moves toward mercenary
        dr = controller.tick(null, Direction.DOWN);   
        //The player now encounter the monster, since the monster moves one extra step (within battle range), the battle starts immediatelly

        /**
         * After one round, the health should be:
         * Player health = 100 - 60 = 40
         * enemy health = <0
         */
        EntityResponse player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        EntityResponse mercenary_1 = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary_1")).findFirst().orElse(null); //here it return null if it does not find anything
        assertNotNull(player);
        assertNull(mercenary_1);

        //confronting the next mercenary
        dr = controller.tick(null, Direction.DOWN);   
        dr = controller.tick(null, Direction.DOWN);   
        dr = controller.tick(null, Direction.DOWN);   
        dr = controller.tick(null, Direction.DOWN);   
        /**
         * After one round, the health should be:
         * Player health = <0
         * enemy health = >0
         */
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        assertNull(player);
    }

    @Test
    public void test_zombieToast_move() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("advanced", "Standard");    //in advanced there's no barrier on Right

        //Test move to right
        dr = controller.tick(null, Direction.RIGHT);
        EntityResponse player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException();
        //check the entity position is on the RIGHT
        Position exp_position = new Position(2,1,0);
        assertEquals(exp_position, player.getPosition());

        //test move to left
        dr = controller.tick(null, Direction.LEFT);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,1,0);
        assertEquals(exp_position, player.getPosition());
        
        //test move to bottom
        dr = controller.tick(null, Direction.DOWN);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,2,0);
        assertEquals(exp_position, player.getPosition());

        //test move to up
        dr = controller.tick(null, Direction.UP);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,1,0);
        assertEquals(exp_position, player.getPosition());

        //player cannot move across a wall
        dr = controller.tick(null, Direction.UP);
        player = dr.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        //check the entity position is on the RIGHT
        exp_position = new Position(1,1,0);
        assertEquals(exp_position, player.getPosition());
    }

    @Test
    public void test_bribe() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("bribe_mercenary", "Standard");   

        //bribe the nearby mercenary - failure (no gold)
        assertThrows(InvalidActionException.class, () -> controller.interact("mercenary_1"));
        //bribe the far away mercenary - failure
        // assertThrows(InvalidActionException.class, () -> controller.interact("mercenary_3"));
        //ID is not valid
        assertThrows(IllegalArgumentException.class, () -> controller.interact("mercenary_9"));

        dr = controller.tick(null, Direction.DOWN);   
        //pick up the gold
        //killed the nearby mercenary

        controller.interact("mercenary_2");

        //player moves down, mercenary_1 should leave a way for the player
        dr = controller.tick(null, Direction.DOWN);   

        //Test position of the mercenary  
        // EntityResponse mercenary_2 = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary_2")).findFirst().orElse(null); //here it return null if it does not find anything
        // if (mercenary_2 == null) throw new IllegalArgumentException();
        // Position exp_position = new Position(2,3,0);
        // assertEquals(exp_position, mercenary_2.getPosition());


        //player moves further down
        // dr = controller.tick(null, Direction.DOWN);  
        // //the merenary_1 should follow up
        // mercenary_2 = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary_2")).findFirst().orElse(null); //here it return null if it does not find anything
        // if (mercenary_2 == null) throw new IllegalArgumentException();
        // exp_position = new Position(2,2,0);
        // assertEquals(exp_position, mercenary_2.getPosition());


        // //player moves right, confront the next mercetary, it followed 
        // dr = controller.tick(null, Direction.RIGHT);
        // //check the mercenary_1 moves
        // mercenary_2 = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary_2")).findFirst().orElse(null); //here it return null if it does not find anything
        // if (mercenary_2 == null) throw new IllegalArgumentException();
        // exp_position = new Position(2,2,0);
        // assertNotEquals(exp_position, mercenary_2.getPosition());
    }

    @Test
    public void test_mercenaryMovements_withWalls_afterBribe() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("advanced", "Standard");   
        EntityResponse mercenary = null;
        List<EntityResponse> ers = dr.getEntities();   
        for(EntityResponse e : ers) {
            if(e.getType().contains("mercenary")) {
                mercenary = e;
            }
        }

        //mercenary is located in (3,5)
        //player bribe mercenary
        controller.interact(mercenary.getId());

        //player moves toward mercenary
        dr = controller.tick(null, Direction.RIGHT);   
        dr = controller.tick(null, Direction.DOWN);   

        //here the mercenary went up, left and then should stop moving
        Position exp_posit = new Position(2, 5);
        assertEquals(exp_posit, mercenary.getPosition());

        dr = controller.tick(null, Direction.DOWN);   
        //player keep moving down, mercenary should move to the right to keep a space
        exp_posit = new Position(3, 5);
        assertEquals(exp_posit, mercenary.getPosition());

        //player move right, mercenary should move to the right to keep a space
        dr = controller.tick(null, Direction.RIGHT);   
        exp_posit = new Position(4, 5);
        assertEquals(exp_posit, mercenary.getPosition());

        //player move right twice, mercenary should move to the right to keep a space
        dr = controller.tick(null, Direction.RIGHT);   
        exp_posit = new Position(5, 5);
        assertEquals(exp_posit, mercenary.getPosition());

        //player move right three times, mercenary should move to the right to keep a space
        dr = controller.tick(null, Direction.RIGHT);   
        exp_posit = new Position(6, 5);
        assertEquals(exp_posit, mercenary.getPosition());

        //player move right four times, mercenary should move to the right to keep a space
        dr = controller.tick(null, Direction.RIGHT);   
        exp_posit = new Position(7, 5);
        assertEquals(exp_posit, mercenary.getPosition());

        //player move right
        //now the mervenary should stay after the player continue moving to the right, because it cannot hit the wall
        dr = controller.tick(null, Direction.RIGHT);   
        exp_posit = new Position(7, 5);
        assertEquals(exp_posit, mercenary.getPosition());

        //player moves down, the mercenary stay
        dr = controller.tick(null, Direction.DOWN);   
        exp_posit = new Position(7, 5);
        assertEquals(exp_posit, mercenary.getPosition());
    }

    @Test
    public void test_healthPotion() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("testHealthPotion", "Standard");   
        //when the item is not a bomb, health_potion, invincibility_potion, or an invisibility_potion, or null (if no item is being used)


        //battled the first mercenary
        dr = controller.tick(null, Direction.DOWN);
        dr = controller.tick(null, Direction.DOWN);

        //trying to access health potion when the player does not have it
        // assertEquals(new IllegalArgumentException(), controller.tick("health_potion", Direction.DOWN));

        //pick the health potion
        dr = controller.tick("45", Direction.NONE);

        //consume the potion and then battled the next mercenary
        dr = controller.tick(null, Direction.DOWN);
        dr = controller.tick(null, Direction.DOWN);
        dr = controller.tick(null, Direction.DOWN);
        assertNotNull(dr);
    }
    
    @Test
    public void test_itemUsedInvalid() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("advanced", "Standard");   
        //when the item is not a bomb, health_potion, invincibility_potion, or an invisibility_potion, or null (if no item is being used)
        assertThrows(IllegalArgumentException.class, () -> controller.tick("treasure", Direction.DOWN));
        assertThrows(IllegalArgumentException.class, () -> controller.tick("sword", Direction.DOWN));
        
        //if itemUsed is not in the player's inventory
        assertThrows(InvalidActionException.class,() -> controller.tick("invincibility_potion", Direction.DOWN));
    }

    @Test
    public void test_consumeInvisiblePotion() {
        /**
         * test_movingEntity_advance map: it has two mercenary nearby the player
         * the player moves up by one unit, which means he's not moving, and 
        */
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("invisibility_potion", "Standard");    //in advanced there's no barrier on Right
        
        //player moves toward mercenary
        dr = controller.tick(null, Direction.RIGHT);   
        dr = controller.tick(null, Direction.RIGHT);   
        // the player has picked up the invisiblity potion
        
        EntityResponse mercenary = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null);
        
        //find invisibility potion  
        // EntityResponse invibilityPotion = dr.getEntities().stream().filter(e -> e.getType().contains("invisibility")).findFirst().orElse(null);
        // String id = invibilityPotion.getId();
        dr = controller.tick("1", Direction.NONE);   
        //here the player has already taken the invisibillePotion, mercenary should make no move anymore
        
        //record the prev position of the mercenary
        Position prev_position = mercenary.getPosition();
        for(int i = 0; i < 8; i++) {
            dr = controller.tick(null, Direction.RIGHT); 
        }

        mercenary = dr.getEntities().stream().filter(e -> e.getType().contains("mercenary")).findFirst().orElse(null);

        //now we shouldn't expect the position has changed
        assertEquals(prev_position, mercenary.getPosition()); 

        //now the position of the mercenary should be changed after 10 rounds  
        dr = controller.tick(null, Direction.RIGHT); 

        mercenary = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null);
        assertNotEquals(prev_position, mercenary.getPosition()); 

    }

    @Test
    public void test_consumeInvinciblePotion() {
        //mercenary_1 should die immediately because it can move two units to the player after one tick
        //mercenary_2 should not run toward the player first and then run away
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dr = controller.newGame("InvincibilePotionTest_movingEntity_advance", "Standard");    //in advanced there's no barrier on Right
        EntityResponse mercenary_1 = null;
        EntityResponse mercenary_2 = null;
        EntityResponse player = null;
        List<EntityResponse> ers = dr.getEntities();   
        for(EntityResponse e : ers) {
            if(e.getType().contains("mercenary_1")) {
                mercenary_1 = e;
            }
            else if(e.getType().contains("mercenary_2")) {
                mercenary_2 = e;
            }
            else if(e.getType().contains("player")) {
                player = e;
            }
        }

        //player moves toward the potion
        dr = controller.tick(null, Direction.DOWN);  
        //At this stage, the player has already consumed the potion, the mercenary_1 meets the player immediately after
        //check mercenary_1 is no longer exist
        List<EntityResponse> all_entities = dr.getEntities();
        assertEquals(false, all_entities.contains(mercenary_1));

        //make sure the mercenary nearby runs away
        dr = controller.tick("1", Direction.NONE);   
        dr = controller.tick(null, Direction.DOWN);   

        mercenary_2 = dr.getEntities().stream().filter(e -> e.getType().equals("mercenary_2")).findFirst().orElse(null); //here it return null if it does not find anything

        Position exp_posit = new Position(1, 5);
        assertNotEquals(exp_posit, mercenary_2.getPosition());

        //player chasing afterward
        dr = controller.tick(null, Direction.DOWN);  
        exp_posit = new Position(1, 6);
        assertNotEquals(exp_posit, mercenary_2.getPosition());
    }
}
