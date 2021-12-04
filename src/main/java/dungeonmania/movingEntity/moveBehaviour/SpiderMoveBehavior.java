package dungeonmania.movingEntity.moveBehaviour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Spider;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.staticEntity.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderMoveBehavior extends MovingBehavior{
    // private Spider spider;
    private List<Position> path;
    private int pathIndex;
    private Boolean clockwise;
    private Position spawnPosition;

    public SpiderMoveBehavior(Dungeon dungeon, MovingEntity movingEntity, Position currPosition) {
        super(dungeon, movingEntity, currPosition);
        this.spawnPosition = currPosition;
        this.pathIndex = -1;
        this.clockwise = true;

        setPath();
    }

    @Override
    public void makeMove() {
        Position newPosition = getNextPosition();
        int newIndex = getNextPathIndex();
        
        // if next move is a boulder, change direction and check new next move
        // if another boulder, make no movement
        if (dungeon.getEntities(newPosition).stream().anyMatch(Boulder.class::isInstance)) {
            changeClockwise();
            newPosition = getNextPosition();
                changeClockwise();
                if (dungeon.getEntities(newPosition).stream().anyMatch(Boulder.class::isInstance)) {
                newPosition = movingEntity.getCurr_position();
                newIndex = this.pathIndex;
            }
        }

        setPathIndex(newIndex);
        //if it's wall, it can move

        //check if the curr Position is a swamp tile
        SwampTile swamp = dungeon.getSwamp(getCurrPosition());
        if(swamp != null) {
            //do not update if the movement factor still presence
            if(swamp.hasMovementFactorLeft(movingEntity)) {
                return;
            }
        }

        setPrevPosition(getCurrPosition());
        setCurrPosition(newPosition);
        
        interactEachEntities(newPosition);
    }

    private void setPath() {
        this.path = new ArrayList<Position>(Arrays.asList(this.spawnPosition.translateBy(Direction.UP), this.spawnPosition.translateBy(1, -1),
        this.spawnPosition.translateBy(Direction.RIGHT), this.spawnPosition.translateBy(1, 1), this.spawnPosition.translateBy(Direction.DOWN),
        this.spawnPosition.translateBy(-1, 1), this.spawnPosition.translateBy(Direction.LEFT), this.spawnPosition.translateBy(-1, -1)));
    }

    public List<Position> getPath() {
        return this.path;
    }

    public int getNextPathIndex() {
        if (this.pathIndex == -1) {
            return 0;
        }
        if (this.clockwise) {
            if (this.pathIndex == 7) {
                return 0;
            }
            return this.pathIndex + 1;
        }

        if (this.pathIndex == 0) {
            return 7;
        }
        return this.pathIndex - 1;
    }
    
    private Position getPathPosition(int i) {
        return this.path.get(i);
    }

    public Boolean isClockwise() {
        return this.clockwise;
    }

    public void setClockwise(Boolean direction) {
        this.clockwise = direction;
    }

    public void changeClockwise() {
        this.clockwise = !isClockwise();
    }

    public Position getSpawnPosition() {
        return this.spawnPosition;
    }

    private Position getNextPosition() {
        return getPathPosition(getNextPathIndex());
    }

    public int getPathIndex() {
        return this.pathIndex;
    }

    public void setPathIndex(int index) {
        this.pathIndex = index;
    }

    public void setSpawnPosition(Position spawnPosition) {
        this.spawnPosition = spawnPosition;
        setPath();
    }
}
