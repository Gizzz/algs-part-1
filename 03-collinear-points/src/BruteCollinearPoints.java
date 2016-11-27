import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segmentsList = new ArrayList<>();
    private int segmentsCount = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // argument checks

        if (points == null) throw new NullPointerException();

        for (Point p :  points) {
            if (p == null) throw new NullPointerException();
        }

        if (points.length > 1) {
            for (int i = 0; i < points.length; i++) {
                for (int j = i + 1; j < points.length; j++) {
                    if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
                }
            }
        }


        if (points.length <= 3) return;

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        double slope1 = points[i].slopeTo(points[j]);
                        double slope2 = points[i].slopeTo(points[k]);
                        double slope3 = points[i].slopeTo(points[l]);

                        if (slope1 == slope2 && slope2  == slope3) {
                            Point[] segmentPoints = new Point[] { points[i], points[j], points[k], points[l] };
                            Arrays.sort(segmentPoints);

                            segmentsList.add(new LineSegment(segmentPoints[0], segmentPoints[3]));
                            segmentsCount += 1;
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsCount;
    }

    // the line segments
    public LineSegment[] segments() {
        return segmentsList.toArray(new LineSegment[segmentsList.size()]);
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        Stopwatch sw = new Stopwatch();
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        double elapsedTime = sw.elapsedTime();

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        StdOut.println();
        StdOut.printf("N=%d elapsedTime=%f", n, elapsedTime);
    }
}
