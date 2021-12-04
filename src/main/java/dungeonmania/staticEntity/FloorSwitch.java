package dungeonmania.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.logic.Logic;
import dungeonmania.staticEntity.logic.LogicChecker;
import dungeonmania.staticEntity.logic.LogicFactory;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements LogicChecker {
    boolean triggered = false;
    Logic logicState = null;

    /**
     * Constructor for Wall
     * @param posix_entityType is the string that is after the entityType "FloorSwitch_1"
     */

    public FloorSwitch(int id, Position curr_position) {
        super(id, "switch", curr_position);
    }

    public FloorSwitch(int id, String posix_entityType, Position curr_position) {
        super(id, "switch_" + posix_entityType, curr_position);
    }

    public boolean getTriggered() {
        return triggered;
    }

    public void isTriggered() {
        this.triggered = true;
    }

    public void isNotTriggered() {
        this.triggered = false;
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // ========== Skin Management ==========
        if (entity instanceof MovingEntity || entity instanceof Boulder) {
            // For skins
            // To make Moving entites + Boulder skin show over door skin
            dungeon.skinsOrdering(entity, this);
        }

        // ========== Pre conditions ==========
        // Will have to interact with the other entity instead if more than 1 entity on same tile
        // Or already have interact hence, on same tile
        if (
            dungeon.getEntities(this.getCurr_position()).size() > 1 || 
            entity.getCurr_position() == this.getCurr_position()
        ) return;

        // ========== Movement onto tile ==========
        // All entities can tread on floor switch
        if (entity instanceof Player) {
            Player player_entity = (Player) entity;
            player_entity.setPrev_position(getCurr_position());
        }
        entity.setCurr_position(this.getCurr_position());

        return;
    }

    @Override
    public void logicFulfilled(Dungeon dungeon) {
        // Does not have logic, function like normal
        if (logicState == null) {
            // ========== Boulder on Switch ==========
            // Check if boulders is on switch
            boolean onSwitch = dungeon.getEntities(this.getCurr_position()).stream().anyMatch(e -> e instanceof Boulder == true);
            this.triggered = onSwitch;
            return;
        }

        // Logic takes priority over boulder on top of switches
        // If logic is invalid, trigger is off regardless of boulder
        if (logicState.logicActivate(dungeon, this)) {
            this.triggered = true;
        }
        else this.triggered = false;
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
