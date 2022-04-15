package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class PruCollinearPoints {

    private Point[] points;
    private LineSegment[] lineSegments;
    private int segmentsCount;
    private final int pointsCount;

    // finds all line segments containing 4 points
    public PruCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("collinear.BruteCollinearPoints constructor don't accept null input");
        }
        this.points = points;
        pointsCount = points.length;
        segmentsCount = 0;
        extractSegments(points);

    }

    // returns [x, y]
    private static int[] getPointXY(Point point) {
        String stringPoint = point.toString();
        stringPoint = stringPoint.substring(1, stringPoint.length() - 1);
        return Arrays.stream(stringPoint.split(", ")).mapToInt(Integer::parseInt).toArray();
    }

    private class SegmentByID {

        private final int pointID1;
        private final int pointID2;
        private final double slope;
        private final double c;
        private HashSet<Integer> pointsIDSet;

        private SegmentByID(int pointID1, int pointID2) {
            this.pointID1 = pointID1;
            this.pointID2 = pointID2;
            this.pointsIDSet = new HashSet<>();
            pointsIDSet.add(pointID1);
            pointsIDSet.add(pointID2);
            this.slope = points[pointID1].slopeTo(points[pointID2]);
            if (slope == Double.NEGATIVE_INFINITY) {
                this.c = Double.NEGATIVE_INFINITY;
            } else {
                int[] xy = getPointXY(points[pointID1]);
                int x = xy[0];
                int y = xy[1];
                if (slope == Double.POSITIVE_INFINITY) {
                    this.c = x;
                } else {
                    this.c = y - slope * x;
                }
            } // set c
        }

        private Point[] GetPoints() {
            return new Point[]{points[pointID1], points[pointID2]};
        }

        private void print() {
            System.out.println("seg: " + pointID1 + ", " + pointID2);
        }
    }

    private SegmentByID joinSegments(SegmentByID s1, SegmentByID s2) {
        if (s1.slope == s2.slope && s1.c == s2.c) {

            int maxID = -1;
            int minID = -1;

            Point minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
            Point maxPoint = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
            for (SegmentByID segment : new SegmentByID[]{s1, s2}) {
                for (int id : new int[]{segment.pointID1, segment.pointID2}) {
                    int com2max = points[id].compareTo(maxPoint);
                    int com2min = points[id].compareTo(minPoint);
                    if (com2max >= 0) {
                        maxID = id;
                        maxPoint = points[id];
                    }
                    if (com2min <= 0) {
                        minID = id;
                        minPoint = points[id];
                    }
                }
            }
            SegmentByID joinedSegment = new SegmentByID(minID, maxID);
            joinedSegment.pointsIDSet.addAll(s1.pointsIDSet);
            joinedSegment.pointsIDSet.addAll(s2.pointsIDSet);

            return joinedSegment;

        } else {
            return null;
        }
    }

    private void extractSegments(Point[] points) {

        HashMap<Double, ArrayList<SegmentByID>> slopesMap = new HashMap<>();
        for (int i = 0; i < pointsCount; i++) {
            for (int j = i + 1; j < pointsCount; j++) {

                SegmentByID idSegment = new SegmentByID(i, j);
                double slope = idSegment.slope;

                System.out.println("------------------");
                System.out.println("slope: " + slope);

                ArrayList<SegmentByID> segmentList = slopesMap.get(slope);
                if (segmentList == null) {
                    segmentList = new ArrayList<SegmentByID>();
                    segmentList.add(idSegment);
                    slopesMap.put(slope, segmentList);
                    System.out.println("inserted in new list:");
                    idSegment.print();
                } else {
                    boolean unique = true;
                    for (int k = 0; k < segmentList.size(); k++) {
                        System.out.println("segList size before: " + segmentList.size());
                        System.out.println("segment to be put:");
                        idSegment.print();
                        System.out.println("segment in list:");
                        segmentList.get(k).print();
                        SegmentByID joinedSegment = joinSegments(segmentList.get(k), idSegment);
                        System.out.println("joined:");

                        if (joinedSegment != null) {
                            joinedSegment.print();
                            segmentList.set(k, joinedSegment);
                            System.out.println("segList size: " + segmentList.size());
                            unique = false;
                        } else {
                            System.out.println("null");
                        }
                    }
                    if (unique) {
                        System.out.println("adding unique");
                        segmentList.add(idSegment);
                    }
                }

            }
        }

        System.out.println("===================================");
        for (double slope : slopesMap.keySet()) {
            System.out.println("slope: " + slope);
            ArrayList<SegmentByID> segmentList = slopesMap.get(slope);

            for (SegmentByID segment : segmentList) {
                segment.print();
                System.out.println("n points: " + segment.pointsIDSet.size());
            }


        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsCount;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments;
    }

}
