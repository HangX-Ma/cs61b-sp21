package byow.Core;

import byow.Core.Maps.Room;
import byow.Core.Maps.Wall;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

/**
 * A World class whose origin point locates at the right-down corner.
 * e.g.
 *  y
 *  |
 *  |________ x
 * O
 */
public class World implements Serializable {
    private final int width;
    private final int height;

    private TETile[][] tiles;

    World(int w, int h) {
        width = w;
        height = h;
        tiles = new TETile[width][height];
    }

    public void initilizeWorld(Property property) {
        fillWorldNothing();

        Room.createRooms(this, property);
        Wall.createWalls(this, property);

    }

    public void fillWorldNothing() {
        for (int w = 0; w < width; w += 1) {
            for (int h = 0; h < height; h += 1) {
                tiles[w][h] = Tileset.NOTHING;
            }
        }
    }


    @Override
    public World clone() {
        World ret = new World(width, height);
        ret.tiles = TETile.copyOf(tiles);

        return ret;
    }

    /**
     * Get random x-axis value that can fit 'w' length
     * @param w unit width
     * @param inflation inflation padding
     * @param property game property set
     * @return randomly selected 'x'
     */
    public int getRandomX(int w, int inflation, Property property) {
        return RandomUtils.uniform(property.getRandom(), inflation * 2, width - w - inflation);
    }

    /**
     * Get random y-axis value that can fit 'h' length
     * @param h unit width
     * @param inflation inflation padding
     * @param property game property set
     * @return randomly selected 'y'
     */
    public int getRandomY(int h, int inflation, Property property) {
        return RandomUtils.uniform(property.getRandom(), inflation * 2, height - h - inflation);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public void setTiles(int x, int y, TETile tile) {
        tiles[x][y] = tile;
    }

    public boolean isNothing(int x, int y) {
        return tiles[x][y].equals(Tileset.NOTHING);
    }

    public boolean isWall(int x, int y) {
        return tiles[x][y].equals(Tileset.WALL1) || tiles[x][y].equals(Tileset.WALL2)
                || tiles[x][y].equals(Tileset.WALL3) || tiles[x][y].equals(Tileset.WALL4);
    }

    public boolean isRoom(int x, int y) {
        return tiles[x][y].equals(Tileset.ROOM);
    }

    public boolean isRoad(int x, int y) {
        return tiles[x][y].equals(Tileset.FLOOR);
    }

}
