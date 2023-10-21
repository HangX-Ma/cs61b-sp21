package byow.Core.Maps;

import byow.Core.Point;
import byow.Core.Property;
import byow.Core.RandomUtils;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;

public class Wall {
    static public void createWalls(World world, Property property) {
        HashSet<Point> points = property.getRoomSurroundedPoints();
        for (Point p : points) {
            if (RandomUtils.bernoulli(property.getRandom(), 0.3)) {
                world.setTiles(p.getX(), p.getY(), getRandomWallTile(property));
            } else {
                world.setTiles(p.getX(), p.getY(), Tileset.FLOOR);
            }
        }
    }

    static private TETile getRandomWallTile(Property property) {
        return switch (RandomUtils.uniform(property.getRandom(), 4)) {
            case 0 -> Tileset.WALL1;
            case 1 -> Tileset.WALL2;
            case 2 -> Tileset.WALL3;
            case 3 -> Tileset.WALL4;
            default -> Tileset.NOTHING;
        };
    }
}
