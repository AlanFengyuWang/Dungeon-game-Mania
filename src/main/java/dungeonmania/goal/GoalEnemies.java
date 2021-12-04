package dungeonmania.goal;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.movingEntityStates.EnemyState;

public class GoalEnemies implements Goal {
    private Dungeon d;
    private Boolean complete;
    private int numEnemiesRemaining;

    public GoalEnemies(Dungeon d) {
        this.d = d;
        this.numEnemiesRemaining = 0;
        updateProgress();
    }

    private void updateProgress() {
        List<MovingEntity> movingEntities = d.getEntitiesByClass(MovingEntity.class);
        this.numEnemiesRemaining = 0;
        for (MovingEntity me : movingEntities) {
            if (me.getState() instanceof EnemyState) {
                this.numEnemiesRemaining++;
            }
        }

        if (numEnemiesRemaining > 0) this.complete = false;
        else this.complete = true;
    }

    @Override
    public Boolean checkComplete() {
        updateProgress();
        return this.complete;
    }

    @Override
    public String constructGoalString() {
        return ":enemies(" + this.numEnemiesRemaining + ")";
    }
    
}
