package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    private static final double CONCERT_BASE = 440.0;

    private static double getConcert(int i) {
        return CONCERT_BASE * Math.pow(2, (double)(i - 24) / 12);
    }

    public static void main(String[] args) {
        /* create 37 guitar strings, for concert A and C */
        GuitarString[] GStr = new GuitarString[keyboard.length()];

        for (int i = 0; i < keyboard.length(); i++) {
            GStr[i] = new GuitarString(getConcert(i));
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int keyIndex = keyboard.indexOf(key);

                if (keyIndex > 0) {
                    GStr[keyIndex].pluck();
                }
            }

            double sample = 0.0;
            /* compute the superposition of samples */
            for (GuitarString guitarConcert: GStr) {
                sample += guitarConcert.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString guitarConcert: GStr) {
                guitarConcert.tic();
            }
        }
    }
}
