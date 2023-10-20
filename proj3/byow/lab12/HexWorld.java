package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static int edgeSize;
    private static int hexagonMaxWidth;
    private static int hexagonMaxHeight;

    /** Add hexagon at specific location. I set the leftmost corner as the start position.
     *  You can image a hexagon is surrounded by a square box.
     * e.g.
     *     p
     *     |
     *     v
     *    ++==++
     *    +====+
     *    ++==++
     * */
    private static void addHexagon(TETile[][] tiles, Position p, TETile tiltType, int base, int length) {
        Position startPoint = p.shift(base, 0);
        drawRow(tiles, startPoint, tiltType, length);
        // draw remaining recursively
        if (base > 0) {
            Position nextPoint = p.shift(0, -1);
            addHexagon(tiles, nextPoint, tiltType, base - 1, length + 2);
        }
        Position mirrorStartPoint = startPoint.shift(0, -(2 * base + 1));
        drawRow(tiles, mirrorStartPoint, tiltType, length);
    }

    private static void drawRow(TETile[][] tiles, Position p, TETile tiltType, int length) {
        for (int dx = 0; dx < length; dx += 1) {
            tiles[p.x + dx][p.y] = tiltType;
        }
    }

    private static void drawHexagonColumn(TETile[][] tiles, Position p, int num) {
        for (int i = 0; i < num; i += 1) {
            Position startPoint = p.shift(0, -i * getHexagonMaxHeight());
            addHexagon(tiles, startPoint, randomTile(), edgeSize - 1, edgeSize);
        }
    }

    private static void drawWorld(TETile[][] tiles, Position p, int level) {
        int maxNum = 2 * level - 1;
        // draw from top to left
        for (int i = 0; i < level; i += 1) {
            drawHexagonColumn(tiles, p.shift(-(getHexagonWidth() - edgeSize + 1) * i, -(edgeSize) * i), maxNum - i);
        }

        // draw from top to right
        for (int i = 0; i < level; i += 1) {
            drawHexagonColumn(tiles, p.shift((getHexagonWidth() - edgeSize + 1) * i, -(edgeSize) * i), maxNum - i);
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(7);
        return switch (tileNum) {
            case 0 -> Tileset.WALL;
            case 1 -> Tileset.FLOWER;
            case 2 -> Tileset.SAND;
            case 3 -> Tileset.GRASS;
            case 4 -> Tileset.TREE;
            case 5 -> Tileset.FLOOR;
            case 6 -> Tileset.WATER;
            default -> Tileset.NOTHING;
        };
    }

    /**
     * Generate a world with hexagon shape
     * @param tiles TETile array, actually contains the tile property in the world
     * @param hexagonSize each hexagon size
     * @param level the hexagon range level
     */
    public static void generateHexagonWorld(TETile[][] tiles, int hexagonSize, int level) {
        edgeSize = Math.max(hexagonSize, 2);
        hexagonMaxWidth = 3 * edgeSize - 2;
        hexagonMaxHeight = 2 * edgeSize;
        Position p = new Position(20, 40);
        level = checkLevel(level, p);

//        addHexagon(tiles, p, Tileset.SAND, edgeSize - 1, edgeSize); // TEST-1
//        drawHexagonColumn(tiles, p, level); // TEST-2
        drawWorld(tiles, p, level);
    }

    /** Check the level param to fit the world size */
    private static int checkLevel(int level, Position p) {
        level = checkHeightLimit(level, p);
        level = checkWidthLimit(level, p);
        return Math.max(1, level);
    }

    private static int checkHeightLimit(int level, Position p) {
        if (p.y > HEIGHT - 1 || p.y < 0) {
            throw new IllegalArgumentException("You need to set an appropriate start 'y' axis! [0, HEIGHT - 1]");
        }
        int wholeHexagonHeight = (level * 2 - 1) * getHexagonMaxHeight();
        if (wholeHexagonHeight > p.y) {
            return checkHeightLimit(level - 1, p);
        }
        return level;
    }

    private static int checkWidthLimit(int level, Position p) {
        if (p.x > WIDTH - 1 || p.x < 0) {
            throw new IllegalArgumentException("You need to set an appropriate start 'x' axis! [0, WIDTH - 1]");
        }
        int halfHexagonWidth = (level * getHexagonWidth() + (level - 1) * edgeSize) / 2;
        if (p.x + halfHexagonWidth + edgeSize > WIDTH - 1 || p.x - (halfHexagonWidth - edgeSize) < 0) {
            return checkWidthLimit(level - 1, p);
        }
        return level;
    }

    /** Get the width of one hexagon according to its edge size */
    private static int getHexagonWidth() {
        return hexagonMaxWidth;
    }

    /** Get the height of one hexagon according to its edge size */
    private static int getHexagonMaxHeight() {
        return hexagonMaxHeight;
    }

    private static class Position {
        int x;
        int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position shift(int dx, int dy) {
            return new Position(x + dx, y + dy);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] hexTiles= new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hexTiles[x][y] = Tileset.NOTHING;
            }
        }

        generateHexagonWorld(hexTiles, 4, 3);

        ter.renderFrame(hexTiles);
    }
}
