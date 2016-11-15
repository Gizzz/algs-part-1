import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int privateSize;
    private int currentIndex;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        privateSize = 0;
        currentIndex = 0;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return privateSize == 0;
    }
    // return the number of items on the queue
    public int size() {
        return privateSize;
    }

    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];

        for (int i = 0; i < privateSize; i++) {

        }
    }

    // add the item
    public void enqueue(Item item) {
        if (currentIndex < items.length) {
            items[currentIndex] = item;
            currentIndex += 1;

            return;
        }

        resize(items.length * 2);

        items[currentIndex] = item;
        currentIndex += 1;

        return;
    }

    // remove and return a random item
    public Item dequeue() {
        return null;
    }

    // return (but do not remove) a random item
    public Item sample() {
        return null;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return null;
    }


    // unit testing
    public static void main(String[] args) {

    }
}
