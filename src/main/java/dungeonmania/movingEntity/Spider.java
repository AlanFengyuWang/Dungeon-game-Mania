package dungeonmania.movingEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.moveBehaviour.MovingBehavior;
import dungeonmania.movingEntity.moveBehaviour.SpiderMoveBehavior;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {

    public Spider(int id, Position position) {
        super(id, "spider", position, 30, 1, 20);
        // SpiderMoveBehavior spiderMovement = new SpiderMoveBehavior(super.dungeon, this);
        // setMovingBehavior((MovingBehavior) new SpiderMoveBehavior(dungeon, this));
    }

    public Spider(int id, String entityType, Position position, int health, int battleRadius, int attackDamage) {
        super(id, "spider", position, health, battleRadius, attackDamage);
    }

    @Override
    public void doMove(Dungeon dungeon) {
        // TODO Auto-generated method stub
    }

    @Override
    public void doMove(Player player, Dungeon dungeon) {
        // check if any moving entities are in the next position
        // if so and its another enemy, stay in place
        // if its a player, battle (unless player is invincible then change direction)
        if (this.movingBehavior == null) {
            setMovingBehavior((MovingBehavior) new SpiderMoveBehavior(dungeon, this, getCurr_position()));
        }

        this.movingBehavior.makeMove();
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        if(entity != null && entity instanceof Player) {
            // System.out.println("")
            attachBattleEntity((MovingEntity) entity);
            notifyObserversBattle(dungeon);
        }
    }
}
