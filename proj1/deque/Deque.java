package deque;

public interface Deque<T> {
    void addFirst(T item);
    void addLast(T item);

    /** Returns true if deque is empty, false otherwise. */
    default boolean isEmpty() {
        return size() == 0;
    }

    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);
    boolean equals(Object o);
}
