package dungeonmania.movingEntity.moveBehaviour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.staticEntity.Boulder;
import dungeonmania.staticEntity.Door;
import dungeonmania.staticEntity.Wall;
import dungeonmania.staticEntity.ZombieToastSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RandomMoveBehavior extends MovingBehavior{
    private Random random;

    public RandomMoveBehavior(Dungeon dungeon, MovingEntity movingEntity, Position currPosition) {
        super(dungeon, movingEntity, currPosition);
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void makeMove() {
        this.random = new Random(System.currentTimeMillis());
        
        List<Position> validMoves = getValidMoves(dungeon);
        Position newPosition;

        if (!validMoves.isEmpty()) {
            newPosition = validMoves.get(random.nextInt(validMoves.size()));
        } else {
            newPosition = movingEntity.getCurr_position();
        }
        moveToTile(newPosition, dungeon, false);
        interactEachEntities(newPosition);
    }

    public List<Position> getValidMoves(Dungeon d) {
        List<Position> validMoves = new ArrayList<Position>();
        List<Entity> entities = new ArrayList<Entity>();

        // check direction UP
        entities.addAll(d.getEntities(movingEntity.getCurr_position().translateBy(Direction.UP)));

        for (Direction dir : new ArrayList<Direction>(Arrays.asList(
                Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT))) {
            entities.clear();
            entities.addAll(d.getEntities(movingEntity.getCurr_position().translateBy(dir)));
            Boolean validDirection = true;

            // check for any blocking entities
            for (Entity e : entities) {
                if (e instanceof Wall || e instanceof Boulder || e instanceof ZombieToastSpawner) {
                    validDirection = false;
                } else if (e instanceof Door) {
                    Door door = (Door) e;
                    if (!door.getUnlocked()) {
                        validDirection = false;
                    } 
                }
            }
            if (validDirection) {
                validMoves.add(movingEntity.getCurr_position().translateBy(dir));
            } else {
                validDirection = true;
            }
        }
        return validMoves;
    }
}
