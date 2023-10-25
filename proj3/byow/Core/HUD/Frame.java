package byow.Core.HUD;

import byow.Core.Engine;
import byow.Core.Utils;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.time.LocalTime;

public class Frame {
    enum MenuState {
        INITIAL,
        INPUT_SEED,
    }

    private int dungeonDepth;
    private final double cw;
    private final double ch;
    private MenuState state;

    public Frame() {
        dungeonDepth = 1;
        cw = Engine.WIDTH / 2.0;
        ch = Engine.HEIGHT / 1.8;
        state = MenuState.INITIAL;
    }

    public void menuInput(StringBuilder input) {
        boolean createNewWorld = false;
        while (true) {
            drawMenuWithState(input);
            if (StdDraw.hasNextKeyTyped()) {
                char typed = Character.toLowerCase(StdDraw.nextKeyTyped());
                System.out.println(typed);

                if (!createNewWorld) {
                    switch (typed) {
                        case 'n' -> {
                            input.append(typed);
                            createNewWorld = true;
                            state = MenuState.INPUT_SEED;
                        }
                        case 'l' -> {
                            input.append(typed);
                            return;
                        }
                    }
                } else {
                    if (Utils.NUMBER_SET.contains(typed)) {
                        input.append(typed);
                    } else if (typed == 's') {
                        input.append(typed);
                        return;
                    }
                }
            }
            StdDraw.show();
        }
    }

    public void actionInput(StringBuilder input) {
        boolean prepareToQuit = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char typed = Character.toLowerCase(StdDraw.nextKeyTyped());
                System.out.println(typed);

                switch (typed) {
                    case 'w', 'a', 's', 'd' -> {
                        // discard colon!
                        if (prepareToQuit) {
                            input.deleteCharAt(0);
                        }
                        input.append(typed);
                        return;
                    }
                    case ':' -> {
                        input.append(typed);
                        prepareToQuit = true;
                    }
                    case 'q' -> {
                        if (prepareToQuit) {
                            input.append(typed);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void frameInit() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setXscale(0, Engine.WIDTH);
        StdDraw.setYscale(0, Engine.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        Utils.usageHelper();
    }

    private void drawMenuWithState(StringBuilder input) {
        switch (state) {
            case INITIAL -> drawMenu();
            case INPUT_SEED -> {
                drawMenu();
                StdDraw.text(cw , ch - 10, "Enter a seed ending with 's'");
                StdDraw.text(cw, ch - 12, input.substring(1));
            }
        }

    }

    private void drawMenu() {
        StdDraw.clear(Color.BLACK);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.text(cw, Engine.HEIGHT - ch / 2.2, "CS61B: THE GAME");
        font = new Font("Comic Sans MS", Font.BOLD, 15);
        StdDraw.setFont(font);
        StdDraw.text(cw, ch - 2, "NEW GAME(N)");
        StdDraw.text(cw, ch - 4, "LOAD GAME(L)");
        StdDraw.text(cw, ch - 6, "QUIT (Q)");
    }

    public void drawFrame(TERenderer ter, TETile[][] tiles) {
        ter.renderFrame(tiles);
        drawDepth();
        StdDraw.show();
    }

    private void drawDepth() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(4, 1, " ---  Depth: " + dungeonDepth + "  --- ");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void delay(double sec) {
        double startTime = getTime();
        while (getTime() - startTime < sec) {
        }
    }

    private double getTime() {
        return LocalTime.now().getSecond() + LocalTime.now().getNano() / 1000000000.0;
    }

}
