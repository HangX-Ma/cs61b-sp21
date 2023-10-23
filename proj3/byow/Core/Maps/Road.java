package byow.Core.Maps;


import byow.Core.Point;
import byow.Core.Property;
import byow.Core.RandomUtils;
import byow.Core.World;
import byow.TileEngine.Tileset;

import java.util.*;

/** TODO: Get the collection of all Room points and create
 *  a group of separate union set.
 */
public class Road {
    private static final int WINDING_PERCENT = 40;

    public static void createRoad(World world, Property property) {
        for (int x = 1; x < world.getWidth() - 1; x += 2) {
            for (int y = 1; y < world.getHeight() - 1; y += 2) {
                if (world.isWall(x, y)) {
                    Point p = new Point(x, y);
                    growMaze(p, world, property);
                }

            }
        }
    }

    private static void growMaze(Point p, World world, Property property) {
        Stack<Point> cells = new Stack<>();
        Point lastDir = new Point(0, 0);

        setRoad(p, world);
        cells.push(p);

        while (!cells.isEmpty()) {
            Point nextPoint = cells.peek();
            List<Point> candidateDirs = new ArrayList<>();
            for (Point dir : Point.getFourNeighborDirs()) {
                if (canCarveCheck(nextPoint, dir, world)) {
                    candidateDirs.add(dir);
                }
            }
            Point nextDir;
            Point nextDirPoint;
            if (!candidateDirs.isEmpty()) {
                if (candidateDirs.contains(lastDir)
                        && RandomUtils.uniform(property.getRandom(), 0, 100) > WINDING_PERCENT) {
                    nextDir = lastDir;
                } else {
                    nextDir = candidateDirs.get(RandomUtils.uniform(property.getRandom(), candidateDirs.size()));
                }
                // Even position
                nextDirPoint = new Point(nextPoint.getX() + nextDir.getX(),
                        nextPoint.getY() + nextDir.getY());
                setRoad(nextDirPoint, world);
                // Odd position
                nextDirPoint = new Point(nextPoint.getX() + 2 * nextDir.getX(),
                        nextPoint.getY() + 2 * nextDir.getY());
                setRoad(nextDirPoint, world);

                cells.push(nextDirPoint);
                lastDir = nextDir;
            } else {
                // Cannot generate new path
                cells.pop();
                lastDir = new Point(0, 0);
            }
        }
    }

    /** Gets whether or not an opening can be carved from the given starting
     *  [Cell] at [pos] to the adjacent Cell facing [direction]. Returns `true`
     *  if the starting Cell is in bounds and the destination Cell is filled
     *  (or out of bounds) */
    private static boolean canCarveCheck(Point p, Point dir, World world) {
        // Must end in bounds.
        if (p.getX() + dir.getX() * 3 > world.getWidth() || p.getX() + dir.getX() * 3 < 0
            || p.getY() + dir.getY() * 3 > world.getHeight() || p.getY() + dir.getY() * 3 < 0) {
            return false;
        }

        // Destination must not be open.
        return world.isWall(p.getX() + 2 * dir.getX(), p.getY() + 2 * dir.getY());
    }

    private static boolean isConnected(Point p1, Point p2, HashMap<Point, Point> root) {
        return kruskalFind(p1, root).equals(kruskalFind(p2, root));
    }

    /** find ancestor */
    private static Point kruskalFind(Point unit, HashMap<Point, Point> root) {
        if (root.get(unit) != unit) {
            root.put(unit, kruskalFind(root.get(unit), root));
        }
        return root.get(unit);
    }

    /** We use rank union method, which will reduce union tree depth */
    private static void kruskalUnion(Point unit1, Point unit2, HashMap<Point, Point> root) {
        Point root1 = kruskalFind(unit1, root);
        Point root2 = kruskalFind(unit2, root);
        // Attach the lower rank to higher rank
        if (root1.getRank() <= root2.getRank()) {
            root.put(root1, root2);
        } else {
            root.put(root2, root1);
        }
        if (root1.getRank() == root2.getRank() && !root1.equals(root2)) {
            root2.addRank();
        }
    }

    public static void setRoad(int x, int y, World world) {
        Point p = new Point(x, y);
        setRoad(p, world);
    }

    public static void setRoad(Point p, World world) {
        world.setTiles(p, Tileset.FLOOR);
    }
}
