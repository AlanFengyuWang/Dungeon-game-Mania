package dungeonmania.StaticEntityTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ExitTest {
    /**
    * Player can be on exit
    **/
    @Test
    public void basicExitTest1() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("exit_test1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Player can be on exit
        EntityResponse exit = newDungeon.getEntities().stream().filter(e -> e.getType().equals("exit")).findFirst().orElse(null); //here it return null if it does not find anything
        Position exitPos = exit.getPosition();
        player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        Position playerPos = player.getPosition();
        assertEquals(exitPos, playerPos);
    }

    /**
    * Mercenary can be on exit
    **/
    @Test
    public void basicExitTest2() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("exit_test2", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Player can be on exit
        EntityResponse exit = newDungeon.getEntities().stream().filter(e -> e.getType().equals("exit")).findFirst().orElse(null); //here it return null if it does not find anything
        Position exitPos = exit.getPosition();
        EntityResponse mercenary = newDungeon.getEntities().stream().filter(e -> e.getType().equals("mercenary")).findFirst().orElse(null); //here it return null if it does not find anything
        Position mercenaryPos = mercenary.getPosition();

        assertEquals(exitPos, mercenaryPos);
    }

    /**
    * Zombie can be on exit
    **/
    @Test
    public void basicExitTest3() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("exit_test3", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Player can be on exit
        List<EntityResponse> exits = newDungeon.getEntities().stream().filter(e -> e.getType().equals("exit")).collect(Collectors.toList()); //here it return null if it does not find anything
        List<Position> exitsPos = new ArrayList<>();
        for (EntityResponse exit: exits) {
            Position exitPos = exit.getPosition();
            exitsPos.add(exitPos);
        }
        EntityResponse zombie = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).findFirst().orElse(null); //here it return null if it does not find anything
        Position zombiePos = zombie.getPosition();

        assertTrue(exitsPos.contains(zombiePos));
    }
}
