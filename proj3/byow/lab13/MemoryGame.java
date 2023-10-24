package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.time.LocalTime;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private final int width;
    /** The height of the window of this game. */
    private final int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private final Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;

        initCanvas();

        //DONE: Initialize random number generator
        rand = new Random(seed);
        round = 1;
        gameOver = false;
        playerTurn = false;
    }

    private void initCanvas() {
        StdDraw.setCanvasSize(width * 16, height * 16);
        Font font = new Font("Comic Sans MS", Font.PLAIN, 22);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public String generateRandomString(int n) {
        //DONE: Generate random string of letters of length n
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i += 1) {
            stringBuilder.append(CHARACTERS[RandomUtils.uniform(rand, CHARACTERS.length)]);
        }

        return stringBuilder.toString();
    }

    public void drawFrame(String s) {
        //DONE: Take the string and display it in the center of the screen
        //DONE: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(width / 2.0, height / 2.0, s);
        headUpDisplay();
        StdDraw.show();
    }

    private void headUpDisplay() {
        double lineY = height - 2.5;
        double textY = height - 1.5;
        StdDraw.line(0, lineY, width, lineY);
        StdDraw.textLeft(2.0, textY, "Round: " + round);
        if (playerTurn) {
            StdDraw.text(width / 2.0, textY, "Type!");
        } else {
            StdDraw.text(width / 2.0, textY, "Watch!");
        }
        StdDraw.textRight(width - 2, textY, ENCOURAGEMENT[RandomUtils.uniform(rand, ENCOURAGEMENT.length)]);
    }

    public void flashSequence(String letters) {
        //DONE: Display each character in letters, making sure to blank the screen between letters
        for (char c : letters.toCharArray()) {
            drawFrame(Character.toString(c));
            StdDraw.clear(Color.BLACK);
            delay(1.0);
            drawFrame(" ");
            delay(0.5);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static void delay(double sec) {
        double startTime = getTime();
        while (getTime() - startTime < sec) {
        }
    }

    private static double getTime() {
        return LocalTime.now().getSecond() + LocalTime.now().getNano() / 1000000000.0;
    }

    public String solicitNCharsInput(int n) {
        //DONE: Read n letters of player input
        StringBuilder stringBuilder = new StringBuilder();
        playerTurn = true;
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                char typed = StdDraw.nextKeyTyped();
                stringBuilder.append(typed);
                drawFrame(Character.toString(typed));
                n -= 1;
            }
        }
        playerTurn = false;
        return stringBuilder.toString();
    }

    public void startGame() {
        //DONE: Set any relevant variables before the game starts
        String randomAnswer;
        String userAnswer;
        //DONE: Establish Engine loop
        while (!gameOver) {
            randomAnswer = generateRandomString(round);
            flashSequence(randomAnswer);
            userAnswer = solicitNCharsInput(round);
            if (userAnswer.equals(randomAnswer)) {
                delay(0.5);
                drawFrame("Congratulations!");
                delay(2.0);
                round += 1;
            } else {
                drawFrame("Game Over! You made it to round: " + round);
                gameOver = true;
            }
        }
    }

}
