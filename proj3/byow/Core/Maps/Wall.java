package byow.Core.Maps;

import byow.Core.Point;
import byow.Core.Property;
import byow.Core.RandomUtils;
import byow.Core.World;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class Wall {
    public static void createWalls(World world, Property property) {
        for (int x = 1; x < world.getWidth() - 1; x += 2) {
            for (int y = 1; y < world.getHeight() - 1; y += 2) {
                if (world.isNothing(x, y)) {
                    setWall(x, y, world, property);
                }
            }
        }
    }

    public static List<Point> scanAllWalls(World world) {
        List<Point> walls = new ArrayList<>();
        for (int x = 1; x < world.getWidth() - 1; x += 1) {
            for (int y = 1; y < world.getHeight() - 1; y += 1) {
                if (world.isWall(x, y)) {
                    walls.add(new Point(x, y));
                }
            }
        }
        return walls;
    }

    public static void setWall(int x, int y, World world, Property property) {
        Point p = new Point(x, y);
        setWall(p, world, property);
    }

    public static void setWall(Point p, World world, Property property) {
        world.setTiles(p, getRandomWallTile(property));
    }

    private static TETile getRandomWallTile(Property property) {
        return switch (RandomUtils.uniform(property.getRandom(), 4)) {
            case 0 -> Tileset.WALL1;
            case 1 -> Tileset.WALL2;
            case 2 -> Tileset.WALL3;
            case 3 -> Tileset.WALL4;
            default -> Tileset.NOTHING;
        };
    }
}
