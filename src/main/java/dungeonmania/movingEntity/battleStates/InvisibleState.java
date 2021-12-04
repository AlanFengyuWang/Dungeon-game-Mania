package dungeonmania.movingEntity.battleStates;
import dungeonmania.Dungeon;
import dungeonmania.movingEntity.MovingEntity;

public class InvisibleState extends BattleState{
    /**
     * Battle cannot happen between player and monster
     * @pre 'this' is always Player
     */
    private Dungeon dungeon;
    private int duration;

    public InvisibleState(MovingEntity thisMovingEntity, int duration) {
        super(thisMovingEntity);
        super.duration = duration;
    }

    @Override
    public void doBattle(MovingEntity target) {
        if(dungeon == null) throw new IllegalAccessError("Dungeon needs to be added before access the battle");
        //no battle happens
        //when the duration is going to be zero after this usage, it will change the state of thisMovingEntity
        if(duration <= 1) {
            setToNormalState();
        }
        updatePotionDuration();
    }
}
