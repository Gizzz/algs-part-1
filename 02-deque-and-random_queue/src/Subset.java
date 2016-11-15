import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> randomQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            randomQueue.enqueue(StdIn.readString());
        }

//        if (k < 0 || k > randomQueue.size()) {
//            throw new IllegalArgumentException();
//        }

        for (int i = 0; i < k; i++) {
            StdOut.println(randomQueue.dequeue());
        }
    }
}
