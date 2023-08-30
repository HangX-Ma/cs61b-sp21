package deque;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class MaxArrayDequeTest {
    @Test
    public void maxIdenticalIntegerTest() {
        MaxArrayDeque<Integer> maxDeque = new MaxArrayDeque<>(new IntComparator());

        maxDeque.addFirst(2);
        maxDeque.addFirst(2);
        maxDeque.addLast(2);

        Assert.assertEquals((Integer) 2, maxDeque.max());
    }

    @Test
    public void maxDiffIntegerTest() {
        MaxArrayDeque<Integer> maxDeque = new MaxArrayDeque<>(new IntComparator());

        maxDeque.addFirst(0);
        maxDeque.addFirst(1);
        maxDeque.addLast(2);

        Assert.assertEquals((Integer) 2, maxDeque.max());
    }

    @Test
    public void maxWithComparatorTest() {
        MaxArrayDeque<String> arrayDeque = new MaxArrayDeque<>(new StringComparator());

        arrayDeque.addLast("this is a test");
        arrayDeque.addLast("This is another test");

        Assert.assertEquals("this is a test", arrayDeque.max());
        Assert.assertEquals("This is another test", arrayDeque.max(new StringLenComparator()));
    }

    private static class IntComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            int len1 = a.length();
            int len2 = b.length();

            for (int i = 0; i < Math.min(len1, len2); i++) {
                int ch1 = a.charAt(i);
                int ch2 = b.charAt(i);

                if (ch1 != ch2) {
                    return ch1 - ch2;
                }

                if (len1 != len2) {
                    return len1 - len2;
                }
            }

            return 0;
        }
    }

    private static class StringLenComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }
}
