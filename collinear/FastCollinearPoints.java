package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private final ArrayList<PruLine> pruLines;

    public FastCollinearPoints(Point[] points) {

        // check inputs
        if (points == null) {
            throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor don't accept null array");
        }

        int pointsCount = points.length;
        PruPoint[] pruPoints = new PruPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor don't accept null points");
            }
            pruPoints[i] = new PruPoint(points[i]);
        }
        for (int i = 0; i < pointsCount; i++) {
            for (int j = i + 1; j < pointsCount; j++) {
                if (points[j].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor rejects duplicate points");
                }
            }
        }

        Arrays.sort(pruPoints);
        pruLines = new ArrayList<>();

        for (int i = 0; i < pointsCount - 3; i++) {
            PruPoint[] row = Arrays.copyOfRange(pruPoints, i, pointsCount);
//            System.out.println(" ");
//            System.out.println("started new row");
            linesFromRow(row);
        }

    }

    private void linesFromRow(PruPoint[] row) {

        PruPoint firstInRow = row[0];
        Arrays.sort(row, firstInRow.slopeOrder());

//        for (PruPoint pp: row) {
//            System.out.println(pp);
//        }

        ArrayList<PruPoint> currentLine = new ArrayList<>();
        currentLine.add(firstInRow);

        double lastSlope = firstInRow.slopeTo(row[1]);
        double currentSlope;

        for (int i = 1; i < row.length; i++) {
            currentSlope = firstInRow.slopeTo(row[i]);

//            System.out.println("examining point: " + row[i]);
//            System.out.println("curr slope: " + currentSlope);
//            System.out.println("last slope: " + lastSlope);

            if (Double.compare(currentSlope, lastSlope) == 0) {
                currentLine.add(row[i]);
                if (i == row.length - 1 && currentLine.size() >= 4) {
//                    System.out.println("adding at row end");
                    addLine(currentLine, lastSlope);
                }
            } else {
                if (currentLine.size() >= 4) {
                    addLine(currentLine, lastSlope);
                }
                currentLine = new ArrayList<>();
                currentLine.add(firstInRow);
                currentLine.add(row[i]);
            }

            lastSlope = currentSlope;
        }
    }

    private void addLine(ArrayList<PruPoint> addingPointList, double addingSlope) {
        PruPoint addingLastPoint = addingPointList.get(addingPointList.size() - 1);

        ArrayList<Double> endingPointSlopes = addingLastPoint.endingSlopes;

        boolean add = false;
        if (endingPointSlopes.isEmpty()) {
            add = true;
        } else {
            boolean unique = true;
            for (double endingPointSlope : endingPointSlopes) {
                if (Double.compare(endingPointSlope, addingSlope) == 0) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                add = true;
            }
        }
        if (add) {
            PruLine addingLine = new PruLine(addingPointList.get(0), addingLastPoint);
//            System.out.println("adding: " + addingLine.start + ", " + addingLine.end);
//            for (PruPoint ppp : addingPointList) {
//                System.out.println(ppp);
//            }
            this.pruLines.add(addingLine);
            addingLastPoint.endingSlopes.add(addingSlope);
        }
    }

    private static class PruPoint implements Comparable<PruPoint> {
        private final Point point;
        private final ArrayList<Double> endingSlopes;

        private PruPoint(Point point) {
            this.point = point;
            endingSlopes = new ArrayList<>();
        }

        public double slopeTo(PruPoint that) {
            return this.point.slopeTo(that.point);
        }

        public int compareTo(PruPoint that) {
            return this.point.compareTo(that.point);
        }

        public Comparator<PruPoint> slopeOrder() {
            return Comparator.comparingDouble(PruPoint.this::slopeTo);
        }

        public String toString() {
            return point.toString();
        }

    }

    private static class PruLine {
        private final PruPoint start;
        private final PruPoint end;

        private PruLine(PruPoint start, PruPoint end) {
            this.start = start;
            this.end = end;
        }

        private LineSegment toLineSegment() {
            return new LineSegment(start.point, end.point);
        }
    }

    public int numberOfSegments() {
        return pruLines.size();
    }

    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[pruLines.size()];
        for (int i = 0; i < pruLines.size(); i++) {
            lineSegments[i] = pruLines.get(i).toLineSegment();
        }
        return lineSegments;
    }


}
