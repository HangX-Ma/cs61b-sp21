package deque;

public class Node<T> {
    public T item;
    public Node<T> next;
    public Node<T> prev;

    public Node(T item, Node<T> node) {
        this.item = item;
        this.prev = node;
        this.next = node;
    }
}
