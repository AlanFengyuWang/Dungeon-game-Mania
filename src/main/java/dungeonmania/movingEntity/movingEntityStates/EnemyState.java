package dungeonmania.movingEntity.movingEntityStates;

import dungeonmania.movingEntity.MovingEntity;

public class EnemyState implements MovingEntityState{
    /**
     * Author: Alan
     */
    MovingEntity MovEntity;
    public EnemyState(MovingEntity MovEntity) {
        this.MovEntity = MovEntity;
    }

}
