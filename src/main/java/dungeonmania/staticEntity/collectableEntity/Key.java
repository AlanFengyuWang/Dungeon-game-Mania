package dungeonmania.staticEntity.collectableEntity;
import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class Key extends CollectableEntity {
    // corresponding key to door
    int keyNo = -1;
    
    /**
     * Constructor for Key
     * @param posix_entityType is the string that is after the entityType "Key_1"
     */

    public Key(int id, Position curr_position) {
        super(id, "key", curr_position);
    }

    public int getKeyNo() {
        return this.keyNo;
    }

    public void setKeyNo(int keyNo) {
        this.keyNo = keyNo;
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
