package dungeonmania.movingEntity.battleStates;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;

public class InvincibleState extends BattleState{
    /**
     * Battle can happen between two players (Old self vs new self) or 
     * happen between player and a monster 
     * @pre 'this' is always Player
     * @author Alan
     */

    public InvincibleState(MovingEntity thisMovingEntity, int duration) {
        super(thisMovingEntity);
        super.duration = duration;
    }

    @Override
    public void doBattle(MovingEntity target) {
        if(dungeon == null) throw new IllegalAccessError("Dungeon needs to be added before access the battle");
        //no battle occurs
        //when the duration is going to be zero after this usage, it will change the state of thisMovingEntity
        System.out.println("InvincibleState.doBattle --> duration = " + duration + ", isDisabledInvincibility() = " + isDisabledInvincibility());
        if(duration <= 0) {
            setToNormalState();
        }
        //delete the target
        if(!isDisabledInvincibility()) {
            if(target instanceof Player) {
                dungeon.deleteEntity(thisMovingEntity);
            }
            else {
                dungeon.deleteEntity(target);
            }
        } 
    }
}
