package dungeonmania.goal;

import dungeonmania.Dungeon;

public class GoalCompositeOr implements Goal {
    Dungeon d;
    private Goal goalLeft;
    private Goal goalRight;

    public GoalCompositeOr(Dungeon d, Goal goalLeft, Goal goalRight) {
        this.d = d;
        this.goalLeft = goalLeft;
        this.goalRight = goalRight;
    }

    @Override
    public Boolean checkComplete() {
        if (goalLeft.checkComplete() || goalRight.checkComplete()) {
            return true;
        }
        return false;
    }
    @Override
    public String constructGoalString() {
        return goalLeft.constructGoalString() + " OR " + goalRight.constructGoalString();
    }
}
