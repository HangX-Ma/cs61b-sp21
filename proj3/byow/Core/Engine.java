package byow.Core;

import byow.Core.Avatar.Avatar;
import byow.Core.HUD.Frame;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import java.io.IOException;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;

    Property property = new Property();
    TERenderer ter = new TERenderer();

    boolean gameStart = false;

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
        ter.initialize(WIDTH, HEIGHT, 2, 2);

        Frame frame = new Frame();
        frame.frameInit();

        while (true) {
            StringBuilder input = new StringBuilder();
            if (!gameStart) {
                frame.menuInput(input);
            } else {
                frame.actionInput(input);
            }
            TETile[][] tiles = interactWithInputString(input.toString());
            if (tiles == null) { return; }
            frame.drawFrame(ter, tiles);
        }
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
        // DONE: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        String newInput = getFixedLowerCaseInput(input);

        if (!gameStart) {
            char action = newInput.charAt(0);
            newInput = newInput.substring(1);

            switch (action) {
                case 'n' -> {
                    int seedEndIndex = newInput.indexOf('s');
                    System.out.println(newInput.substring(seedEndIndex));
                    long seed = Utils.getSeedFromString(newInput.substring(0, seedEndIndex));
                    generateWorld(seed, property);
                    input = Utils.parseCommand(newInput, seedEndIndex + 1);
                }
                case 'l' -> {
                    property = Utils.load();
                    input = Utils.parseCommand(newInput, 0);
                }
            }
            gameStart = true;
        }
        Utils.move(input, property);
        parseSuffixCommand(newInput, property);

        return property.gameWorld.getTiles();
    }

    private static void parseSuffixCommand(String input, Property property) {
        if (input == null || input.isEmpty()) {
            return;
        }
        int colonIndex = input.indexOf(':');
        if (colonIndex != -1) {
            switch (input.charAt(colonIndex + 1)) {
                case 'q' -> {
                    Utils.quit(property);
                }
                default -> {
                    System.out.println("Coming soon...");
                }
            }
        }
    }

    private static String getFixedLowerCaseInput(String input) {
        StringBuilder inputBuilder = new StringBuilder();

        /* Make input case-insensitive */
        char[] lowerCaseInput = input.toLowerCase().toCharArray();

        /* Check the input to ensure it fits the key set requirement. */
        for (char c : lowerCaseInput) {
            if (!Utils.NUMBER_SET.contains(c) && !Utils.SYMBOL_SET.contains(c)) {
                continue;
            }
            inputBuilder.append(c);
        }

        /* retrieve information from neInput */
        return inputBuilder.toString();
    }

    private static void generateWorld(long seed, Property property) {
        property.shadowWorld.initilizeWorld(seed, property);
        property.gameWorld = property.shadowWorld.clone();
        property.avatar = new Avatar(property.gameWorld);
        property.avatar.avatarMove(property.gameWorld, "");
    }

    static public void main(String[] args) {
        Engine engine = new Engine();
        String input1 = "n24958091840518SsswwWaasssSsdD:q";
        String input2 = "laasssSsdD:q";
        String input3 = "la:q";
        String input4 = "n2440518SsswwWaasss";
        engine.interactWithInputString(input3);
    }
}
