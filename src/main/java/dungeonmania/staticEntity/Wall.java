package dungeonmania.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;
import dungeonmania.entity.*;

public class Wall extends StaticEntity {
    
    /**
     * Constructor for Wall
     * @param posix_entityType is the string that is after the entityType "wall_1"
     */

    public Wall(int id, Position curr_position) {
        super(id, "wall", curr_position);

        //change the state to blocking state
        currBlockingState = blockingState;
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        //if the entity hit the wall it will not move
        return;
    }
}