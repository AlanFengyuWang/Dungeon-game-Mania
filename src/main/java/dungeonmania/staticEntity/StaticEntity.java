package dungeonmania.staticEntity;
import dungeonmania.entity.*;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {
    /**
     * 
     * Constructor for StaticEntity
     */
    public StaticEntity(int id, String entityType) {
        super(id, entityType);
    }

    public StaticEntity(int id, String entityType, Position curr_position) {
        super(id, entityType, curr_position);
    }
}
