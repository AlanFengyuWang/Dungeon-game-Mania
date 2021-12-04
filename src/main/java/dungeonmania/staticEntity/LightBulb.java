package dungeonmania.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.staticEntity.logic.Logic;
import dungeonmania.staticEntity.logic.LogicChecker;
import dungeonmania.staticEntity.logic.LogicFactory;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity implements LogicChecker {
    Logic logicState = null;
    boolean triggered = false;

    public LightBulb(int id, Position curr_position) {
        super(id, "light_bulb_off", curr_position);

        //change the state to blocking state
        currBlockingState = blockingState;
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // Cannot go through a light bulb
        return; 
    }

    @Override
    public void logicFulfilled(Dungeon dungeon) {
        // Ignore if normal condition
        if (logicState == null) return;

        if (logicState.logicActivate(dungeon, this)) {
            this.triggered = true;
            this.setEntityType("light_bulb_on");
        }
        else {
            this.triggered = false;
            this.setEntityType("light_bulb_off");
        }
        dungeon.updateEntityResponse(this);
    }

    @Override
    public Logic getLogic() {
        return this.logicState;
    }

    @Override
    public void setLogic(String logic) {
        this.logicState = LogicFactory.createLogic(logic);
    }
}
