package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static byow.Core.Utils.*;

public class Engine {
    // FIXME: Delete if not debug
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;

    Property property;
    World world = new World(WIDTH, HEIGHT);


    /** Internet game sharing */
    // TODO: Add internet connection after keyboard function finished.
    public void interactWithRemoteClient(String input) throws IOException {
        if (!portCheck(input)) {
            throw new IOException("Input port needs to be 4 length digit");
        }
        int port = Integer.getInteger(input);
    }

    private boolean portCheck(String input) {
        if (input.length() != 4) {
            return false;
        }
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        long seed;
        String newInput = getFixedLowerCaseInput(input);
        char action = newInput.charAt(0);
        String commands = newInput.substring(1);

        Pair<Long, List<Character>> parsedCommands = null;
        switch (action) {
            case 'n' -> {
                parsedCommands/*[seed, actions]*/ = parseCommandN(commands);
                seed = parsedCommands.first();
                // FIXME: Delete if not debug
//                seed = LocalTime.now().toNanoOfDay();
                property = new Property(seed);
                world.initilizeWorld(property);
            }
            case 'l' -> {
                parsedCommands = parseCommandL(commands);
            }
        }
        // FIXME: Delete if not debug
//        ter.initialize(WIDTH, HEIGHT);
//        ter.renderFrame(world.getTiles());
        return world.getTiles();
    }

    private static String getFixedLowerCaseInput(String input) {
        StringBuilder inputBuilder = new StringBuilder();

        /* Make input case-insensitive */
        char[] lowerCaseInput = input.toLowerCase().toCharArray();

        /* Check the input to ensure it fits the key set requirement. */
        for (char c : lowerCaseInput) {
            if (!Utils.numberSet.contains(c) && !Utils.symbolSet.contains(c)) {
                continue;
            }
            inputBuilder.append(c);
        }

        /* retrieve information from neInput */
        return inputBuilder.toString();
    }

    static public void main(String[] args) {
        Engine engine = new Engine();
        String input1 = "n24958091840518SsswwWaasssSsdD:q";
        String input2 = "laasssSsdD:q";
        String input3 = "laasssSsdD";
        String input4 = "n2440518SsswwWaasss";
        engine.interactWithInputString(input1);
    }
}
