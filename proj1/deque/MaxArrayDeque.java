package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;

    /** Creates a MaxArrayDeque with the given Comparator. */
    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    /** returns the maximum element in the deque as governed by the previously
     *  given Comparator. If the MaxArrayDeque is empty, simply return null */
    public T max() {
        return max(comparator);
    }

    /** Returns the maximum element in the deque as governed by the parameter
     * Comparator c. If the MaxArrayDeque is empty, simply return null. */
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (c.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }

        return get(maxIndex);
    }
}
