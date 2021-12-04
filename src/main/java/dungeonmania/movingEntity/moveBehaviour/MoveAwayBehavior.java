package dungeonmania.movingEntity.moveBehaviour;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.util.Position;

public class MoveAwayBehavior extends MovingBehavior {
    public MoveAwayBehavior(Dungeon dungeon, MovingEntity movingEntity, Position currPosition) {
        super(dungeon, movingEntity, currPosition);
    }

    @Override
    public void makeMove() {
        Position player_prev_position = player.getPrev_position();
        Position player_curr_position = player.getCurr_position();

        //when player moves to a direction, it will move to the same direction if there's no barrier
        Position positionDiff = Position.getDirection(player_prev_position, player_curr_position);
        Position whereTo = Position.calculatePositionAdd(positionDiff, getCurrPosition());

        Position newPosition = getCurrPosition();
        // if(dungeon.isUnblock(whereTo)) {
        //     //update position
        //     newPosition = whereTo;
        // }
        // //when there's a barrier, it moves to other directions
        // else {
        //     List<Position> adjacentMoveablePositions = getCurrPosition().getMoveablePositions();
        //     //explore positions
        //     for(Position explorePosition : adjacentMoveablePositions) {
        //         if(dungeon.isUnblock(whereTo) && noTowardPlayer(explorePosition, getCurrPosition(), player_curr_position)) {
        //             //update
        //             newPosition = explorePosition;
        //             break;
        //         }
        //     }
        //     //if the mercenary is trapped, nothing happened
        // }

        List<Position> adjacentMoveablePositions = getCurrPosition().getMoveablePositions();
        //explore positions
        for(Position explorePosition : adjacentMoveablePositions) {
            if(dungeon.isUnblock(whereTo) && noTowardPlayer(explorePosition, getCurrPosition(), player_curr_position)) {
                //update
                newPosition = explorePosition;
                break;
            }
        }

        //update the position
        updatePosition(newPosition, dungeon, false);
    }

    public boolean noTowardPlayer(Position nextPosition, Position currPosition, Position playerPosition) {
        int distanceToPlayerNow = Position.calculateDistanceBetween(currPosition, playerPosition);
        int distanceToPlayerNext = Position.calculateDistanceBetween(nextPosition, playerPosition);
        if(distanceToPlayerNext > distanceToPlayerNow) return true;
        return false;
    }
}
