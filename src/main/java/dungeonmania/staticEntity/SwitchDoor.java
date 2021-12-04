package dungeonmania.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.Spider;
import dungeonmania.staticEntity.logic.Logic;
import dungeonmania.staticEntity.logic.LogicChecker;
import dungeonmania.staticEntity.logic.LogicFactory;
import dungeonmania.util.Position;

public class SwitchDoor extends StaticEntity implements LogicChecker {
    private boolean unlocked = false;
    Logic logicState = null;

    public SwitchDoor(int id, Position curr_position) {
        super(id, "switch_door", curr_position);

        //beginning it's blocking
        currBlockingState = blockingState;
    }

    public SwitchDoor(int id, String posix_entityType, Position curr_position) {
        super(id, "switch_door_" + posix_entityType, curr_position);

        //beginning it's blocking
        currBlockingState = blockingState;
    }

    public boolean getUnlocked() {
        return this.unlocked;
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
            (dungeon.getEntities(this.getCurr_position()).size() > 1 && unlocked == true) || 
            entity.getCurr_position() == this.getCurr_position()
        ) return;

        // ========== Movement onto tile ==========
        // Spider can move on locked doors
        if (entity instanceof Spider || unlocked) {
            if (entity instanceof Player) {
                Player player_entity = (Player) entity;
                player_entity.setPrev_position(getCurr_position());
            }
            entity.setCurr_position(this.getCurr_position());
            dungeon.updateEntityResponse(entity);
        }
        // If locked, cannot move through door
        else if (!(entity instanceof Player) && unlocked == false) return;
    }

    @Override
    public void logicFulfilled(Dungeon dungeon) {
        // ========== Check Logic ==========
        if (logicState.logicActivate(dungeon, this)) {
            this.unlocked = true;
            currBlockingState = unblockingState;
            this.setEntityType("switch_door_unlocked");
        }
        else {
            this.unlocked = false;
            currBlockingState = blockingState;
            this.setEntityType("switch_door");
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