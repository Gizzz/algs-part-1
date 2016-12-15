import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.Queue;

public class PointSET {
    private SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();

        if (!set.contains(p)) set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();

        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();

        Queue<Point2D> queue = new Queue<>();

        for (Point2D item : set) {
            if (rect.contains(item)) {
                queue.enqueue(item);
            }
        }

        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();

        Point2D nearestPoint = null;

        for (Point2D item : set) {
            if (nearestPoint == null) {
                nearestPoint = item;
                continue;
            }

            if (p.distanceTo(item) < p.distanceTo(nearestPoint)) {
                nearestPoint = item;
            }
        }

        return nearestPoint;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
