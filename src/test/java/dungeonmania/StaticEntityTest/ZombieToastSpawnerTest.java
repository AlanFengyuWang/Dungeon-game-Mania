package dungeonmania.StaticEntityTest;

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

public class ZombieToastSpawnerTest {
    // David's test with static entities
    /**
     * Check if zombies spawn in 20 ticks
     */
    @Test
    public void basicZombieToastSpawnerTest1() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("zombie_toast_spawner1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // 2 spawners
        // Zombie spawn after 20 ticks
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);

        // Adjacent squares must be empty to spawn zombie
        Position spawnPos = new Position(2,3);
        List<EntityResponse> zombies = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect(Collectors.toList());
        List<Position> zombiePos = new ArrayList<>();
        for (EntityResponse zombie: zombies) {
            zombiePos.add(zombie.getPosition());
        }
        assertTrue(zombiePos.contains(spawnPos));
        int zombieNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        // 2 zombies
        assertTrue(zombieNo == 2);

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Pick up sword
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        int zombieSpawnerNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast_spawner")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        assertTrue(zombieSpawnerNo == 2);
        newDungeon = dungeonStart.interact("zombie_toast_spawner");
        // Destroy zombie toast spawner
        zombieSpawnerNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast_spawner")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        assertTrue(zombieSpawnerNo == 1);
    }

    /**
     * Check if zombies spawn in 15 ticks in hard mode
     */
    @Test
    public void basicZombieToastSpawnerTest2() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("zombie_toast_spawner1", "Hard");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // 2 spawners
        // Zombie spawn after 15 ticks
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        // Adjacent squares must be empty to spawn zombie
        int zombieNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        // 2 zombies
        assertTrue(zombieNo == 2);
    }

    /**
     * Destroy spawner
     */
    @Test
    public void basicZombieToastSpawnerTest3() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("zombie_toast_spawner1", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        // Pick up sword
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        int zombieSpawnerNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast_spawner")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        assertTrue(zombieSpawnerNo == 2);
        newDungeon = dungeonStart.interact("zombie_toast_spawner");
        // Destroy zombie toast spawner
        zombieSpawnerNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast_spawner")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        assertTrue(zombieSpawnerNo == 1);
    }

    @Test
    public void basicZombieToastSpawnerTest4() {
        // ========== Create Dungeon ==========
        // Create a new game
        DungeonManiaController dungeonStart = new DungeonManiaController();
        // Create dungeon from exisitng json file
        DungeonResponse newDungeon =  dungeonStart.newGame("zombie_toast_spawner2", "Peaceful");

        // Get player
        EntityResponse player = newDungeon.getEntities().stream().filter(e -> e.getType().equals("player")).findFirst().orElse(null); //here it return null if it does not find anything
        if (player == null) throw new IllegalArgumentException("Player does not exist");

        // 2 spawners
        // Zombie spawn after 20 ticks
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);
        newDungeon = dungeonStart.tick(null, Direction.RIGHT);

        // 0 zombies since no empty tile
        int zombieNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        assertTrue(zombieNo == 0);
        // Adjacent squares must be empty to spawn zombie
        // All squares are block so no zombies spawn
        newDungeon = dungeonStart.tick(null, Direction.LEFT);
        // Can spawn as soon as a tile is empty
        zombieNo = newDungeon.getEntities().stream().filter(e -> e.getType().equals("zombie_toast")).collect(Collectors.toList()).size(); //here it return null if it does not find anything
        assertTrue(zombieNo == 1);
    }
}
