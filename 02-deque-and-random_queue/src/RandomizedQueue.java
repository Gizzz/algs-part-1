import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int privateSize;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        privateSize = 0;
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
            newArray[i] = items[i];
        }

        items = newArray;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();

//        if (privateSize > items.length) {
//            throw new NullPointerException("privateSize should not be greater than items.length");
//        }

        if (privateSize == items.length) {
            resize(items.length * 2);
        }

        items[privateSize] = item;
        privateSize += 1;

        return;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int index = StdRandom.uniform(privateSize);

        Item valueToReturn = items[index];
        items[index] = items[privateSize - 1];
        items[privateSize - 1] = null;

        privateSize -= 1;
        if (privateSize > 0 && privateSize == (items.length / 4)) {
            resize(items.length / 4);
        }

        return valueToReturn;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        int index = StdRandom.uniform(privateSize);
        return items[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            int[] sequence = new int[privateSize];
            int currentIndex = 0;
            boolean isSequenceInitialized = false;

            private void initSequence() {
                for (int i = 0; i < privateSize; i++) {
                    sequence[i] = i;
                }

                StdRandom.shuffle(sequence);
                isSequenceInitialized = true;
            }

            public boolean hasNext() {
                if (privateSize == 0) return false;

                if (!isSequenceInitialized) initSequence();

                return currentIndex < sequence.length ? true : false;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();

                Item valueToReturn = items[ sequence[currentIndex] ];
                currentIndex += 1;

                return valueToReturn;
            }
        };
    }

    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomQueue = new RandomizedQueue<>();

        randomQueue.enqueue(1);
        randomQueue.enqueue(2);
        randomQueue.enqueue(3);

        for (int i = 0, count = randomQueue.size(); i < count; i++) {
            int a = randomQueue.dequeue();
            StdOut.println(a);
        }

//        for (int item : randomQueue) {
//            StdOut.println(item);
//        }
    }
}
