package byow.Core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Property implements Serializable {
    private Random random;
    private HashMap<Point, Point> roomAreas;
    private HashSet<Point> roomSurroundedPoints;
    private HashMap<Point, Point> kruskalUnionMaps;

    Property(long seed) {
        random = new Random(seed);
        roomAreas = new HashMap<>();
        roomSurroundedPoints = new HashSet<>();
        kruskalUnionMaps = new HashMap<>();
    }

    public Random getRandom() {
        return random;
    }

    public HashMap<Point, Point> getRoomAreas() {
        return roomAreas;
    }

    public HashSet<Point> getRoomSurroundedPoints() {
        return roomSurroundedPoints;
    }

    public HashMap<Point, Point> getKruskalUnionMaps() {
        return kruskalUnionMaps;
    }
}
