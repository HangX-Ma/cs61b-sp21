package byow.Core.Avatar;

import byow.Core.Point;
import byow.Core.World;
import byow.TileEngine.Tileset;

import java.io.Serializable;

public class Avatar implements Serializable {
    private final Tofu tofu;

    public Avatar(World world) {
        Point entry = world.getEntry();
        tofu = new Tofu(entry.getX(), entry.getY());
    }

    public void avatarMove(World world, String input) {
        switch (input) {
            case "w" -> tofu.move(Tofu.TofuAction.UP, world);
            case "s" -> tofu.move(Tofu.TofuAction.DOWN, world);
            case "a" -> tofu.move(Tofu.TofuAction.LEFT, world);
            case "d" -> tofu.move(Tofu.TofuAction.RIGHT, world);
        }
        world.setTiles(tofu.getX(), tofu.getY(), Tileset.AVATAR);
    }
}
