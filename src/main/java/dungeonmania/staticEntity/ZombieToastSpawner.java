package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.entity.Entity;
import dungeonmania.entity.PlayerInteract;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.ZombieToast;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.collectableEntity.Sword;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.Bow;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity implements PlayerInteract {
    private int tickCounter = 0;
    private int spawnTimer = 20;
        
    /**
     * Constructor for ZombieToastSpawner
     * @param posix_entityType is the string that is after the entityType "ZombieToastSpawner_1"
     */

    public ZombieToastSpawner(int id, Position curr_position) {
        super(id, "zombie_toast_spawner", curr_position);
    }

    public void setSpawnTimer(int newSpawnTimer) {
        this.spawnTimer = newSpawnTimer;
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        spawnZombie(dungeon);
    }

    public void spawn(Dungeon dungeon) {
        this.tickCounter += 1;

        if (!canSpawn()) return;
        spawnZombie(dungeon);
    }

    // Check if 20 ticks have passed
    // canSpawn is true
    public boolean canSpawn() {
        if (tickCounter >= spawnTimer) return true;
        else return false;
    }

    // If yes, spawn and set canSpawn
    public void spawnZombie(Dungeon dungeon) {
        List<Position> spawnableTile = new ArrayList<>();

        // Check if there are empty spots adjacent; If tile not empty, move on
        for (Position newPos: this.getCurr_position().getMoveablePositions()) {
            if (dungeon.getEntities(newPos).isEmpty()) spawnableTile.add(newPos);
        }

        // No spawnable tiles
        if (spawnableTile.isEmpty()) return;

        // Random spawning
        Random rand = new Random(); 
        Position spawnPos = spawnableTile.get(rand.nextInt(spawnableTile.size()));

        Entity entity = (Entity) new ZombieToast(dungeon.uniqueEntityID(), spawnPos);
        dungeon.adjustDifficulty((MovingEntity) entity);
        EntityResponse er = new EntityResponse(Integer.toString(entity.getId()), entity.getEntityType(), entity.getCurr_position(), entity.IsInteractable());
        //update dungeon
        dungeon.addEntity(entity);
        dungeon.addEntitiesResponse(er);

        // Reset timer
        this.tickCounter = 0;
    }

    // if interact, destory
    // checkifAdjacent(Position, Position); Should be adjacent when method is called
    public void destroySpawner(Dungeon dungeon) throws InvalidActionException {
        // Player and Spawner
        Player player_entity = (Player) dungeon.getEntity("player");
        Inventory player_bag = player_entity.getInventory();
        
        // if player has weapon, destory spawner
        if (
            player_bag.getStaticEntities().stream().filter(e -> e instanceof Sword).collect(Collectors.toList()).size() > 0 ||
            player_bag.getStaticEntities().stream().filter(e -> e instanceof Bow).collect(Collectors.toList()).size() > 0
        ) {
            Entity spawner = dungeon.findEntity(this.getId());
            dungeon.deleteEntityResponse(spawner);
            dungeon.deleteEntity(spawner);
        }
        else {
            throw new InvalidActionException("No weapons to destroy spawner.");
        }
        return;
    }

    public int getTickCounter() {
        return this.tickCounter;
    }

    public void setTickCounter(int tickCounter) {
        this.tickCounter = tickCounter;
    }

    @Override
    public void doPlayerInteract(Player player, Dungeon dungeon) {
        destroySpawner(dungeon);
    }
}