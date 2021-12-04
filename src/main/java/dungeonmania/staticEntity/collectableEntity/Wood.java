package dungeonmania.staticEntity.collectableEntity;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class Wood extends CollectableEntity {
    /**
     * Constructor for Wood
     * @param posix_entityType is the string that is after the entityType "Wood_1"
     */

    public Wood(int id, Position curr_position) {
        super(id, "wood", curr_position);
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
