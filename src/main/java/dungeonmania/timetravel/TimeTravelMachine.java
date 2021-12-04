package dungeonmania.timetravel;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.Dungeon;

public class TimeTravelMachine implements TimeTravelAbility{
    private HashMap<Integer, Dungeon> dungeonsTimeMaps = new HashMap<>();

    @Override
    public Dungeon doTimeTravel() {
        //no implemnting
        return null;
    }

    public void saveToTimeMachine(int when, Dungeon dungeon) {
        dungeonsTimeMaps.put(when, dungeon);
    }

    public Dungeon travelBack(int when) {
        return dungeonsTimeMaps.get(when);
    }

    public int getWhen(Dungeon dungeon) {
        return dungeonsTimeMaps
        .entrySet()
        .stream()
        .filter(d -> d.getValue() != null && d.getValue().equals(dungeon))
        .map(Map.Entry::getKey)
        .findFirst().orElse(null);
    }
    
}
