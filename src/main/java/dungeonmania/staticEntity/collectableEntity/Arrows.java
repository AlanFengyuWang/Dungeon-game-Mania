package dungeonmania.staticEntity.collectableEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class Arrows extends CollectableEntity{
    private int damage = 20;
    public Arrows(int id, Position curr_position) {
        super(id, "arrow", curr_position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        
    }
    /**
     * Constructor for Arrows
     * @param posix_entityType is the string that is after the entityType "Arrows_1"
     */

    @Override
    public void doUse(Entity playerEntity) {
        return;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
   
}
