package byow.Core.Maps;

import byow.Core.*;
import byow.TileEngine.Tileset;

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

}
