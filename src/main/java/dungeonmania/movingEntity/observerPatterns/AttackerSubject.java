package dungeonmania.movingEntity.observerPatterns;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.MovingEntity;

public interface AttackerSubject {
    //subjects for battle
    public void attachBattleEntity(MovingEntity observer);
    public void detachBattleEntity(MovingEntity observer);
    public void notifyObserversBattle(Dungeon dungeon);
}
