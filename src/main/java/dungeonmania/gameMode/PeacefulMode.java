package dungeonmania.gameMode;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;

public class PeacefulMode implements GameMode {
    Dungeon dungeon;

    public PeacefulMode(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    public void setEntityStats() {
        for (Entity entity : dungeon.getEntities()) {
            if (entity instanceof MovingEntity) {
                if (!(entity instanceof Player)) {
                    MovingEntity enemy =  (MovingEntity) entity;
                    enemy.setAttackDamage(0);
                }
            }
        }
    }
}
