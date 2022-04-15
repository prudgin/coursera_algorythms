package collinear;

import java.util.ArrayList;
import java.util.Arrays;


public class BruteCollinearPoints {

    private final ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        // check inputs
        if (points == null) {
            throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor don't accept null array");
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor don't accept null points");
            }
        }

        int pointsCount = points.length;
        this.lineSegments = new ArrayList<>();


        for (int i = 0; i < pointsCount; i++) {


            for (int j = i + 1; j < pointsCount; j++) {
                if (points[j].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor rejects duplicate points");
                }


                for (int k = j + 1; k < pointsCount; k++) {
                    if (points[k].compareTo(points[j]) == 0 ||
                            points[k].compareTo(points[i]) == 0) {
                        throw new IllegalArgumentException(
                                "collinear.BruteCollinearPoints constructor rejects duplicate points");
                    }
                    if (points[i].slopeOrder().compare(points[j], points[k]) == 0) {


                        for (int n = k + 1; n < pointsCount; n++) {
                            if (points[n].compareTo(points[k]) == 0 ||
                                    points[n].compareTo(points[j]) == 0 ||
                                    points[n].compareTo(points[i]) == 0) {
                                throw new IllegalArgumentException(
                                        "collinear.BruteCollinearPoints constructor rejects duplicate points");
                            }
                            if (points[i].slopeOrder().compare(points[j], points[n]) == 0) {
                                Point[] inlinePoints = {points[i], points[j], points[k], points[n]};
                                Arrays.sort(inlinePoints);
                                lineSegments.add(
                                        new LineSegment(inlinePoints[0], inlinePoints[inlinePoints.length - 1]));
                            }
                        }
                    }
                }

            }
        }


    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }


}

