import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        private Point2D point;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lbNode;        // the left/bottom subtree
        private Node rtNode;        // the right/top subtree
    }

    private Node rootNode;
    private int size;

    // construct an empty set of points
    public KdTree() {
        rootNode = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();

        if (contains(p)) return;

        Node newNode = new Node();
        newNode.point = p;

        if (isEmpty()) {
            rootNode = newNode;
            size += 1;

            return;
        }

        Node currentNode = rootNode;
        boolean isHorizontal = false;
        boolean isLeftBottom;

        while (true) {
            double targetPointCoord;
            double currentPointCoord;

            if (isHorizontal) {
                targetPointCoord = p.y();
                currentPointCoord = currentNode.point.y();
            } else {
                targetPointCoord = p.x();
                currentPointCoord = currentNode.point.x();
            }

            if (targetPointCoord < currentPointCoord) {
                if (currentNode.lbNode == null) {
                    isLeftBottom = true;
                    break;
                }

                currentNode = currentNode.lbNode;
            } else {
                if (currentNode.rtNode == null) {
                    isLeftBottom = false;
                    break;
                }

                currentNode = currentNode.rtNode;
            }

            isHorizontal = !isHorizontal;
        }

        if (isLeftBottom) {
            currentNode.lbNode = newNode;
        } else {
            currentNode.rtNode = newNode;
        }

        size += 1;
    }

    // does the set contain point point?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();

        return false;
    }

    // draw all points to standard draw
    public void draw() {
//        StdDraw.setPenColor(StdDraw.BLACK);
//        StdDraw.setPenRadius(0.01);
//        StdDraw.line(.0, .0, 1, 1);
//        rootNode.point.draw();

        if (rootNode == null) return;

        Node currentNode = rootNode;

        // draw vertical line
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(currentNode.point.x(), 1, currentNode.point.x(), 0);

        //draw point
        StdDraw.setPenColor();
        StdDraw.setPenRadius(0.01);
        currentNode.point.draw();

        if (currentNode.lbNode != null) {
            Node childNode = currentNode.lbNode;

            // draw horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(0, childNode.point.y(), currentNode.point.x(), childNode.point.y());

            //draw point
            StdDraw.setPenColor();
            StdDraw.setPenRadius(0.01);
            childNode.point.draw();
        }

        if (currentNode.rtNode != null) {
            Node childNode = currentNode.rtNode;

            // draw horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(currentNode.point.x(), childNode.point.y(), 1, childNode.point.y());

            //draw point
            StdDraw.setPenColor();
            StdDraw.setPenRadius(0.01);
            childNode.point.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();

        return null;
    }

    // a nearest neighbor in the set to point point; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();

        return null;
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        KdTree kdTree = new KdTree();

        kdTree.insert(new Point2D(.5, .5));
        kdTree.insert(new Point2D(.25, .25));
        kdTree.insert(new Point2D(.75, .75));
        kdTree.draw();
        StdDraw.show();

//        while (true) {
//            if (StdDraw.mousePressed()) {
//                double x = StdDraw.mouseX();
//                double y = StdDraw.mouseY();
//                StdOut.printf("%8.6f %8.6f\n", x, y);
//                Point2D point = new Point2D(x, y);
//
//                if (rect.contains(point)) {
//                    StdOut.printf("%8.6f %8.6f\n", x, y);
//                    kdTree.insert(point);
//                    StdDraw.clear();
//                    kdTree.draw();
//                    StdDraw.show();
//                }
//            }
//
//            StdDraw.pause(50);
//        }

    }
}
