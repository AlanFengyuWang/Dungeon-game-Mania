package dungeonmania.movingEntity;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.battleStates.InvincibleState;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity {
    Random random;

    public Hydra(int id, Position curr_position) {
        super(id, "hydra", curr_position, 20, 1, 10);
    }

    public Hydra(int id, String entityType, Position curr_position, int health, int battleRadius, int attackDamage) {
        super(id, "hydra", curr_position, health, battleRadius, attackDamage);
    }

    @Override
    public void doMove(Dungeon dungeon) {
        
    }

    @Override
    public void doMove(Player player, Dungeon dungeon) {
        if(player.getCurrPlayerState() instanceof InvincibleState) {
            runAway(dungeon);
        } else {
            randomMovebehavior(dungeon);
        }
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        //battle the player if it's enemy or battle the another entity if it's ally
        boolean battlePlayer = entity instanceof Player;
        boolean battleOtherEntities = entity instanceof MovingEntity && this.state.equals(allyState);

        if(entity != null && (battlePlayer || battleOtherEntities)) {
            attachBattleEntity((MovingEntity) entity);
            notifyObserversBattle(dungeon);
        }
    }
}
