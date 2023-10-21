package byow.Core;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private int x;
    private int y;

    private static final int[][] directions =
            new int[][]{{1, 0}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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
            points.add(new Point(x + directions[i][0], y + directions[i][1]));
        }
        return points;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
