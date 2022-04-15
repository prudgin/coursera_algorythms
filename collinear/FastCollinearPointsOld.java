package collinear;

import java.util.ArrayList;
import java.util.Arrays;


public class FastCollinearPointsOld {

    private final ArrayList<LineSegment> lineSegments;

    // finds all line segments containing 4 points
    public FastCollinearPointsOld(Point[] points) {

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
        ArrayList<PointSegment> pointSegments = new ArrayList<>();

        for (int i = 0; i < pointsCount; i++) {
            for (int j = i + 1; j < pointsCount; j++) {
                if (points[j].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor rejects duplicate points");
                }
            }
        }

        for (int i = 0; i < pointsCount - 3; i++) {
//            System.out.println(" ");
//            System.out.println("----------------");
//            System.out.println("examine point: " + points[i].toString());

            Point[] sortedPoints = Arrays.copyOfRange(points, i, pointsCount);

            Arrays.sort(sortedPoints, points[i].slopeOrder());
//            System.out.println("slope sorted: " + Arrays.toString(sortedPoints));

            double prevSlope = sortedPoints[0].slopeTo(sortedPoints[1]);
            int pointsInLineCount = 2;

            for (int j = 2; j < sortedPoints.length; j++) {
                PointSegment newSegment = null;
//                System.out.println("examine point: " + sortedPoints[j].toString());

                double currentSlope = sortedPoints[0].slopeTo(sortedPoints[j]);
//                System.out.println("current slope: " + currentSlope + "; prev slope: " + prevSlope);


                if (Double.compare(currentSlope, prevSlope) == 0) {
                    pointsInLineCount++;
//                    System.out.println("found next inline, line count is now: " + pointsInLineCount);

                    if (pointsInLineCount >= 4) {

                        if (j == sortedPoints.length - 1) {
//                            System.out.println("reached end, line count is now: " + pointsInLineCount);
                            Point[] inlinePoints;
                            inlinePoints = Arrays.copyOfRange(sortedPoints, j - pointsInLineCount + 1, j + 1);
                            inlinePoints[0] = sortedPoints[0];
//                            System.out.println("inlined points: " + Arrays.toString(inlinePoints));
                            Arrays.sort(inlinePoints);
//                            System.out.println("inlined points sorted: " + Arrays.toString(inlinePoints));
                            newSegment =
                                    new PointSegment(inlinePoints[0], inlinePoints[inlinePoints.length - 1]);
//                            collinear.LineSegment newLine = new collinear.LineSegment(inlinePoints[0], inlinePoints[inlinePoints.length - 1]);
//                            System.out.println("==added new line: " + newLine.toString());
                        }


                    }

                } else {
                    if (pointsInLineCount >= 4) {
                        Point[] inlinePoints;
                        inlinePoints = Arrays.copyOfRange(sortedPoints, j - pointsInLineCount, j);
                        inlinePoints[0] = sortedPoints[0];
//                        System.out.println("inlined points: " + Arrays.toString(inlinePoints));
                        Arrays.sort(inlinePoints);
//                        System.out.println("inlined points sorted: " + Arrays.toString(inlinePoints));
                        newSegment =
                                new PointSegment(inlinePoints[0], inlinePoints[inlinePoints.length - 1]);
//                        collinear.LineSegment newLine = new collinear.LineSegment(inlinePoints[0], inlinePoints[inlinePoints.length - 1]);
//                        System.out.println("==added new line: " + newLine.toString());
                    }
                    pointsInLineCount = 2;
                    prevSlope = currentSlope;
                }


                if (newSegment != null) {
//                    System.out.println("=======================");
//                    System.out.println("got new segment: " + newSegment.p1.toString() + ", " + newSegment.p2.toString());
                    if (!pointSegments.isEmpty()) {
                        boolean unique = true;

                        for (int ii = 0; ii < pointSegments.size(); ii++) {
                            PointSegment addSegment = joinSegments(newSegment, pointSegments.get(ii));
                            if (addSegment != null) {
                                pointSegments.set(ii, addSegment);
//                                System.out.println("inserted: " + addSegment.p1.toString() +
//                                        " " + addSegment.p2.toString());
                                unique = false;
                                break;
                            }
                        }
                        if (unique) {
                            pointSegments.add(newSegment);
//                            System.out.println("added unique: " + newSegment.p1.toString() +
//                                    " " + newSegment.p2.toString());
                        }

                    } else {
                        pointSegments.add(newSegment);
//                        System.out.println("added to empty: " + newSegment.p1.toString() +
//                                " " + newSegment.p2.toString());
                    }
                }

            }
        }

        for (PointSegment seg : pointSegments) {
            lineSegments.add(new LineSegment(seg.p1, seg.p2));
        }

    }


    private static class PointSegment {
        private final Point p1;
        private final Point p2;

        private PointSegment(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }


    }

    private PointSegment joinSegments(PointSegment s1, PointSegment s2) {
//        System.out.println("joining: " + s1.p1.toString() + " " + s1.p2.toString() +
//                " with " + s2.p1.toString() + " " + s2.p2.toString());

        double s1slope = s1.p1.slopeTo(s1.p2);
        double s2slope = s2.p1.slopeTo(s2.p2);
        double p1slope = s1.p1.slopeTo(s2.p1);
        double p1p2slope = s1.p1.slopeTo(s2.p2);

//        System.out.println("s1 slope: " + s1slope);
//        System.out.println("s2 slope: " + s2slope);


        if ((Double.compare(s1slope, s2slope) == 0) &&
                (Double.compare(s1slope, p1slope) == 0 || Double.compare(s1slope, p1p2slope) == 0)) {

            Point[] pts = {s1.p1, s1.p2, s2.p1, s2.p2};
            Arrays.sort(pts);
//            System.out.println("newly merged: " + pts[0].toString() + " " + pts[3].toString());
            return new PointSegment(pts[0], pts[3]);
        }

        return null;
    }


    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }


}

