import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int privateSize;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Node first;
    public Node last;

    // construct an empty deque
    public Deque() {
        first = last = null;
        privateSize = 0;
    }

    // return the number of items on the deque
    public int size() {
        return privateSize;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return privateSize == 0;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException();

        Node newNode = new Node();
        newNode.item = item;
        newNode.next = first;
        newNode.prev = null;

        if (first != null) first.prev = newNode;
        first = newNode;

        privateSize += 1;
        if (privateSize == 1) last = first;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException();

        Node newNode = new Node();
        newNode.item = item;
        newNode.next = null;
        newNode.prev = last;

        if (last != null) last.next = newNode;
        last = newNode;

        privateSize += 1;
        if (privateSize == 1) first = last;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (privateSize == 0 || first == null) throw new NoSuchElementException();

        Item valueToReturn = first.item;
        first = first.next;
        if (first != null) first.prev = null;

        privateSize -= 1;
        if (privateSize == 0 || first == null) last = first;

        return valueToReturn;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (privateSize == 0 || last == null) throw new NoSuchElementException();

        Item valueToReturn = last.item;

        last = last.prev;
        if (last != null) last.next = null;

        privateSize -= 1;
        if (privateSize == 0 || last == null) first = last;

        return valueToReturn;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (current == null) throw new NoSuchElementException();

                Item valueToReturn = current.item;
                current = current.next;

                return valueToReturn;
            }
        };
    }

    // unit testing
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();

        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        for (int item : deque) {
            StdOut.println(item);
        }

        StdOut.println();

        deque.removeLast();

        for (int item : deque) {
            StdOut.println(item);
        }

    }
}
