package dungeonmania.staticEntity;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Assassin;
import dungeonmania.movingEntity.Hydra;
import dungeonmania.movingEntity.Mercenary;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Spider;
import dungeonmania.movingEntity.movingEntityStates.EnemyState;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Spawner {
    private int spiderSpawn = 0;
    private int maxSpiderSpawn = 4;
    private int mercenarySpawn = 0;
    private int hydraSpawn = 0;
    private int spiderTickCounter = 10;
    private int mercenaryTickCounter = 30;
    private int hydraTickCounter = 50;
    private Random random = new Random(System.currentTimeMillis());

    public void spawn(Dungeon dungeon) {
        spiderSpawn += 1;
        mercenarySpawn += 1;
        hydraSpawn += 1;

        // Spawn zombies from zombie toast spawner 
        List<Entity> allSpawners = dungeon.getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).collect(Collectors.toList());
        for (Entity spawning: allSpawners) {
            ((ZombieToastSpawner) spawning).spawn(dungeon);
        }

        if (spiderSpawn > spiderTickCounter) spawnSpider(dungeon);
        if (mercenarySpawn > mercenaryTickCounter) spawnMercenary(dungeon);
        if (hydraSpawn > hydraTickCounter && dungeon.getGameMode().equals("Hard")) spawnHydra(dungeon);
    }

    // If yes, spawn and set canSpawn
    public void spawnSpider(Dungeon dungeon) {
        int spiderNo = dungeon.getEntities().stream().filter(e -> e instanceof Spider).collect(Collectors.toList()).size();
        // Reached max spawn capacity
        if (spiderNo > this.maxSpiderSpawn) return;

        // Random spawning
        Position spawnPos = null;
        while (true) {
            Random rand = new Random(); 
            int spawnPosX = rand.nextInt(10);
            int spawnPosY = rand.nextInt(10);
            spawnPos = new Position(spawnPosX, spawnPosY);

            // Cannot spawn on moving entities
            boolean blocked = dungeon.getEntities(spawnPos).stream().filter(e -> e instanceof MovingEntity).collect(Collectors.toList()).size() > 0;
            if (!blocked) break;
        }
        
        Entity entity = (Entity) new Spider(dungeon.uniqueEntityID(), spawnPos);
        dungeon.adjustDifficulty((MovingEntity) entity);
        EntityResponse er = new EntityResponse(Integer.toString(entity.getId()), entity.getEntityType(), entity.getCurr_position(), entity.IsInteractable());
        //update dungeon
        dungeon.addEntity(entity);
        dungeon.addEntitiesResponse(er);

        // Reset timer
        this.spiderSpawn = 0;
    }

    public void spawnHydra(Dungeon dungeon) {
        // Random spawning
        Position spawnPos = null;
        while (true) {
            Random rand = new Random(); 
            int spawnPosX = rand.nextInt(dungeon.getWidth());
            int spawnPosY = rand.nextInt(dungeon.getHeight());
            spawnPos = new Position(spawnPosX, spawnPosY);

            // Cannot only spawn on empty ground squares
            boolean emptyGround = dungeon.getEntities(spawnPos).stream().filter(e -> e instanceof Entity).collect(Collectors.toList()).size() == 0;
            if (emptyGround) break;
        }
        
        Entity entity = (Entity) new Hydra(dungeon.uniqueEntityID(), spawnPos);
        EntityResponse er = new EntityResponse(Integer.toString(entity.getId()), entity.getEntityType(), entity.getCurr_position(), entity.IsInteractable());
        //update dungeon
        dungeon.addEntity(entity);
        dungeon.addEntitiesResponse(er);

        // Reset timer
        this.hydraSpawn = 0;
    }

    public void spawnMercenary (Dungeon dungeon) {
        // Position spawnPos = new Position(1,1);
        Position spawnPos = dungeon.getPlayerEntryPos();

        // Must be an empty tile to spawn
        boolean spawnable = dungeon.getEntities(spawnPos).isEmpty();
        // Must be at least 1 enemy to spawn
        if (!spawnable || !minimalEnemy(dungeon)) return;

        Entity entity;
        // less than 30% of the time, an asssassin will spawn instead
        if (random.nextInt(100) < 30) {
            entity = (Entity) new Assassin(dungeon.uniqueEntityID(), spawnPos);
        } else {
            entity = (Entity) new Mercenary(dungeon.uniqueEntityID(), spawnPos);
        }
        dungeon.adjustDifficulty((MovingEntity) entity);
        EntityResponse er = new EntityResponse(Integer.toString(entity.getId()), entity.getEntityType(), entity.getCurr_position(), entity.IsInteractable());
        //update dungeon
        dungeon.addEntity(entity);
        dungeon.addEntitiesResponse(er);
    
        // Reset timer
        this.mercenarySpawn = 0;
    }

    public boolean minimalEnemy(Dungeon dungeon) {
        List<Entity> enemyList = dungeon.getEntities().stream().filter(e -> e instanceof MovingEntity).collect(Collectors.toList());
        for (Entity isEnemy: enemyList) {
            if (((MovingEntity) isEnemy).getState() instanceof EnemyState) {
                return true;
            }
        }
        return false;
    }

    public int getSpiderSpawn() {
        return this.spiderSpawn;
    }

    public int getMercenarySpawn() {
        return this.mercenarySpawn;
    }

    public void setSpiderSpawn(int spiderSpawn) {
        this.spiderSpawn = spiderSpawn;
    }

    public void setMercenarySpawn(int mercenarySpawn) {
        this.mercenarySpawn = mercenarySpawn;
    }
}
