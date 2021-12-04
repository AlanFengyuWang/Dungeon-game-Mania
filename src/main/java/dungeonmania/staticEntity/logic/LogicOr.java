package dungeonmania.staticEntity.logic;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.staticEntity.FloorSwitch;

public class LogicOr implements Logic {
    /*
    the entity will be activated if there are 1 or more adjacent activated switches
    */
    @Override
    public boolean logicActivate(Dungeon dungeon, Entity center) {
        List<FloorSwitch> adjacentSwitches = WireSwitchChecker.getAdjacentSwitches(dungeon, center);
        // Exclude itself
        if (center instanceof FloorSwitch && adjacentSwitches.contains((FloorSwitch) center)) {
            adjacentSwitches.remove((FloorSwitch) center);
        }
        // Prevent edge cases from not logic
        // If there is a not switch connected to another switch, all wires are false
        if (
            adjacentSwitches.stream().filter(e -> e.getLogic() instanceof LogicNot).findAny().orElse(null) != null
        ) {
            return false;
        }
        
        // Must have 1 or more adjacent activated switches
        for (FloorSwitch switches: adjacentSwitches) {
            // If there triggered switches, wire will be triggered as well
            // Check if there is at least 1 activated switch
            if (switches.getTriggered()) return true;
        }
        // No switches were activated
        return false;
    }

    @Override
    public String getLogicString() {
        return "or";
    }
}
