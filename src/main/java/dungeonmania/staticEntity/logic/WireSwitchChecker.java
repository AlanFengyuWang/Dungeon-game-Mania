package dungeonmania.staticEntity.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.staticEntity.FloorSwitch;
import dungeonmania.staticEntity.Wire;
import dungeonmania.util.Position;

public class WireSwitchChecker {
    public static void triggerSwitches(Dungeon dungeon) {
        List<Entity> switches = dungeon.getEntities().stream().filter(e -> e instanceof FloorSwitch).collect(Collectors.toList());
        // First check for static triggers
        boulderSwitchChecker(dungeon, switches);
        while (true) {
            // Check for connected wires
            getAllTriggerdWires(dungeon);
            if (!logicSwitchChecker(dungeon, switches)) break;
        }
        // Now all switches have been set
        // Check the logic of other entities and trigger accordingly
        List<Entity> logicE = dungeon.getEntities().stream().filter(e -> e instanceof LogicChecker).collect(Collectors.toList());
        for (Entity e: logicE) {
            ((LogicChecker) e).logicFulfilled(dungeon);
        }
    }

    // Trigger switches with correct logic
    public static boolean logicSwitchChecker(Dungeon dungeon, List<Entity> switches) {
        boolean newTriggeredSwitch = false;
        for (Entity logicSwitch: switches) {
            if (((FloorSwitch) logicSwitch).getLogic() != null ) {
                boolean notTriggered = ((FloorSwitch) logicSwitch).getTriggered();
                // Trigger switches if logic works
                ((FloorSwitch) logicSwitch).logicFulfilled(dungeon);
                if (notTriggered != ((FloorSwitch) logicSwitch).getTriggered()) newTriggeredSwitch = true;
            }
        }
        return newTriggeredSwitch;
    }

    // Check switches with boulder on top and no logic
    public static void boulderSwitchChecker(Dungeon dungeon, List<Entity> switches) {
        for (Entity activated: switches) {
            Entity boulder = dungeon.getEntities(activated.getCurr_position()).stream().filter(e -> e instanceof Boulder).findAny().orElse(null);
            FloorSwitch triggered = (FloorSwitch) activated;
            if (triggered.getLogic() == null && boulder != null) {
                triggered.isTriggered();
            }
            else triggered.isNotTriggered();
        }
    }
    
    public static void getAllTriggerdWires(Dungeon dungeon) {
        List<Entity> switches = dungeon.getEntities().stream().filter(e -> e instanceof FloorSwitch).collect(Collectors.toList());
        List<Entity> wires = dungeon.getEntities().stream().filter(e -> e instanceof Wire).collect(Collectors.toList());
       
        // Set all wires to false to account for closing switches
        for (Entity activeWire: wires) {
            ((Wire) activeWire).setTriggered(false);
        }

        // Check all switches
        for (Entity activated: switches) {
            // if Logic is fulfilled
            if (((FloorSwitch) activated).getTriggered()) {
                for (Entity activeWire: wires) {
                    // Wires are triggered
                    if (((Wire) activeWire).connectedSwitchesList().contains((FloorSwitch) activated)) {
                        ((Wire) activeWire).setTriggered(true);
                    }
                }
            }
        }

        // Prevent edge cases from not logic
        // If there is a not switch connected to another switch, all wires are false
        for (Entity activeWire: wires) {
            List<FloorSwitch> noNotSwitch = ((Wire) activeWire).connectedSwitchesList();
            if (
                noNotSwitch.size() > 1 &&
                noNotSwitch.stream().filter(e -> e.getLogic() instanceof LogicNot).findAny().orElse(null) != null
            ) {
                ((Wire) activeWire).setTriggered(false);
            }
        }

    }

    public static List<FloorSwitch> getAdjacentSwitches(Dungeon dungeon, Entity center) {
        List<FloorSwitch> adjacentSwitches = new ArrayList<>();
        // Get adjacent squares
        List<Position> adjacentPos = center.getCurr_position().getMoveablePositions();
        for (Position pos: adjacentPos) {
            // Find if any instance of wire or switch
            Entity wire = dungeon.getEntities(pos).stream().filter(e -> e instanceof Wire).findAny().orElse(null);
            Entity floorSwitch = dungeon.getEntities(pos).stream().filter(e -> e instanceof FloorSwitch).findAny().orElse(null);
            // Can only have wire or switch
            if (wire != null) {
                // The wire should contain all connected switches
                for (FloorSwitch connectedSwitch : ((Wire) wire).connectedSwitchesList()) {
                    adjacentSwitches.add(connectedSwitch);
                }
            }
            else if (floorSwitch != null) adjacentSwitches.add((FloorSwitch) floorSwitch);
        }
        return adjacentSwitches;
    }

    // ========== Find wires and switches connection ==========
    // Link all switches to wire
    public static void activeWires(Dungeon dungeon) {
        // Find all switches
        List<Entity> switches = dungeon.getEntities().stream().filter(e -> e instanceof FloorSwitch).collect(Collectors.toList());
        for (Entity activated: switches) {
            // Find all wires linked to switch
            wireConnection(dungeon, activated);
        }
    }

    // Find all wires related to switch
    public static void wireConnection(Dungeon dungeon, Entity baseSwitch) {
        List<Wire> connectedWires = new ArrayList<>();
        Queue<Entity> q = new LinkedList<>();
        q.add(baseSwitch);
        while (!q.isEmpty()) {
            Entity center = q.remove();
            for (Entity wire: adjacentWire(dungeon, center)) {
                if (!connectedWires.contains((Wire) wire)) {
                    q.add(wire);
                    connectedWires.add((Wire) wire);
                }
            }
        }
        wireToSwitches((FloorSwitch) baseSwitch, connectedWires);
    }

    // Link wires with switches
    public static void wireToSwitches(FloorSwitch baseSwitch, List<Wire> connectedWires) {
        for (Wire wire: connectedWires) {
            wire.addConnectedSwitches(baseSwitch);
        }
    }

    // Find all adjacent wires
    public static List<Entity> adjacentWire(Dungeon dungeon, Entity center) {
        List<Entity> adjoiningWires = new ArrayList<>();
        // Get adjacent squares
        List<Position> adjacentPos = center.getCurr_position().getMoveablePositions();
        for (Position pos: adjacentPos) {
            // Find if any instance of wire
            Entity wire = dungeon.getEntities(pos).stream().filter(e -> e instanceof Wire).findAny().orElse(null);
            // Adjacent wire found
            if (wire != null) adjoiningWires.add(wire);
        }
        return adjoiningWires;
    }
    
}
    