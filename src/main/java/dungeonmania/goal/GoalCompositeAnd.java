package dungeonmania.goal;

import dungeonmania.Dungeon;

public class GoalCompositeAnd implements Goal {
    Dungeon d;
    private Goal goalLeft;
    private Goal goalRight;

    public GoalCompositeAnd(Dungeon d, Goal goalLeft, Goal goalRight) {
        this.d = d;
        this.goalLeft = goalLeft;
        this.goalRight = goalRight;
    }

    @Override
    public Boolean checkComplete() {
        Boolean completeLeft = goalLeft.checkComplete();
        Boolean completeRight = goalRight.checkComplete();
        if (completeLeft && completeRight) {
            return true;
        }
        return false;
    }
    @Override
    public String constructGoalString() {
        return goalLeft.constructGoalString() + " AND " + goalRight.constructGoalString();
    }
    
}
