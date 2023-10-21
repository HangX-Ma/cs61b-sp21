package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    static Color azure4 = new Color(131, 139, 139);
    static Color slateBlue4 = new Color(71, 60, 139);
    static Color beige = new Color(245, 245, 220);
    static Color mistyRose = new Color(255, 228, 225);
    static Color lavender = new Color(230, 230, 250);
    static Color wheat = new Color(245, 222, 179);

    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL1 = new TETile('#', Color.black, beige, "wall1");
    public static final TETile WALL2 = new TETile('#', Color.black, mistyRose, "wall2");
    public static final TETile WALL3 = new TETile('#', Color.black, lavender, "wall3");
    public static final TETile WALL4 = new TETile('#', Color.black, wheat, "wall4");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    // user added
    public static final TETile ROOM = new TETile('·', azure4, slateBlue4, "room");
}


