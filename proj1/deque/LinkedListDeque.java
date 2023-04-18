package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private static class Node<T> {
        private final T item;
        private Node<T> next;
        private Node<T> prev;

        public Node(T item, Node<T> node) {
            this.item = item;
            this.prev = node;
            this.next = node;
        }
    }

    private int size;
    private final Node<T> sentinel;

    /** Creates an empty linked list deque */
    public LinkedListDeque() {
        sentinel = new Node<>(null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        Node<T> node = new Node<>(item, null);
        // link to the next node
        node.next = sentinel.next;
        sentinel.next.prev = node;
        // link to sentinel
        sentinel.next = node;
        node.prev = sentinel;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        Node<T> node = new Node<>(item, null);
        // link to the previous node
        sentinel.prev.next = node;
        node.prev = sentinel.prev;
        // link to sentinel
        node.next = sentinel;
        sentinel.prev = node;
        size += 1;
    }



    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     *  Once all the items have been printed, print out a new line. */
    public void printDeque() {
        Node<T> node = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(node.item);
            if (i != size - 1) {
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

        Node<T> firstNode = sentinel.next;
        firstNode.next.prev = sentinel;
        sentinel.next = firstNode.next;
        size -= 1;

        return firstNode.item;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        Node<T> lastNode = sentinel.prev;
        lastNode.prev.next = sentinel;
        sentinel.prev = lastNode.prev;
        size -= 1;

        return lastNode.item;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null. */
    public T get(int index) {
        if (index > size - 1 || index < 0 || isEmpty()) {
            return null;
        }

        Node<T> node = sentinel.next;
        for (int i = 0; i <= index; i++) {
            if (i == index) {
                return node.item;
            } else {
                node = node.next;
            }
        }

        throw new UnknownError();
    }

    /** The Deque objects we’ll make are iterable (i.e. Iterable<T>) so we must
     * provide this method to return an iterator. */
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> node;
        public LinkedListDequeIterator() {
            node = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return node != sentinel;
        }

        @Override
        public T next() {
            T item = node.item;
            node = node.next;
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

        if (o == this) {
            return true;
        }

        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        /* <?> means unverified cast */
        LinkedListDeque<?> list = (LinkedListDeque<?>) o;

        if (list.size() != this.size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (list.get(i) != this.get(i)) {
                return false;
            }
        }

        return true;
    }

    /** Same as get, but uses recursion. */
    public T getRecursive(int index) {
        if (index > size - 1 || index < 0 || isEmpty()) {
            return null;
        }

        return getRecursiveHelper(index, sentinel.next);
    }

    /** tail recursion */
    private T getRecursiveHelper(int index, Node<T> node) {
        if (index == 0) {
            return node.item;
        }

        return getRecursiveHelper(index - 1, node.next);
    }

}
