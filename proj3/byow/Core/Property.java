package byow.Core;

import byow.Core.Avatar.Avatar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class Property implements Serializable {
    World shadowWorld;
    World gameWorld;
    Avatar avatar;
    private Random random;
    private final HashMap<Point, Point> roomAreas;
    private final HashMap<Point, Point> kruskalUnionMaps;

    public Property() {
        shadowWorld = new World(Engine.WIDTH - 3, Engine.HEIGHT - 3);
        roomAreas = new HashMap<>();
        kruskalUnionMaps = new HashMap<>();
    }

    public Property(long seed) {
        this();
        random = new Random(seed);
    }

    public void setRandom(long seed) {
        this.random = new Random(seed);
    }

    public Random getRandom() {
        return random;
    }

    public HashMap<Point, Point> getRoomAreas() {
        return roomAreas;
    }

    public HashMap<Point, Point> getKruskalUnionMaps() {
        return kruskalUnionMaps;
    }
}
