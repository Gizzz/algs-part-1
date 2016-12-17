import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.Queue;

public class KdTree {
    private static class Node {
        private Point2D point;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lbNode;        // the left/bottom subtree
        private Node rtNode;        // the right/top subtree
    }
    
    private enum Orientation {
        vertical,
        horizontal
    }

    private Node rootNode;
    private int size;
    private Point2D closestPoint;

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

        if (isEmpty()) {
            Node newNode = new Node();
            newNode.point = p;
            newNode.rect = new RectHV(0, 0, 1, 1);

            rootNode = newNode;
            size += 1;

            return;
        }

        Node currentNode = rootNode;
        boolean is_currentNode_horizontal = false;
        boolean is_newNode_leftBottom;

        while (true) {
            double targetPointCoord;
            double currentPointCoord;

            if (is_currentNode_horizontal) {
                targetPointCoord = p.y();
                currentPointCoord = currentNode.point.y();
            } else {
                targetPointCoord = p.x();
                currentPointCoord = currentNode.point.x();
            }

            if (targetPointCoord < currentPointCoord) {
                if (currentNode.lbNode == null) {
                    is_newNode_leftBottom = true;
                    break;
                }

                currentNode = currentNode.lbNode;
            } else {
                if (currentNode.rtNode == null) {
                    is_newNode_leftBottom = false;
                    break;
                }

                currentNode = currentNode.rtNode;
            }

            is_currentNode_horizontal = !is_currentNode_horizontal;
        }

        boolean is_newNode_horizontal = !is_currentNode_horizontal;

        Node newNode = new Node();
        newNode.point = p;

        if (is_newNode_horizontal) {
            if (is_newNode_leftBottom) {
                newNode.rect = new RectHV(
                        currentNode.rect.xmin(), currentNode.rect.ymin(),
                        currentNode.point.x(), currentNode.rect.ymax()
                );
            } else {
                newNode.rect = new RectHV(
                        currentNode.point.x(), currentNode.rect.ymin(),
                        currentNode.rect.xmax(), currentNode.rect.ymax()
                );
            }
        } else /* if new node vertical */ {
            if (is_newNode_leftBottom) {
                newNode.rect = new RectHV(
                        currentNode.rect.xmin(), currentNode.rect.ymin(),
                        currentNode.rect.xmax(), currentNode.point.y()
                );
            } else {
                newNode.rect = new RectHV(
                        currentNode.rect.xmin(), currentNode.point.y(),
                        currentNode.rect.xmax(), currentNode.rect.ymax()
                );
            }
        }

        if (is_newNode_leftBottom) {
            currentNode.lbNode = newNode;
        } else {
            currentNode.rtNode = newNode;
        }

        size += 1;
    }

    // does the set contain point point?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();

        if (rootNode == null) return false;

        Node currentNode = rootNode;
        boolean is_currentNode_horizontal = false;

        while (currentNode != null) {
            double targetPointCoord;
            double currentPointCoord;

            if (is_currentNode_horizontal) {
                targetPointCoord = p.y();
                currentPointCoord = currentNode.point.y();
            } else {
                targetPointCoord = p.x();
                currentPointCoord = currentNode.point.x();
            }

            if ( currentNode.point.toString().equals(p.toString()) ) return true;

            if (targetPointCoord < currentPointCoord) {
                currentNode = currentNode.lbNode;
            } else {
                currentNode = currentNode.rtNode;
            }

            is_currentNode_horizontal = !is_currentNode_horizontal;
        }

        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(rootNode, Orientation.vertical);
    }

    private void draw(Node currentNode, Orientation orientation) {
        if (currentNode == null) return;

        if (orientation == Orientation.vertical) {
            // draw vertical line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(
                    currentNode.point.x(), currentNode.rect.ymin(), 
                    currentNode.point.x(), currentNode.rect.ymax()
            );
        } else {
            // draw horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(
                    currentNode.rect.xmin(), currentNode.point.y(), 
                    currentNode.rect.xmax(), currentNode.point.y()
            );
        }

        //draw point
        StdDraw.setPenColor();
        StdDraw.setPenRadius(0.01);
        currentNode.point.draw();
        
        Orientation childOrientation = orientation == Orientation.vertical 
                                        ? Orientation.horizontal 
                                        : Orientation.vertical;
        
        draw(currentNode.lbNode, childOrientation);
        draw(currentNode.rtNode, childOrientation);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();

        Queue<Point2D> queue = new Queue<>();
        rangeSearch(rootNode, rect, queue);

        return queue;
    }

    private void rangeSearch(Node node, RectHV rect, Queue<Point2D> queue) {
        if (node == null) return;

        if (node.rect.intersects(rect)) {
            if (rect.contains(node.point)) {
                queue.enqueue(node.point);
            }

            rangeSearch(node.lbNode, rect, queue);
            rangeSearch(node.rtNode, rect, queue);
        }

        return;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();

        if (rootNode == null) return null;
        if (size == 1) return rootNode.point;

        closestPoint = rootNode.point;
        nearestSearch(rootNode, p);

        return closestPoint;
    }

    private void nearestSearch(Node currentNode, Point2D targetPoint) {
        if (currentNode == null) return;

        if (currentNode.rect.distanceSquaredTo(targetPoint) > closestPoint.distanceSquaredTo(targetPoint)) {
            return;
        }

        if (currentNode.point.distanceSquaredTo(targetPoint) < closestPoint.distanceSquaredTo(targetPoint)) {
            closestPoint = currentNode.point;
        }

        boolean isLbSideContainsPoint = false;

        if (currentNode.lbNode != null) {
            isLbSideContainsPoint = currentNode.lbNode.rect.contains(targetPoint);
        }

        if (isLbSideContainsPoint) {
            nearestSearch(currentNode.lbNode, targetPoint);
            nearestSearch(currentNode.rtNode, targetPoint);
        } else {
            nearestSearch(currentNode.rtNode, targetPoint);
            nearestSearch(currentNode.lbNode, targetPoint);
        }
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        // my tree

//        kdTree.insert(new Point2D(.5, .5));
//
//        kdTree.insert(new Point2D(.25, .25));
//        kdTree.insert(new Point2D(.75, .75));
//
//        kdTree.insert(new Point2D(.125, .125));
//        kdTree.insert(new Point2D(.375, .375));
//        kdTree.insert(new Point2D(.625, .625));
//        kdTree.insert(new Point2D(.875, .875));


        // example from spec

//        kdTree.insert(new Point2D(.7, .2));
//        kdTree.insert(new Point2D(.5, .4));
//        kdTree.insert(new Point2D(.2, .3));
//        kdTree.insert(new Point2D(.4, .7));
//        kdTree.insert(new Point2D(.9, .6));


        // cirlcle
        kdTree.insert(new Point2D(.0, .5));
        kdTree.insert(new Point2D(.5, 1));
        kdTree.insert(new Point2D(.5, .0));
        kdTree.insert(new Point2D(1, .5));



        kdTree.draw();
        StdDraw.show();

        Point2D point = new Point2D(.5, .1);
        StdOut.println(kdTree.nearest(point));
    }
}
