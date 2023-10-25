package byow.Core.Maps;

import byow.Core.*;
import byow.TileEngine.Tileset;
import java.util.*;

public class Road {
    private static final int WINDING_PERCENT = 40;
    private static final int REMOVE_DEAD_ENDS_TIMES = 20;
    private static final double MAZE_GEN_RATIO = 0.7; // Maze area ratio

    public static void createRoad(World world, Property property) {
        for (int x = 1; x < world.getWidth() - 1; x += 2) {
            for (int y = 1; y < world.getHeight() - 1; y += 2) {
                if (world.isWall(x, y)) {
                    Point p = new Point(x, y);
                    growMaze(p, world, property);
                }
            }
        }
        addConnector(world, property);
        removeDeadEnds(world, property);
    }

    /** Generate maze path in rest empty space. */
    private static void growMaze(Point p, World world, Property property) {
        Stack<Point> cells = new Stack<>();
        Point lastDir = new Point(0, 0);

        setRoad(p, world);
        cells.push(p);
        property.getKruskalUnionMaps().put(p, p); // root of this maze path

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
                    nextDir = candidateDirs.get(
                            RandomUtils.uniform(property.getRandom(), candidateDirs.size()));
                }
                // Even position
                nextDirPoint = new Point(nextPoint.getX() + nextDir.getX(),
                        nextPoint.getY() + nextDir.getY());
                updateMaze(nextDirPoint, p, world, property);

                // Odd position
                nextDirPoint = new Point(nextPoint.getX() + 2 * nextDir.getX(),
                        nextPoint.getY() + 2 * nextDir.getY());
                updateMaze(nextDirPoint, p, world, property);

                cells.push(nextDirPoint);
                lastDir = nextDir;
            } else {
                // Cannot generate new path
                cells.pop();
                lastDir = new Point(0, 0);
            }
        }
    }

    private static void updateMaze(Point mazePoint, Point rootPoint,
                                   World world, Property property) {
        setRoad(mazePoint, world);
        property.getKruskalUnionMaps().put(mazePoint, mazePoint);
        Utils.kruskalUnion(mazePoint, rootPoint, property.getKruskalUnionMaps());
    }

    /**
     * Gets whether an opening can be carved from the given starting
     * [Cell] at [pos] to the adjacent Cell facing [direction]. Returns `true`
     * if the starting Cell is in bounds and the destination Cell is filled
     * (or out of bounds)
     *
     * @param p     current point
     * @param dir   neighbor directions
     * @param world world instance
     */
    private static boolean canCarveCheck(Point p, Point dir, World world) {
        // Must end in bounds.
        if (p.getX() + dir.getX() * 3 > world.getWidth() * (1.0 - Road.MAZE_GEN_RATIO)
            || p.getX() + dir.getX() * 3 < world.getWidth() * Road.MAZE_GEN_RATIO
            || p.getY() + dir.getY() * 3 > world.getHeight() * (1.0 - Road.MAZE_GEN_RATIO)
            || p.getY() + dir.getY() * 3 < world.getHeight() * Road.MAZE_GEN_RATIO) {
            return false;
        }

        // Destination must not be open.
        return world.isWall(p.getX() + 2 * dir.getX(), p.getY() + 2 * dir.getY());
    }

    /** Create a minimum spanning tree. We serve every room
     *  and maze path as separately single vertex. */
    private static void addConnector(World world, Property property) {
        HashMap<Point, List<Point>> connectorMaps = getRegionConnectors(world);
        List<Point> connectors = new ArrayList<>(connectorMaps.keySet());

        while (!connectors.isEmpty()) {
            Point connector = connectors.get(RandomUtils.uniform(property.getRandom(), connectors.size()));
            List<Point> points = connectorMaps.get(connector);
            assert (points.size() == 2);
            if (!Utils.isConnected(points.get(0), points.get(1), property.getKruskalUnionMaps())) {
                Road.setRoad(connector, world);
                Utils.kruskalUnion(points.get(0), points.get(1), property.getKruskalUnionMaps());
            }
            connectors.remove(connector);
        }
    }

    /** Search connectors that can connect two regions in the empty space(Fill with 'Nothing'). */
    private static HashMap<Point, List<Point>> getRegionConnectors(World world) {
        HashMap<Point, List<Point>> regionConnectors = new HashMap<>();
        for (int x = 1; x < world.getWidth() - 1; x += 1) {
            for (int y = 1; y < world.getHeight() - 1; y += 1) {
                if (world.isNothing(x, y)) {
                    List<Point> passageSet = new ArrayList<>();
                    for (Point neighborPoint : Point.getFourNeighborPoints(x, y)) {
                        if (!world.isNothing(neighborPoint.getX(), neighborPoint.getY())) {
                            passageSet.add(neighborPoint);
                        }
                    }
                    if (passageSet.size() >= 2) {
                        regionConnectors.put(new Point(x, y), passageSet);
                    }
                }
            }
        }
        return regionConnectors;
    }

    /** Simplify the maze! */
    private static void removeDeadEnds(World world, Property property) {
        List<Point> deadEndPoints = getDeadEnds(world);
        while (!deadEndPoints.isEmpty()) {
            Point checkPoint = deadEndPoints.remove(
                    RandomUtils.uniform(property.getRandom(), deadEndPoints.size()));
            int count = RandomUtils.uniform(property.getRandom(),
                                        REMOVE_DEAD_ENDS_TIMES / 2,
                                           REMOVE_DEAD_ENDS_TIMES);
            for (int i = 0; i < count; i += 1) {
                List<Point> nextDirs = new ArrayList<>();
                for (Point dir : Point.getFourNeighborDirs()) {
                    if (!world.isNothing(checkPoint.getX() + dir.getX(),
                                         checkPoint.getY() + dir.getY())) {
                        nextDirs.add(dir);
                    }
                }
                if (nextDirs.size() != 1) {
                    break;
                }
                // Surrounded with 3 'Nothing' tile. Dead End!
                world.setTiles(checkPoint.getX(), checkPoint.getY(), Tileset.NOTHING);
                checkPoint = new Point(checkPoint.getX() + nextDirs.get(0).getX(),
                        checkPoint.getY() + nextDirs.get(0).getY());
            }
        }
    }

    private static List<Point> getDeadEnds(World world) {
        List<Point> deadEndPoints = new ArrayList<>();
        for (int x = 1; x < world.getWidth() - 1; x += 1) {
            for (int y = 1; y < world.getHeight() - 1; y += 1) {
                if (world.isRoad(x, y)) {
                    Point checkPoint = new Point(x, y);
                    List<Point> nextDirs = new ArrayList<>();
                    for (Point dir : Point.getFourNeighborDirs()) {
                        if (!world.isNothing(checkPoint.getX() + dir.getX(),
                                             checkPoint.getY() + dir.getY())) {
                            nextDirs.add(dir);
                        }
                    }
                    if (nextDirs.size() == 1) {
                        deadEndPoints.add(checkPoint);
                    }
                }
            }
        }
        return deadEndPoints;
    }

    public static void setRoad(int x, int y, World world) {
        Point p = new Point(x, y);
        setRoad(p, world);
    }

    public static void setRoad(Point p, World world) {
        world.setTiles(p, Tileset.FLOOR);
    }
}
