package BST;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;


public class PointSET {

    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        this.pointSet = new SET<>();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        SET<Point2D> rangeSet = new SET<>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                rangeSet.add(new Point2D(point.x(), point.y()));
            }
        }
        return rangeSet;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D champ = null;
        Double champDistance = null;
        for (Point2D point : pointSet) {
            double pDistance = point.distanceSquaredTo(p);
            if ((p != point) && (champ == null || pDistance < champDistance)) {
                champ = point;
                champDistance = pDistance;
            }
        }
        return champ;
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointSet) {
            point.draw();
        }
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {

        // write to a file
//        Out out;
//        out = new Out("pointsTest.txt");
//        for (int i = 0; i < 100; i++) {
//            double x = StdRandom.uniform(0d, 1d);
//            double y = StdRandom.uniform(0d, 1d);
//            out.println(x + " " + y);
//        }
//        out.close();

        // initialize the data structures from file
        String filename = "BST/pointsTest.txt";
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        StdDraw.setCanvasSize(400, 400);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenRadius(0.008);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.enableDoubleBuffering();

        brute.draw();

        StdDraw.show();

        System.out.println(brute.nearest(new Point2D(0, 0)));
    }


}
