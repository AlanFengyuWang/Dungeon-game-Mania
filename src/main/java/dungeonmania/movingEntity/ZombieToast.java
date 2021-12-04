package dungeonmania.movingEntity;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.battleStates.InvincibleState;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {
    private Random random;
    private Boolean hasArmour;

    public ZombieToast(int id, Position curr_position) {
        super(id, "zombie_toast", curr_position, 10, 1, 10);
        this.random = new Random(System.currentTimeMillis());
        if (this.random.nextInt(100) < 15) {
            this.hasArmour = true;
        } else {
            this.hasArmour = false;
        }
        if (this.hasArmour == true) {
            this.setDefense(60);
        }
    }

    public ZombieToast(int id, String entityType, Position curr_position, int health, int battleRadius, int attackDamage) {
        super(id, "zombie_toast", curr_position, health, battleRadius, attackDamage);
        this.random = new Random(System.currentTimeMillis());
        if (this.random.nextInt(100) < 15) {
            this.hasArmour = true;
        } else {
            this.hasArmour = false;
        }
        if (this.hasArmour == true) {
            this.setDefense(60);
        }
    }

    @Override
    public void doMove(Dungeon dungeon) {
        
    }

    @Override
    public void doMove(Player player, Dungeon dungeon) {
        //if the zombieToast can see the player
        //if the zombieToast is invincible
        if(player.getCurrPlayerState() instanceof InvincibleState) {
            runAway(dungeon);
        } else {
            randomMovebehavior(dungeon);
        }
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        //equip armor whenever dointeract
        if(hasArmour) {
            this.equipArmour(dungeon);
        }
        //battle the player if it's enemy or battle the another entity if it's ally
        boolean battlePlayer = entity instanceof Player;
        boolean battleOtherEntities = entity instanceof MovingEntity && this.state.equals(allyState);

        if(entity != null && (battlePlayer || battleOtherEntities)) {
            attachBattleEntity((MovingEntity) entity);
            notifyObserversBattle(dungeon);
        }
    }

    public Boolean hasArmour() {
        return this.hasArmour;
    }

}
