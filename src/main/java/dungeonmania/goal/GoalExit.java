package dungeonmania.goal;

import dungeonmania.Dungeon;
import dungeonmania.staticEntity.Exit;
import dungeonmania.util.Position;

public class GoalExit implements Goal {
    private Dungeon d;
    private Boolean complete;
    private Position exitLocation;

    public GoalExit(Dungeon d) {
        this.d = d;
        this.complete = false;
        if (!d.getEntitiesByClass(Exit.class).isEmpty()) {
            this.exitLocation = d.getEntitiesByClass(Exit.class).get(0).getCurr_position();
        } else {
            this.exitLocation = null; // TODO or maybe just throw exception?
        }
    }

    private void updateProgress() {
        Position playerLocation = d.getPlayer().getCurr_position();
        if (playerLocation.equals(this.exitLocation)) {
            this.complete = true;
        } else {
            this.complete = false;
        }
    }

    @Override
    public Boolean checkComplete() {
        updateProgress();
        return this.complete;
    }

    @Override
    public String constructGoalString() {
        return ":exit";
    }
    
}
