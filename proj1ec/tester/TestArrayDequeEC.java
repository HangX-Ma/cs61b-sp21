package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;

public class TestArrayDequeEC {
    @Test
    public void randomArrayDequeTest() {
        StudentArrayDeque<Integer> st = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();

        int M = 5000;
        StringBuilder str = new StringBuilder("\n");
        for (int i = 0; i < M; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                int randVal= StdRandom.uniform(0, 100);
                st.addFirst(randVal);
                sol.addFirst(randVal);
                str.append("AddFirst(").append(randVal).append(")\n");
                assertEquals(String.valueOf(str), sol.get(sol.size() - 1), st.get(st.size() - 1));
            } else if (operationNumber == 1) {
                int randVal= StdRandom.uniform(0, 100);
                st.addLast(randVal);
                sol.addLast(randVal);
                str.append("AddLast(").append(randVal).append(")\n");
                assertEquals(String.valueOf(str), sol.get(sol.size() - 1), st.get(st.size() - 1));
            }

            if (st.size() == 0) {
                assertEquals(st.isEmpty(), sol.isEmpty());
                continue;
            }

            if (operationNumber == 2 && st.size() > 0 && sol.size() > 0) {
                Integer expected = sol.removeFirst();
                Integer actual   = st.removeFirst();

                str.append("removeFirst()\n");

                assertEquals(String.valueOf(str), expected, actual);
            } else if (operationNumber == 3 && st.size() > 0 && sol.size() > 0) {
                Integer expected = sol.removeLast();
                Integer actual   = st.removeLast();

                str.append("removeLast()\n");

                assertEquals(String.valueOf(str), expected, actual);
            }
        }

    }
}
