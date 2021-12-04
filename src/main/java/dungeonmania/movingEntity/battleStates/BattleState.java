package dungeonmania.movingEntity.battleStates;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.BattleBehavior;
import dungeonmania.movingEntity.MovingEntity;

public abstract class BattleState {
    private boolean disabledInvincibility = false;
    protected MovingEntity thisMovingEntity;
    protected BattleBehavior battleBehavior;
    protected Dungeon dungeon;
    protected int duration;

    public BattleState(MovingEntity thisMovingEntity) {
        this.thisMovingEntity = thisMovingEntity;
    }

    public void doBattle(MovingEntity target){
    };

    //dungeon needs to be added before updating the result of the battle
    public void addDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }  

    public void updatePotionDuration() {
        duration -= 1;
        //if the duration is less than 1, change the state
        if(duration <= 1) {
            setToNormalState();
        }
    }

    public void setToNormalState() {
        thisMovingEntity.setCurrBattleState(this.thisMovingEntity.getNormalState());
    }

    public void addBattleBehavior(BattleBehavior battleBehavior){
        this.battleBehavior = battleBehavior;
    }

    public void disable() {
        disabledInvincibility = true;
    }

    public boolean isDisabledInvincibility() {
        return disabledInvincibility;
    }
}
