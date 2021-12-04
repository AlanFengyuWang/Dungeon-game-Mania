package dungeonmania.movingEntity.observerPatterns;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.MovingEntity;

public interface DefenderObserver {
    //observers for battle
    public void battleMethology(MovingEntity target, Dungeon dungeon);

}
