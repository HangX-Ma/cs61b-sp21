package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

public class World implements Serializable {
    private final int width;
    private final int height;

    private TETile[][] tiles;

    World(int w, int h) {
        width = w;
        height = h;
        tiles = new TETile[width][height];
    }

    public void generateWorld(long seed) {
        Property property = new Property(seed);
        fillWorldNothing();

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TETile[][] getTiles() {
        return tiles;
    }

    public boolean isNothing(int x, int y) {
        return tiles[x][y].equals(Tileset.NOTHING);
    }

    public boolean isWall(int x, int y) {
        return tiles[x][y].equals(Tileset.WALL);
    }

    public boolean isRoom(int x, int y) {
        return tiles[x][y].equals(Tileset.FLOOR);
    }

    public boolean isRoad(int x, int y) {
        return tiles[x][y].equals(Tileset.FLOOR);
    }

}
