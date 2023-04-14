package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        int a, b;
        int [] val = {4, 5, 6};
        BuggyAList<Integer> bugAList = new BuggyAList<>();
        AListNoResizing<Integer> AList = new AListNoResizing<>();

        for (int j : val) {
            bugAList.addLast(j);
            AList.addLast(j);
        }

        for (int i = 0; i < val.length; i++) {
            a = bugAList.removeLast();
            b = AList.removeLast();
            assertEquals(a, b);
        }
    }

    @Test
    public void randomizedTest() {
        int a, b;
        BuggyAList<Integer> bugAList = new BuggyAList<>();
        AListNoResizing<Integer> AList = new AListNoResizing<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                bugAList.addLast(randVal);
                AList.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = bugAList.size();
                System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                // remove last
                if (bugAList.size() > 0 && AList.size() > 0) {
                    a = bugAList.removeLast();
                    b = AList.removeLast();
                    assertEquals(a, b);
                }
            }
        }
    }
}
