package dungeonmania.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Position {
    private final int x, y, layer;

    public Position(int x, int y, int layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.layer = 0;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(x, y, layer);
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;

        // z doesn't matter
        return x == other.x && y == other.y;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getLayer() {
        return layer;
    }

    public final Position asLayer(int layer) {
        return new Position(x, y, layer);
    }

    public final Position translateBy(int x, int y) {
        return this.translateBy(new Position(x, y));
    }

    public final Position translateBy(Direction direction) {
        return this.translateBy(direction.getOffset());
    }

    public final Position translateBy(Position position) {
        return new Position(this.x + position.x, this.y + position.y, this.layer + position.layer);
    }

    public final Position calculatePositionAdd(Direction b) {
        // System.out.println("position/calculatePositionAdd x = " + translateBy(b).getX() + this.x);
        // System.out.println("position/calculatePositionAdd y = " + translateBy(b).getY() + this.y);
        // System.out.println("position/calculatePositionAdd position = " + new Position(translateBy(b).getX() + this.x, translateBy(b).getY() + this.y));
        return new Position(translateBy(b).getX() + this.x, translateBy(b).getY() + this.y);
    }

    public static Position calculatePositionAdd(Position a, Position b) {
        // System.out.println("position/calculatePositionAdd x = " + translateBy(b).getX() + this.x);
        // System.out.println("position/calculatePositionAdd y = " + translateBy(b).getY() + this.y);
        // System.out.println("position/calculatePositionAdd position = " + new Position(translateBy(b).getX() + this.x, translateBy(b).getY() + this.y));
        return new Position(a.getX() + b.getX(), a.getY() + b.getY());
    }

    // (Note: doesn't include z)

    /**
     * Calculates the position vector of b relative to a (ie. the direction from a
     * to b)
     * @return The relative position vector
     */
    public static final Position calculatePositionBetween(Position a, Position b) {
        return new Position(b.x - a.x, b.y - a.y);
    }

    /**
     * @author Alan
     * @param a
     * @param b
     * @return
     */
    public static final int calculateDistanceBetween(Position a, Position b) {
        return (Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()));
    }

    // public  static final boolean isAdjacent(Position a, Position b) {
    //     System.out.println("Position/isAdjacent --> player position = " + a + " merenary position = " + b);
    //     int x = a.x - b.x;
    //     int y = a.y - b.y;
    //     System.out.println("Position/isAdjacent --> x = " + x + " y = " + y);
    //     return x + y == 1;
    // }

    public  static final boolean isAdjacent(Position a, Position b) {
        int x = a.x - b.x;
        int y = a.y - b.y;
        return Math.abs(x + y) == 1;
    }

    // public  static final boolean isAdjacent(Position a, Position b, int battleRadius) {
    //     int x = a.x - b.x;
    //     int y = a.y - b.y;
    //     return x + y <= battleRadius;
    // }

    //Modified version. Rational: Battle radius is a circle, we need to check by having the maximum value of the differeneces.
    public  static final boolean isAdjacent(Position a, Position b, int battleRadius) {
        int x = a.x - b.x;
        int y = a.y - b.y;
        // System.out.println("Position/isAdjacent --> player position = " + a + " merenary position = " + b);
        // System.out.println("Position/isAdjacent --> x = " + x + " y = " + y + " max = " + Math.max(Math.abs(x), Math.abs(y)) + " ");
        return Math.max(Math.abs(x), Math.abs(y)) <= battleRadius;
    }

    // (Note: doesn't include z)
    public final Position scale(int factor) {
        return new Position(x * factor, y * factor, layer);
    }

    @Override
    public final String toString() {
        return "Position [x=" + x + ", y=" + y + ", z=" + layer + "]";
    }

    // Return Adjacent positions in an array list with the following element positions:
    // 0 1 2
    // 7 p 3
    // 6 5 4
    public List<Position> getAdjacentPositions() {
        List<Position> adjacentPositions = new ArrayList<>();
        adjacentPositions.add(new Position(x-1, y-1));
        adjacentPositions.add(new Position(x  , y-1));
        adjacentPositions.add(new Position(x+1, y-1));
        adjacentPositions.add(new Position(x+1, y));
        adjacentPositions.add(new Position(x+1, y+1));
        adjacentPositions.add(new Position(x  , y+1));
        adjacentPositions.add(new Position(x-1, y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    public List<Position> getMoveablePositions() {
        List<Position> adjacentPositions = new ArrayList<>();
        // adjacentPositions.add(new Position(x-1, y-1));
        adjacentPositions.add(new Position(x  , y-1));
        // adjacentPositions.add(new Position(x+1, y-1));
        adjacentPositions.add(new Position(x+1, y));
        // adjacentPositions.add(new Position(x+1, y+1));
        adjacentPositions.add(new Position(x  , y+1));
        // adjacentPositions.add(new Position(x-1, y+1));
        adjacentPositions.add(new Position(x-1, y));
        return adjacentPositions;
    }

    public static Position getDirection(Position prevPosition, Position currPosition) {
        Position newPosition = new Position(currPosition.getX() - prevPosition.getX(), currPosition.getY() - prevPosition.getY());
        return newPosition;
    }

}
