import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segmentsList = new ArrayList<>();
    private int segmentsCount = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        checkPointsArray(points);

        if (points.length < 4) return;

        for (int i = 0; i < points.length; i++) {
            Point originPoint = points[i];
            Point[] otherPoints = new Point[points.length - 1];
            int otherPointsIndex = 0;

            for (int j = 0; j < points.length; j++) {
                if (points[j] != originPoint) {
                    otherPoints[otherPointsIndex] = points[j];
                    otherPointsIndex += 1;
                }
            }

            Arrays.sort(otherPoints, originPoint.slopeOrder());

            Double prevSlope = null;
            int sameSlopePointsCount = 0;

            for (int j = 0; j < otherPoints.length; j++) {
                Point currentPoint = otherPoints[j];
                double slope = originPoint.slopeTo(currentPoint);

                if (prevSlope == null || slope == prevSlope) {
                    sameSlopePointsCount += 1;

                    boolean lastPoint = (j == otherPoints.length - 1);
                    if (lastPoint && sameSlopePointsCount >= 3) {
                        createAndAddSegment(originPoint, otherPoints, j, sameSlopePointsCount);
                    }

                } else {
                    if (sameSlopePointsCount >= 3) {
                        createAndAddSegment(originPoint, otherPoints, j - 1, sameSlopePointsCount);
                    }

                    sameSlopePointsCount = 1;
                }

                prevSlope = slope;
            }
        }
    }

    private void checkPointsArray(Point[] points) {
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
    }

    private void createAndAddSegment(Point originPoint, Point[] otherPoints, int lastPointIndex, int sameSlopePointsCount) {
        Point[] segmentPoints = new Point[sameSlopePointsCount + 1];
        int segmentPointsIndex = 0;

        for (int k = lastPointIndex - (sameSlopePointsCount - 1); k <= lastPointIndex; k++) {
            segmentPoints[segmentPointsIndex] = otherPoints[k];
            segmentPointsIndex += 1;
        }

        segmentPoints[segmentPoints.length - 1] = originPoint;
        Arrays.sort(segmentPoints);

        boolean isSegmentExists = false;
        String segmentDirectString = new LineSegment(segmentPoints[0], segmentPoints[segmentPoints.length - 1]).toString();
        String segmentReverseString = new LineSegment(segmentPoints[segmentPoints.length - 1], segmentPoints[0]).toString();

        for (LineSegment currentSegment : segmentsList) {
            String currentSegmentString = currentSegment.toString();

            if (currentSegmentString.equals(segmentDirectString) || currentSegmentString.equals(segmentReverseString)) {
                isSegmentExists = true;
            }
        }

        if (!isSegmentExists) {
            segmentsList.add(new LineSegment(segmentPoints[0], segmentPoints[segmentPoints.length - 1]));
            segmentsCount += 1;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
