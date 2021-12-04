package dungeonmania.movingEntity.moveBehaviour;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.movingEntityStates.AllyState;
import dungeonmania.util.Position;

public class MoveTowardPlayerBehavior extends MovingBehavior{

    public MoveTowardPlayerBehavior(Dungeon dungeon, MovingEntity movingEntity, Position currPosition) {
        super(dungeon, movingEntity, currPosition);
    }

    @Override
    public void makeMove() {
       //if it's in the battle radius, it will moves twice
       if(Position.isAdjacent(player.getCurr_position(), getCurrPosition(), movingEntity.getBattleRadius())) {
            doApproachPlayer(player, dungeon, 2);
        }
        //when the mercenary is far away.
        else {
            //continue moving toward player and then battle if it's enemy, follow if it's ally
            doApproachPlayer(player, dungeon, 1);
        }
    }

    /**
     * Logic:
     * Let the position of the mercenary be (x2, y2), that of the player is (x1, y1).
     * moving closer to the player means moving the player by (x2 - x1, y2 - y1).
     * @param player
     * @param dungeon
     * @param num_moves
     */
    public void doApproachPlayer(Player player, Dungeon dungeon, int num_moves) {
        for(int i = 0; i < num_moves; i++) {
            // System.out.println("mercenary/ monster curr position = " + getCurr_position() + " player prev position = " + player.getPrev_position());

            //use Dijkstra to find the closest adjacent neighbor
            HashMap<Position, Position> travelMap = dijkstras(dungeon, player);

            //get the nextStop
            Position nextStop = generateNextStop(travelMap, player.getCurr_position(), null);

            Position newPosition = new Position(getCurrPosition().getX(), getCurrPosition().getY());
            List <Entity> entities_ecounter = dungeon.getEntities(getCurrPosition());
            
            //if the mercenary is trapped, it will not move

            //otherwise, it will update accordingly
            if(nextStop != null) {
                newPosition = new Position(nextStop.getX(), nextStop.getY());
                
                //if it's ally, it will stop before hit the player
                if(movingEntity.getState() instanceof AllyState && newPosition.equals(player.getCurr_position()))
                    newPosition = movingEntity.getCurr_position();
                
                entities_ecounter = dungeon.getEntities(newPosition);
            }

            //if it's a ground, change position
            //if it's swamp, slower down
            moveToTile(newPosition, dungeon, false);

            //update each entities
            for(Entity e : entities_ecounter) {
                movingEntity.doInteract(e, dungeon);
            }
        }
    }

    public HashMap<Position, Position> dijkstras(Dungeon dungeon, Player player) {
        /**
         * function Dijkstras(grid, source):
            let dist be a Map<Position, Double>
            let prev be a Map<Position, Position>

            for each Position p in grid:
                dist[p] := infinity
                previous[p] := null
            dist[source] := 0

            let queue be a Queue<Position> of every position in grid
            while queue is not empty:
                u := next node in queue with the smallest dist
                for each cardinal neighbour v of u:
                    if dist[u] + cost(u, v) < dist[v]:
                        dist[v] := dist[u] + cost(u, v)
                        previous[v] := u
            return previous
         */

        //initialize dist and prev
        HashMap<Position, Double> dist = new HashMap<>();
        HashMap<Position, Position> prev = new HashMap<>();
        Queue<Position> queue = new LinkedList<>();

        //get all unblockable positions
        List<Position> unblockablePositions = dungeon.getUnblockablePositions();
        //put the current mercenary's position in
        unblockablePositions.add(getCurrPosition());

        //debg
        // if(unblockablePositions.contains(player.getCurr_position())) System.out.println("player is in ");
        // else System.out.println("player is not in ");
        
        //initialize all dist to inf, prev to null
        //-1 represents infinity 
        for(Position moveablePosition : unblockablePositions) {
            dist.put(moveablePosition, Double.MAX_VALUE);
            // prev.put(null, null);
            queue.add(moveablePosition);
        }

        Position src = getCurrPosition();
        prev.put(src, null); //the mercenary's prev location is null
        dist.put(src, 0.0);

        //update distance
        while(queue.size() > 0) {
            //next node in queue with the smallest dist
            Position u = nextNodeSmallestDist(queue, dist);

            //if v is null, it means there's no adjacent place it can go
            if(u == null) {
                return prev;
            }
            
            //find each cardinal neighbour v of u
            Position nextNeighborPosition = new Position(u.getX(), u.getY());
            for(Position v : nextNeighborPosition.getMoveablePositions()) {
                //check if it's movable to the new location AND
                //check if the dist from neighbor to u + dist from src to u < dist from src to neighbor
                if(dungeon.isUnblock(v)) {
                    //debg
                    // System.out.println("Mercenary/dijkstra  -->  dist.get(u) = " + dist.get(u) + " Position.calculateDistanceBetween(u, v) = " + Position.calculateDistanceBetween(u, v) +
                    // " dist.get(v) = " + dist.get(v));
                    if(dist.get(v) != null && dist.get(u) != null && dist.get(u) + dungeon.calculateDistanceBetweenPositions(u, v) < dist.get(v)) 
                    {
                        //update the dist
                        dist.put(v, dist.get(u) + dungeon.calculateDistanceBetweenPositions(u, v));
                        //track the way
                        prev.put(v, u);
                    }
                }
            }
            //pop from the queue
            queue.remove(u);
        }
        return prev;
    }

    public Position nextNodeSmallestDist(Queue<Position> queue, HashMap<Position, Double> dist) {
        //get the smallest distance from the start
        Double smallestDistance = Double.MAX_VALUE;
        Position smallestDistPosition = null;
        
        //iterate through the queue and find the min value
        for(Position position : queue) {
            if(dist.get(position) < smallestDistance) {
                smallestDistance = dist.get(position);
                smallestDistPosition = position;
            }
        }

        return smallestDistPosition;
    }

    public Position generateNextStop(HashMap<Position, Position> travelMap, Position whereAtNow, Position next) {
        //when there's no more location to go
        if(whereAtNow == null) return null;

        //get the next move location for the mercenary
        if(whereAtNow.equals(getCurrPosition())) {
            return next;
        } 
        //move to the next unit
        return generateNextStop(travelMap, travelMap.get(whereAtNow), whereAtNow);
    }
}
