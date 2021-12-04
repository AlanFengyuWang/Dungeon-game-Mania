package dungeonmania.staticEntity.collectableEntity;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class Treasure extends CollectableEntity {
    /**
     * Constructor for Treasure
     * @param posix_entityType is the string that is after the entityType "Treasure_1"
     */

    public Treasure(int id, Position curr_position) {
        super(id, "treasure", curr_position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        return;
    }

    @Override
    public void doUse(Entity playerEntity) {
        return;
        
    }

}
