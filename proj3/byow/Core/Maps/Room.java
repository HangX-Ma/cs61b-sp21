package byow.Core.Maps;

import byow.Core.*;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private static final int MIN_WIDTH = 3;
    private static final int MIN_HEIGHT = 3;

    private static final int EXPLORING_TIMES = Math.min(Engine.HEIGHT, Engine.WIDTH);

    public static void createRooms(World world, Property property) {
        for (int i = 0; i < EXPLORING_TIMES; i += 1) {
            createSingleRoom(world, property);
        }
    }

    /** Randomly generate a room in 2D map without overlapping others.
     *  - The width, height and position of the room need to be odd.
     *  - Need to left one tile space for maze alignment. (This means
     *    the position for the inflated position need to be even.) */
    private static void createSingleRoom(World world, Property property) {
        int inflation = 1;
        int w = RandomUtils.uniform(property.getRandom(), 3) * 2 + MIN_WIDTH;
        int h = RandomUtils.uniform(property.getRandom(), 3) * 2 + MIN_HEIGHT;

        int wInflation = w + inflation * 2;
        int hInflation = h + inflation * 2;

        int x = world.getRandomOddX(w, inflation, property);
        int y = world.getRandomOddY(h, inflation, property);

        int xInflation = x - inflation;
        int yInflation = y - inflation;

        int tryTimes = 0;
        while (!world.isNothing(xInflation, yInflation)
                || isRoomOverlapped(xInflation, yInflation, wInflation, hInflation, world)) {
            x = world.getRandomOddX(w, inflation, property);
            y = world.getRandomOddY(h, inflation, property);
            xInflation = x - inflation;
            yInflation = y - inflation;
            tryTimes += 1;
            if (tryTimes >= EXPLORING_TIMES) {
                return;
            }
        }

        collectRoomAreas(x, y, w, h, property);

        Point roomRoot = new Point(x, y);
        /* Update the world */
        for (int i = x; i < x + w; i += 1) {
            for (int j = y; j < y + h; j += 1) {
                world.setTiles(i, j, Tileset.ROOM);
                /* Union all other room point to the point at (x, y) */
                Point roomPoint = new Point(i, j);
                property.getKruskalUnionMaps().put(roomPoint, roomPoint);
                Utils.kruskalUnion(roomPoint, roomRoot, property.getKruskalUnionMaps());
            }
        }
    }

    private static void collectRoomAreas(int x, int y, int w, int h, Property property) {
        property.getRoomAreas().put(new Point(x, y), new Point(x + w - 1, y + h - 1));
    }

    /** Check the target area if contains room tilt */
    private static boolean isRoomOverlapped(int x, int y, int w, int h, World world) {
        for (int i = x; i < x + w; i += 1) {
            for (int j = y; j < y + h; j += 1) {
                if (world.isRoom(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void createEntryAndExit(World world, Property property) {
        Point entryRoomDownLeft = getRandomRoom(property);
        Point entryRoomUpperRight = property.getRoomAreas().get(entryRoomDownLeft);

        List<Point> entryRoomPoints = getRoomPoints(entryRoomDownLeft, entryRoomUpperRight);
        Point entry = entryRoomPoints.get(RandomUtils.uniform(property.getRandom(), entryRoomPoints.size()));
        world.setEntry(entry);
        world.setTiles(entry, Tileset.ENTRY);

        Point exit = getExitRoomPoint(world, property);
        world.setExit(exit);
        world.setTiles(exit, Tileset.UNLOCKED_DOOR);
    }

    private static Point getExitRoomPoint(World world, Property property) {
        List<Point> points = Wall.scanAllWalls(world);

        Point exit;
        while (true) {
            exit = points.get(RandomUtils.uniform(property.getRandom(), points.size()));
            boolean valid = true;
            for (Point neighborPoint : Point.getEightNeighborPoints(exit.getX(), exit.getY())) {
                if (world.isRoad(neighborPoint.getX(), neighborPoint.getY())) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return exit;
            }
        }
    }

    private static Point getRandomRoom(Property property) {
        ArrayList<Point> rooms = new ArrayList<>(property.getRoomAreas().keySet());
        return rooms.get(RandomUtils.uniform(property.getRandom(), rooms.size()));
    }

    private static List<Point> getRoomPoints(Point downLeft, Point upperRight) {
        List<Point> points = new ArrayList<>();

        for (int x = downLeft.getX(); x <= upperRight.getX(); x += 1) {
            for (int y = downLeft.getY(); y <= upperRight.getY(); y += 1) {
                points.add(new Point(x, y));
            }
        }

        return points;
    }
}
