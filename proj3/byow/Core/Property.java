package byow.Core;

import java.io.Serializable;
import java.util.Random;

public class Property implements Serializable {
    World world;
    private Random random;

    Property(long seed) {
        random = new Random(seed);
    }

    Property() {
        world = new World(Engine.WIDTH - 3, Engine.HEIGHT - 3);
    }

    public Random getRandom() {
        return random;
    }
}
