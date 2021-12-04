package dungeonmania.staticEntity.collectableEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.util.Position;

public class SunStone extends CollectableEntity {

    public SunStone(int id, Position curr_position) {
        super(id, "sun_stone", curr_position);
    }

    @Override
    public void doUse(Entity playerEntity) {
        return;
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        return;
    }
    
}
