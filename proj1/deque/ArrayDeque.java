package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    /* The following two index both point to the nodes' space */
    private int front_idx;
    private int tail_idx;

    @SuppressWarnings("unchecked")
    private T[] items = (T[]) new Object[8];

    /** Creates an empty linked list deque */
    public ArrayDeque() {
        size = 0;
        front_idx = 0;
        tail_idx = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        T[] new_items = (T[]) new Object[capacity];
        if (tail_idx > front_idx) {
            System.arraycopy(items, front_idx, new_items, 0, size);
            front_idx = 0;
            tail_idx = size - 1;
        }

        if (tail_idx < front_idx) {
            System.arraycopy(items, 0, new_items, 0, tail_idx + 1);
            System.arraycopy(items, front_idx, new_items, capacity - (items.length - front_idx), items.length - front_idx);
            front_idx = capacity - (items.length - front_idx);
        }

        items = new_items;
    }

    private int getIndex(int idx) {
        return (idx + items.length) % items.length;
    }

    private boolean isFull() {
        return size == items.length - 1;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        if (isFull()) {
            resize(size * 2);
        }
        front_idx = getIndex(front_idx - 1);
        items[front_idx] = item;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        if (isFull()) {
            resize(size * 2);
        }

        tail_idx = getIndex(front_idx + size);
        items[tail_idx] = item;
        size += 1;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }


    /** Prints the items in the deque from first to last, separated by a space.
     *  Once all the items have been printed, print out a new line. */
    public void printDeque() {
        for (int i = front_idx; i < front_idx + size; i++) {
            System.out.print(items[getIndex(i)]);
            if (i != front_idx + size - 1) {
                System.out.print(" ");
            } else {
                System.out.print("\n");
            }
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T item = items[front_idx];
        items[front_idx] = null; // garbage collection
        front_idx = getIndex(front_idx + 1); // move back
        size -= 1;

        if ((size < items.length / 4) && (size > 16)) {
            resize(items.length / 4);
        }

        return item;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        tail_idx = getIndex(front_idx + size - 1); // move back
        T item = items[tail_idx];
        items[tail_idx] = null; // garbage collection
        size -= 1;

        if ((size < items.length / 4) && (size > 16)) {
            resize(items.length / 4);
        }

        return item;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null. */
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        int idx = getIndex(front_idx + index);
        return items[idx];
    }
    /** The Deque objects we’ll make are iterable (i.e. Iterable<T>) so we must
     * provide this method to return an iterator. */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /** Array iterator helper function */
    private class ArrayDequeIterator implements Iterator<T> {
        int index;

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


    /** Returns whether the parameter o is equal to the Deque. o is considered equal
     * if it is a Deque and if it contains the same contents (as governed by
     * the generic T’s equals method) in the same order. */
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

        ArrayDeque<?> _ad = (ArrayDeque<?>) o;

        if (_ad.size() != size) {
            return false;
        }

        for (int i = 0; i < size ; i++) {
            if (this.get(i) != _ad.get(i)) {
                return false;
            }
        }

        return true;
    }
}
