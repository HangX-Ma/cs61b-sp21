package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {
    @Test
    public void randomArrayDequeTest() {
        StudentArrayDeque<Integer> stad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> stad2 = new ArrayDequeSolution<>();

        int M = 5000;
        StringBuilder str = new StringBuilder("\n");
        for (int i = 0; i < M; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                int randVal= StdRandom.uniform(0, 100);
                stad1.addFirst(randVal);
                stad2.addFirst(randVal);
                str.append("AddFirst(").append(randVal).append(")\n");
            } else if (operationNumber == 1) {
                int randVal= StdRandom.uniform(0, 100);
                stad1.addLast(randVal);
                stad2.addLast(randVal);
                str.append("AddLast(").append(randVal).append(")\n");
            }

            if (stad2.isEmpty()) {
                continue;
            }

            if (operationNumber == 2) {
                str.append("removeFirst()\n");
                assertEquals(String.valueOf(str), stad1.removeFirst(), stad2.removeFirst());
            } else if (operationNumber == 3) {
                str.append("removeLast()\n");
                assertEquals(String.valueOf(str), stad1.removeLast(), stad2.removeLast());
            }
        }

    }
}
