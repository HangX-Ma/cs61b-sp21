package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    /* The following two index both point to the nodes' space */
    private int front;
    private int tail;

    @SuppressWarnings("unchecked")
    private T[] items = (T[]) new Object[8];

    /**
     * Creates an empty linked list deque
     */
    public ArrayDeque() {
        size = 0;
        front = 0;
        tail = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        if (tail > front) {
            System.arraycopy(items, front, newItems, 0, size);
            front = 0;
            tail = size - 1;
        }

        if (tail < front) {
            System.arraycopy(items, 0, newItems, 0, tail + 1);
            System.arraycopy(items, front, newItems,
                    capacity - (items.length - front), items.length - front);
            front = capacity - (items.length - front);
        }

        items = newItems;
    }

    private int getIndex(int idx) {
        return (idx + items.length) % items.length;
    }

    private boolean isFull() {
        return size == items.length - 1;
    }

    /**
     * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        if (isFull()) {
            resize(size * 2);
        }
        front = getIndex(front - 1);
        items[front] = item;
        size += 1;
    }

    /**
     * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {
        if (isFull()) {
            resize(size * 2);
        }

        tail = getIndex(front + size);
        items[tail] = item;
        size += 1;
    }

    /**
     * Returns the number of items in the deque.
     */
    public int size() {
        return size;
    }


    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        for (int i = front; i < front + size; i++) {
            System.out.print(items[getIndex(i)]);
            if (i != front + size - 1) {
                System.out.print(" ");
            } else {
                System.out.print("\n");
            }
        }
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T item = items[front];
        items[front] = null; // garbage collection
        front = getIndex(front + 1); // move back
        size -= 1;

        if ((size < items.length / 4) && (size > 16)) {
            resize(items.length / 4);
        }

        return item;
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        tail = getIndex(front + size - 1); // move back
        T item = items[tail];
        items[tail] = null; // garbage collection
        size -= 1;

        if ((size < items.length / 4) && (size > 16)) {
            resize(items.length / 4);
        }

        return item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null.
     */
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        int idx = getIndex(front + index);
        return items[idx];
    }

    /**
     * The Deque objects we’ll make are iterable (i.e. Iterable<T>) so we must
     * provide this method to return an iterator.
     */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /**
     * Array iterator helper function
     */
    private class ArrayDequeIterator implements Iterator<T> {
        private int index;

        public ArrayDequeIterator() {
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            T item = get(index);
            index += 1;

            return item;
        }
    }


    /**
     * Returns whether the parameter o is equal to the Deque. o is considered equal
     * if it is a Deque and if it contains the same contents (as governed by
     * the generic T’s equals method) in the same order.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof ArrayDeque)) {
            return false;
        }

        ArrayDeque<?> ad = (ArrayDeque<?>) o;

        if (ad.size() != size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (this.get(i) != ad.get(i)) {
                return false;
            }
        }

        return true;
    }
}
