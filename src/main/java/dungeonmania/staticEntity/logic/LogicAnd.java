package dungeonmania.staticEntity.logic;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.staticEntity.FloorSwitch;

public class LogicAnd implements Logic {
    /* 
    the entity will be only activated if there are 2 or more adjacent activated switches 
    (switches with boulders on them). If there are more than two switches adjacent, 
    all must be activated.
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
            adjacentSwitches.size() > 1 &&
            adjacentSwitches.stream().filter(e -> e.getLogic() instanceof LogicNot).findAny().orElse(null) != null
        ) {
            return false;
        }

        // Must have at least 2 adjacent switches
        if (adjacentSwitches.size() < 2) return false;
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
        return "and";
    }
}
