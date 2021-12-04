package dungeonmania.goal;

import dungeonmania.Dungeon;
import dungeonmania.staticEntity.collectableEntity.Treasure;

public class GoalTreasure implements Goal {
    private Dungeon d;
    private Boolean complete;
    private int numTreasureRemaining;

    public GoalTreasure(Dungeon d) {
        this.d = d;
        this.complete = false;
        this.numTreasureRemaining = this.d.getEntitiesByClass(Treasure.class).size();
    }

    private void updateProgress() {
        this.numTreasureRemaining = this.d.getEntitiesByClass(Treasure.class).size();

        if (this.numTreasureRemaining <= 0) {
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
        return ":treasure(" + this.numTreasureRemaining + ")";
    }
    
}
