package dungeonmania.goal;

import dungeonmania.Dungeon;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.staticEntity.FloorSwitch;

public class GoalBoulders implements Goal {
    private Dungeon d;
    private Boolean complete;
    private int numSwitchesNotTriggered;
    private int numSwitchesTriggered;
    private int numBouldersAvailable;
    private int numBouldersRemaining;

    public GoalBoulders(Dungeon d) {
        this.d = d;
        this.numBouldersAvailable = d.getEntitiesByClass(Boulder.class).size();
        updateProgress();
    }

    private void updateProgress() {
        this.numSwitchesNotTriggered = 0;
        this.numSwitchesTriggered = 0;
        for (FloorSwitch fs : d.getEntitiesByClass(FloorSwitch.class)) {
            if (fs.getTriggered()) {
                this.numSwitchesTriggered++;
            } else {
                this.numSwitchesNotTriggered++;
            }
        }
        this.numBouldersRemaining = this.numBouldersAvailable - this.numSwitchesTriggered;

        if (this.numSwitchesNotTriggered == 0) {
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
        return ":boulders(" + this.numBouldersRemaining + ")/:switch(" + this.numSwitchesNotTriggered + ")";
    }
    
}
