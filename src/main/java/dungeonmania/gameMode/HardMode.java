package dungeonmania.gameMode;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.ZombieToastSpawner;

public class HardMode implements GameMode {
    Dungeon dungeon;

    public HardMode(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    public void setEntityStats() {
        for (Entity entity : dungeon.getEntities()) {
            if (entity instanceof MovingEntity) {
                if (entity instanceof Player) {
                    Player player =  (Player) entity;
                    player.setHealth(70);
                    player.getCurrBattleState().disable();       //here it disabled the invincibility skill
                }
            } else if (entity instanceof StaticEntity) {
                if (entity instanceof ZombieToastSpawner) {
                    ZombieToastSpawner zombie_spawner = (ZombieToastSpawner) entity;
                    zombie_spawner.setSpawnTimer(15);
                }
            } 
        }
        
    }

}
