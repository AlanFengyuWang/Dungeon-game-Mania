package dungeonmania.dungeonGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.util.Position;

public class MazeGenerator {
    private boolean[][] maze;
    private int width;
    private int height;
    private Position start;
    private Position end;
    Random random;

    public MazeGenerator() {
        this.random = new Random(System.currentTimeMillis());
    }

    public void RandomizedPrims(int width, int height, Position start, Position end) {
        this.width = width;
        this.height = height;
        this.start = start;
        this.end = end;

        // let maze be a 2D array of booleans (of size width and height) default false
        // false representing a wall and true representing empty space
        maze = new boolean[height][width];

        // mark the start position in the array to be empty
        maze[start.getY()][start.getX()] = true;

        // let options be a list of positions
        // add to options all neighbours of 'start' not on boundary that are of distance 2 away and are walls
        List<Position> options = findNeighbours(start, 2, false);

        // while options is not empty:
        while (!options.isEmpty()) {
            // let next = remove random from options
            Position next = options.get(random.nextInt(options.size()));
            options.remove(next);

            // let neighbours = each neighbour of distance 2 from next not on boundary that are empty
            List<Position> neighbours = findNeighbours(next, 2, true);

            // if neighbours is not empty:
            if (!neighbours.isEmpty()) {
                // let neighbour = random from neighbours
                Position neighbour = neighbours.get(random.nextInt(neighbours.size()));

                // maze[ next ] = empty (i.e. true)
                // maze[ position inbetween next and neighbour ] = empty (i.e. true)
                // maze[ neighbour ] = empty (i.e. true)
                maze[next.getY()][next.getX()] = true;
                maze[(next.getY() + neighbour.getY()) / 2][(next.getX() + neighbour.getX()) / 2] = true;
                maze[neighbour.getY()][neighbour.getX()] = true;
            }

            // add to options all neighbours of 'next' not on boundary that are of distance 2 away and are walls
            List<Position> neighboursWalls = findNeighbours(next, 2, false);
            for (Position p : neighboursWalls) {
                boolean duplicate = false;
                // TODO check if this is necessary
                // check that position is not already in options
                for (Position op : options) {
                    if (p.equals(op)) {
                        duplicate = true;
                    }
                }
                // add to options if the position is not already in
                if (!duplicate) {
                    options.add(p);
                }
            }
        }
        // at the end there is still a case where our end position isn't connected to the map
        // we don't necessarily need this, you can just keep randomly generating maps (was original intention)
        // but this will make it consistently have a pathway between the two.

        // if maze[end] is a wall:
        if (!maze[end.getY()][end.getX()]) {
            // maze[end] = empty
            maze[end.getY()][end.getX()] = true;
        }

        // let neighbours = neighbours not on boundary of distance 1 from maze[end]
        List<Position> neighbours = findNeighbours(end, 1, true);

        // if there are no cells in neighbours that are empty:
        // let's connect it to the grid
        if (neighbours.isEmpty()) {
            // let neighbour = random from neighbours
            neighbours = findNeighbours(end, 1, false);
            Position neighbour = neighbours.get(random.nextInt(neighbours.size()));
            // maze[neighbour] = empty
            maze[neighbour.getY()][neighbour.getX()] = true;
        }

        
    }

    private List<Position> findNeighbours(Position pos, int distance, boolean isEmpty) {
        List<Position> neighbours = new ArrayList<Position>();

        // TODO theres probably a cleaner way to do this
        if (pos.getY() - distance > 0 && maze[pos.getY() - distance][pos.getX()] == isEmpty) {
            neighbours.add(new Position(pos.getX(), pos.getY() - distance));
        }
        if ((pos.getY() + distance) < height - 1 && maze[pos.getY() + distance][pos.getX()] == isEmpty) {
            neighbours.add(new Position(pos.getX(), pos.getY() + distance));
        }
        if (pos.getX() - distance > 0 && maze[pos.getY()][pos.getX() - distance] == isEmpty) {
            neighbours.add(new Position(pos.getX() - distance, pos.getY()));
        }
        if (pos.getX() + distance < width - 1 && maze[pos.getY()][pos.getX() + distance] == isEmpty) {
            neighbours.add(new Position(pos.getX() + distance, pos.getY()));
        }

        return neighbours;
    }

    public boolean[][] getMaze() {
        return this.maze;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Position getStart() {
        return this.start;
    }

    public Position getEnd() {
        return this.end;
    }
}
