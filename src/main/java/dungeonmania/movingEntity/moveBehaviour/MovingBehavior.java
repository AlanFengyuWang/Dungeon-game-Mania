package dungeonmania.movingEntity.moveBehaviour;

import java.util.HashMap;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.entity.blockingStates.BlockState;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.SwampTile;
import dungeonmania.staticEntity.Wall;
import dungeonmania.util.Position;

public abstract class MovingBehavior {
    protected Player player;
    protected MovingEntity movingEntity;
    protected Position currPosition;
    protected Position prevPosition;
    protected Dungeon dungeon;

    public MovingBehavior(Dungeon dungeon, MovingEntity movingEntity, Position currPosition) {
        this.movingEntity = movingEntity;
        this.currPosition = currPosition;
        this.prevPosition = currPosition;
        this.dungeon = dungeon;
        if(movingEntity instanceof Player) {
            player = (Player) movingEntity;
        }
        else {
            setPlayer();
        }
    }

    public abstract void makeMove();

    //interact with each entities
    public void interactEachEntities(Position newPosition) {
        List <Entity> entities_ecounter = dungeon.getEntities(newPosition);
        for(Entity e : entities_ecounter) {
            //pass to the next position (For the boulder and the portal the changing position logic is implemented in their sepeated classess)
            // if(e instanceof MovingEntity) {
            //     MovingEntity mv = (MovingEntity) e;
            //     mv.setNextPosition(newPosition);
                // mv.setCurr_position(getCurrPosition());
                // mv.setPrev_position(getPrevPosition());
            // }
            e.doInteract(movingEntity, dungeon);
        }
    }

    //interact with each entities
    public void interactEachEntities(Position newPosition, Entity entity) {
        entity.doInteract(movingEntity, dungeon);
    }

    public void updatePosition(Position newPosition, Dungeon dungeon, boolean isSpider) {
        //check if the curr Position is a swamp tile
        SwampTile swamp = dungeon.getSwamp(getCurrPosition());
        if(swamp != null) {
            //do not update if the movement factor still presence
            if(swamp.hasMovementFactorLeft(movingEntity)) {
                return;
            }
        }
        //if it's blockable, it will not move
        Entity entity = dungeon.getEntity(newPosition);
        if(entity != null && entity.getcurrBlockingState() instanceof BlockState) {
            // if(entity.getEntityType().contains("bomb")) {
            //     //do nothing
            // }

            //if it's spider, it can move across the wall
            if(entity instanceof Wall && isSpider) {
                setPrevPosition(currPosition);
                setCurrPosition(newPosition);
            }
        }
        //if it's unblockable, it moves
        else {
            setPrevPosition(currPosition);
            setCurrPosition(newPosition);
        }
    }

    public void moveToTile(Position newPosition, Dungeon dungeon, boolean isSpider) {
        // List <Entity> entities_encounter = dungeon.getEntities(getCurrPosition());
        // SwampTile swamp = dungeon.getSwamp(newPosition);
        // if(entities_encounter.isEmpty()) {
        //     updatePosition(newPosition, dungeon, isSpider);
        // }
        // //if it's a swamp, slower down
        // else if(swamp != null) {
        //     updatePosition(newPosition, dungeon, isSpider);
        // }
        updatePosition(newPosition, dungeon, isSpider);
    }

    public HashMap<String, Entity> getNearbyWalls(Dungeon dungeon) {
        HashMap<String, Entity> nearbyEntities = new HashMap<>();
        int x = getCurrPosition().getX();
        int y = getCurrPosition().getY();

        Entity topWall = dungeon.getEntities(new Position(x, y-1)).stream().filter(e -> e instanceof Wall).findAny().orElse(null);
        Entity bottomWall = dungeon.getEntities(new Position(x, y+1)).stream().filter(e -> e instanceof Wall).findAny().orElse(null);
        Entity leftWall = dungeon.getEntities(new Position(x-1, y)).stream().filter(e -> e instanceof Wall).findAny().orElse(null);
        Entity rightWall = dungeon.getEntities(new Position(x+1, y)).stream().filter(e -> e instanceof Wall).findAny().orElse(null);
        nearbyEntities.put("top", topWall);
        nearbyEntities.put("bottom", bottomWall);
        nearbyEntities.put("right", rightWall);
        nearbyEntities.put("left", leftWall);
        
        return nearbyEntities;
    }

    //getters and setters
    public Player setPlayer() {
        this.player = dungeon.getPlayer();
        return this.player;
    }

    public Position getCurrPosition() {
		return this.currPosition;
	}

	public void setCurrPosition(Position currPosition) {
		this.currPosition = currPosition;
        movingEntity.setCurr_position(currPosition);
	}

	public Position getPrevPosition() {
		return this.prevPosition;
	}

	public void setPrevPosition(Position prevPosition) {
		this.prevPosition = prevPosition;
        movingEntity.setPrev_position(prevPosition);
	}

}
