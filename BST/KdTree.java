package BST;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {

    private Node<Point2D> root;
    private int nodeCount;

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        PointsInRectSearch rectSearcher = new PointsInRectSearch(rect);
        rectSearcher.nodeAndChildrenInRect(root);

        return rectSearcher.inRectSet;
    }

    private static class PointsInRectSearch {

        RectHV rect;
        SET<Point2D> inRectSet;
        // rectangle
        double left;
        double right;
        double top;
        double bottom;

        private PointsInRectSearch(RectHV rect) {
            this.rect = rect;
            this.left = rect.xmin();
            this.right = rect.xmax();
            this.top = rect.ymax();
            this.bottom = rect.ymin();
            this.inRectSet = new SET<>();
        }

        private void nodeAndChildrenInRect(Node<Point2D> node) {
            if (node == null) {
                return;
            }
            Point2D point = node.point;

            if (rect.contains(point)) {
                inRectSet.add(point);
            }

            if (node.isVertical) {
                if (point.x() <= right && point.x() >= left) {
                    nodeAndChildrenInRect(node.leftChild);
                    nodeAndChildrenInRect(node.rightChild);
                } else if (point.x() > right) {
                    nodeAndChildrenInRect(node.leftChild);
                } else {
                    nodeAndChildrenInRect(node.rightChild);
                }
            } else {
                if (point.y() <= top && point.y() >= bottom) {
                    nodeAndChildrenInRect(node.leftChild);
                    nodeAndChildrenInRect(node.rightChild);
                } else if (point.y() > top) {
                    nodeAndChildrenInRect(node.leftChild);
                } else {
                    nodeAndChildrenInRect(node.rightChild);
                }
            }

        }

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        NearestSearch nearestSearch = new NearestSearch(p);
        RectHV canvas = new RectHV(0, 0, 1, 1);
        nearestSearch.distanceToNodeAndChildren(root, canvas);

        return nearestSearch.champ.point;
    }

    private static class NearestSearch {

        Node<Point2D> champ = null;
        double champDistance = Double.POSITIVE_INFINITY;
        Point2D queryPoint;
        double queryX;
        double queryY;

        private NearestSearch(Point2D queryPoint) {
            this.queryPoint = queryPoint;
            this.queryX = queryPoint.x();
            this.queryY = queryPoint.y();
        }

        private void distanceToNodeAndChildren(Node<Point2D> node, RectHV boundRect) {

            if (node == null) {
                return;
            }

//            System.out.println(" ");
//            System.out.println("examine: " + node.point);
//            System.out.println("rect: " + boundRect);
//
//            StdDraw.setPenRadius(0.015);
//            StdDraw.setPenColor(StdDraw.MAGENTA);
//            node.point.draw();
//            if (champ != null) {
//                StdDraw.setPenRadius(0.01);
//                StdDraw.setPenColor(StdDraw.YELLOW);
//                champ.point.draw();
//            }
//            StdDraw.show();
//            StdDraw.pause(3000);

            double currentDistance = node.point.distanceSquaredTo(queryPoint);
            double nodeX = node.point.x();
            double nodeY = node.point.y();

            if (currentDistance < champDistance) {
                champDistance = currentDistance;
                champ = node;
            }

            if (node.isVertical) {

                boolean queryIsLeft = queryX <= nodeX;

                if (queryIsLeft) {
                    distanceToNodeAndChildren(node.leftChild,
                            new RectHV(boundRect.xmin(), boundRect.ymin(), nodeX, boundRect.ymax()));
                } else {
                    distanceToNodeAndChildren(node.rightChild,
                            new RectHV(nodeX, boundRect.ymin(), boundRect.xmax(), boundRect.ymax()));
                }

                if (queryIsLeft) {
                    RectHV newBound = new RectHV(nodeX, boundRect.ymin(),
                            boundRect.xmax(), boundRect.ymax());
                    if (newBound.distanceSquaredTo(queryPoint) <= champDistance) {
                        distanceToNodeAndChildren(node.rightChild, newBound);
                    }
                } else {
                    RectHV newBound = new RectHV(boundRect.xmin(), boundRect.ymin(),
                            nodeX, boundRect.ymax());
                    if (newBound.distanceSquaredTo(queryPoint) <= champDistance) {
                        distanceToNodeAndChildren(node.leftChild, newBound);
                    }
                }

            } else {

                boolean queryIsBelow = queryY <= nodeY;
                if (queryIsBelow) {
                    distanceToNodeAndChildren(node.leftChild,
                            new RectHV(boundRect.xmin(), boundRect.ymin(), boundRect.xmax(), nodeY));
                } else {
                    distanceToNodeAndChildren(node.rightChild,
                            new RectHV(boundRect.xmin(), nodeY, boundRect.xmax(), boundRect.ymax()));
                }


                if (queryIsBelow) {
                    RectHV newBound = new RectHV(boundRect.xmin(), nodeY,
                            boundRect.xmax(), boundRect.ymax());
                    if (newBound.distanceSquaredTo(queryPoint) <= champDistance) {
                        distanceToNodeAndChildren(node.rightChild, newBound);
                    }
                } else {
                    RectHV newBound = new RectHV(boundRect.xmin(), boundRect.ymin(),
                            boundRect.xmax(), nodeY);
                    if (newBound.distanceSquaredTo(queryPoint) <= champDistance) {
                        distanceToNodeAndChildren(node.leftChild, newBound);
                    }
                }


            }
        }
    }

    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }
        root = updateNode(point, root, false);
    }

    private Node<Point2D> updateNode(Point2D point, Node<Point2D> node, boolean callerIsVertical) {
        if (node == null) {
            nodeCount++;
            return new Node<>(point, !callerIsVertical);
        }

        if (node.compareToPoint(point) < 0) {
            node.leftChild = updateNode(point, node.leftChild, node.isVertical);

        } else if (node.compareToPoint(point) > 0) {
            node.rightChild = updateNode(point, node.rightChild, node.isVertical);
        }

        return node;
    }

    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException();
        }
        return contains(point, root);
    }

    private boolean contains(Point2D p, Node<Point2D> node) {

        if (node == null) {
            return false;
        }
        if (node.point.equals(p)) {
            return true;
        }

        if (node.isVertical) {
            if (p.x() < node.point.x()) {
                return contains(p, node.leftChild);
            } else if (p.x() > node.point.x()) {
                return contains(p, node.rightChild);
            }
        } else {
            if (p.y() < node.point.y()) {
                return contains(p, node.leftChild);
            } else if (p.y() > node.point.y()) {
                return contains(p, node.rightChild);
            }
        }
        return (contains(p, node.leftChild) || contains(p, node.rightChild));
    }

    public int size() {
        return nodeCount;
    }

    public boolean isEmpty() {
        return root == null;
    }

    private void drawNodeAndChildren(Node<Point2D> node, double left, double right, double bottom, double top) {

        if (node != null) {
            StdDraw.setPenRadius(0.003);

            if (node.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), bottom, node.point.x(), top);
                drawNodeAndChildren(node.leftChild, left, node.point.x(), bottom, top);
                drawNodeAndChildren(node.rightChild, node.point.x(), right, bottom, top);

            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(left, node.point.y(), right, node.point.y());
                drawNodeAndChildren(node.leftChild, left, right, bottom, node.point.y());
                drawNodeAndChildren(node.rightChild, left, right, node.point.y(), top);
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.011);
            node.point.draw();


        }
    }

    public void draw() {
        drawNodeAndChildren(root, 0, 1, 0, 1);
    }

    private static class Node<P extends Point2D> {

        private final P point;
        private final boolean isVertical;
        private Node<P> leftChild;
        private Node<P> rightChild;

        private Node(P point, boolean isVertical) {
            this.point = point;
            this.isVertical = isVertical;
        }

        private int compareToPoint(P thatPoint) {
            if (point.equals(thatPoint)) {
                return 0;
            }
            if (isVertical) {
                if (thatPoint.x() < point.x()) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (thatPoint.y() < point.y()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        @Override
        public String toString() {
            return "Node{" + "point=" + point + ", isVertical=" + isVertical + ", leftChild=" + leftChild + ", rightChild=" + rightChild + '}';
        }
    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree tree = new KdTree();

//        Point2D[] points = {
//                new Point2D(0.5, 0.5),
//                new Point2D(0.3, 0.3),
//                new Point2D(0.6, 0.31),
//                new Point2D(0.1, 0.1),
//                new Point2D(0.2, 0.12)
//        };
//        for (Point2D point : points) {
//            tree.insert(point);
//        }

        // initialize the data structures from file
        String filename = "BST/input10.txt";
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }


        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(-0.01, 1);
        StdDraw.setYscale(-0.01, 1);

        StdDraw.enableDoubleBuffering();

        tree.draw();

        Point2D queryPoint = new Point2D(0.875, 0.125);

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.011);
        queryPoint.draw();

        System.out.println(tree.nearest(queryPoint));

        StdDraw.show();

    }

}

