package dungeonmania.staticEntity.logic;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.staticEntity.FloorSwitch;

public class LogicCoAnd implements Logic {
    /*
    the entity will only be activated if there are 2 or more 
    activated switches adjacent, which are both activated on the same tick 
    (i.e. a boulder is pushed onto them at the same time).
    */
    @Override
    public boolean logicActivate(Dungeon dungeon, Entity center) {
        List<FloorSwitch> adjacentSwitches = WireSwitchChecker.getAdjacentSwitches(dungeon, center);
        // Exclude itself
        if (center instanceof FloorSwitch && adjacentSwitches.contains((FloorSwitch) center)) {
            adjacentSwitches.remove((FloorSwitch) center);
        }
        // Must have all activated switches
        for (FloorSwitch switches: adjacentSwitches) {
            // If there triggered switches, wire will be triggered as well
            // Check if there is at least 1 unactivated switch
            if (!switches.getTriggered()) return false;
        }
        // All switches were activated
        return true;
    }

    @Override
    public String getLogicString() {
        return "co_and";
    }
}
