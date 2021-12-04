package dungeonmania.movingEntity.battleStates;

import dungeonmania.movingEntity.MovingEntity;

public class NormalState extends BattleState{
    /**
     * Battle can happen between monsters (mercenary vs mercenary) or
     * between player and monster or
     * player and player
     * @pre 'this' and thisMovingEntity can be any moving entity
     */
    public NormalState(MovingEntity thisMovingEntity) {
        //don't do anything
        super(thisMovingEntity);
    }

    @Override
    public void doBattle(MovingEntity target) {
        if(dungeon == null) throw new IllegalAccessError("Dungeon needs to be added before access the battle");
        // System.out.println("NormalState.doBattle --> battleBehavior = " + battleBehavior);
        // System.out.println("NormalState.doBattle --> target = " + target);
        // System.out.println("NormalState.doBattle --> dungeon = " + dungeon);
        // System.out.println("NormalState.doBattle --> target = " + target + ", this = " + this);
        battleBehavior.updateBattleResult(target, dungeon);
    }
}
