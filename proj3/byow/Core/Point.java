package byow.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Point implements Serializable {
    private final int x;
    private final int y;
    private int rank;

    private static final int[][] DIRECTIONS =
            new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.rank = 0;
    }

    public static List<Point> getFourNeighborDirs() {
        return getNeighborDirs(4);
    }

    public static List<Point> getEightNeighborDirs() {
        return getNeighborDirs(8);
    }

    private static List<Point> getNeighborDirs(int dirNum) {
        List<Point> dirs = new ArrayList<>();
        assert (dirNum <= 8);
        for (int i = 0; i < dirNum; i += 1) {
            dirs.add(new Point(DIRECTIONS[i][0], DIRECTIONS[i][1]));
        }
        return dirs;
    }

    public static List<Point> getFourNeighborPoints(Point p) {
        return getFourNeighborPoints(p.x, p.y);
    }

    public static List<Point> getFourNeighborPoints(int x, int y) {
        return getNeighborPoints(x, y, 4);
    }

    public static List<Point> getEightNeighborPoints(Point p) {
        return getEightNeighborPoints(p.x, p.y);
    }

    public static List<Point> getEightNeighborPoints(int x, int y) {
        return getNeighborPoints(x, y, 8);
    }

    private static List<Point> getNeighborPoints(int x, int y, int dirNum) {
        List<Point> points = new ArrayList<>();
        assert (dirNum <= 8);
        for (int i = 0; i < dirNum; i += 1) {
            points.add(new Point(x + DIRECTIONS[i][0], y + DIRECTIONS[i][1]));
        }
        return points;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRank() {
        return rank;
    }

    public void addRank() {
        rank += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Point other = (Point) o;
        return other.x == this.x && other.y == this.y;
    }

    @Override
    public int hashCode() {
        return x * Engine.WIDTH + y;
    }

}
