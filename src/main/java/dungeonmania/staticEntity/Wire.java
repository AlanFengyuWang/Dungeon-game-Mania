package dungeonmania.staticEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class Wire extends StaticEntity {
    private boolean triggered = false;
    private List<FloorSwitch> connectedSwitches;

    public Wire(int id, Position curr_position) {
        super(id, "wire", curr_position);
        connectedSwitches = new ArrayList<>();
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // ========== Skin Management ==========
        dungeon.skinsOrdering(entity, this);

        // ========== Pre conditions ==========
        // Will have to interact with the other entity instead if more than 1 entity on same tile
        // Or already have interact hence, on same tile
        if (
            dungeon.getEntities(this.getCurr_position()).size() > 1 || 
            entity.getCurr_position() == this.getCurr_position()
        ) return;

        // ========== Movement onto tile ==========
        // All entities can tread on wire
        if (entity instanceof Player) {
            Player player_entity = (Player) entity;
            player_entity.setPrev_position(getCurr_position());
        }
        entity.setCurr_position(this.getCurr_position());
    }

    public void setTriggered(Boolean triggered) {
        this.triggered = triggered;
    }

    public boolean getTriggered() {
        return this.triggered;
    }

    public void addConnectedSwitches(FloorSwitch connectedSwitch) {
        this.connectedSwitches.add(connectedSwitch);
    }

    public List<FloorSwitch> connectedSwitchesList() {
        return this.connectedSwitches;
    }
}
