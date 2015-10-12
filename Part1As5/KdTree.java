import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinkedBag;

public class KdTree {
    
    private int size;
    private int count;  // count for debug
    private Node root;
    private LinkedBag<Point2D> list;
    private double minDistance;
    private Point2D nearestPoint = null;
    private Point2D target = null;
    
    private static class Node {
        private Point2D p;      // the point
        private Point2D min;
        private Point2D max;
//        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node left;        // the left/bottom subtree
        private Node right;        // the right/top subtree
        private boolean isVer;
        
        private Node(Point2D point, boolean isVer, Point2D min, Point2D max) {
            p = point;
            this.isVer = isVer;
            this.min = min;
            this.max = max;
        }
        
        private Point2D point() {
            return p;
        }
        
        private Point2D min() {
            return min;
        }
        
        private Point2D max() {
            return max;
        }
        
        private boolean isVer() {
            return isVer;
        }        
     }
    
    public KdTree() {
        root = null;
        size = 0;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    // helper function for insert
    private Node insert(Node node , Point2D p, boolean isVer, Point2D min, Point2D max) {
        if (node == null) {
            size++;
            return new Node(p, isVer, min, max);
        }
            
        if (node.point().equals(p))
            return node;
        
        if (isVer) {
            if (p.x() < node.point().x()) {
                node.left = insert(node.left, p, false, min, new Point2D(node.point().x(), max.y()));
            } else {
                node.right = insert(node.right, p, false, new Point2D(node.point().x(), min.y()), max);
            }
        } else {
            if (p.y() < node.point().y()) {
                node.left = insert(node.left, p, true, min, new Point2D(max.x(), node.point().y()));
            } else {
                node.right = insert(node.right, p, true, new Point2D(min.x(), node.point().y()), max);
            }
        }
        return node;
    }
    
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        root = insert(root, p, true, new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
    }
    
    
    // helper function for contains
    private boolean find(Node node, Point2D p, boolean isVer) {
        if (node == null)
            return false;
        if (node.point().equals(p))
            return true;
        if (isVer) {
            if (p.x() < node.point().x()) {
                return find(node.left, p, false);
            } else {
                return find(node.right, p, false);
            }
        } else {
            if (p.y() < node.point().y()) {
                return find(node.left, p, true);
            } else {
                return find(node.right, p, true);
            }
        }
        
        
    }
    
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();
       return find(root, p, true);
    }
    
    
    // helper function for draw
    private void drawhelper(Node node) {
        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        node.point().draw();
        
        StdDraw.setPenRadius();
        if (node.isVer) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point().x(), node.min().y(), node.point().x(), node.max().y());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.min().x(), node.point().y(), node.max().x(), node.point().y());  
        }
        drawhelper(node.left);
        drawhelper(node.right);  
    }
    
    public void draw() {
        drawhelper(root);
    }
    
    // helper function for range
    private void searchRange(Node node, RectHV rect) {
        if (node == null)
            return;
        if (rect.contains(node.point()))
            list.add(node.point());
        if (node.isVer()) {
            if (node.point().x() >= rect.xmin()) {
                searchRange(node.left, rect);
            } 
            if (node.point().x() <= rect.xmax()) {
                searchRange(node.right, rect);
            }
        } else {
            if (node.point().y() >= rect.ymin()) {
                searchRange(node.left, rect);
            } 
            if (node.point().y() <= rect.ymax()) {
                searchRange(node.right, rect);
            }
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        list = new LinkedBag<Point2D>();
        searchRange(root, rect);
        return list;
    }
    
    private double distanceSquared(Node node) {
        double dx = 0.0, dy = 0.0;
        if (node.isVer()) {
            if      (target.y() < node.min().y()) dy = target.y() - node.min().y();
            else if (target.y() > node.max().y()) dy = target.y() - node.max().y();
            dx = target.x() - node.point().x();
        } else {
            if      (target.x() < node.min().x()) dx = target.x() - node.min().x();
            else if (target.x() > node.max().x()) dx = target.x() - node.max().x();
            dy = target.y() - node.point().y(); 
        }
        
        return dx*dx + dy*dy;
        
        
    }
    
    private void searchNearest(Node node) {
        if (node == null)
            return;
        count++;
        if (node.point().distanceSquaredTo(target) < minDistance) {
            minDistance = node.point().distanceSquaredTo(target);
            nearestPoint = node.point();
        }

        if (node.isVer()) {
            if (target.x() < node.point().x()) {
                searchNearest(node.left);
                if (distanceSquared(node) < minDistance) {
                    searchNearest(node.right); 
                }   
            } else {
                searchNearest(node.right);
                if (distanceSquared(node) < minDistance) {
                    searchNearest(node.left); 
                }  
            }
            
        } else {
            if (target.y() < node.point().y()) {
                searchNearest(node.left);
                if (distanceSquared(node) < minDistance) {
                    searchNearest(node.right); 
                }  
            } else {
                searchNearest(node.right);
                if (distanceSquared(node) < minDistance) {
                    searchNearest(node.left); 
                }    
            }
        }

    }
    
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        minDistance = Double.MAX_VALUE;
        count = 0;
        target = p;
        searchNearest(root);
//        StdOut.println(count);
        return nearestPoint;
    }
    
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        
        // test for insert and contains
        Point2D p1 = new Point2D(0.7, 0.2);
        tree.insert(p1);
        StdOut.println(tree.contains(p1));
        StdOut.println(tree.root.point());
        StdOut.println(tree.root.isVer());
        StdOut.println(tree.size());
        StdOut.println("------------------");
        
        Point2D p2 = new Point2D(0.5, 0.4);
        tree.insert(p2);
        StdOut.println(tree.contains(p2));
        StdOut.println(tree.root.left.point());
        StdOut.println(tree.root.left.isVer());
        StdOut.println(tree.size());
        StdOut.println("------------------");
        
        Point2D p3 = new Point2D(0.2, 0.3);
        tree.insert(p3);
        StdOut.println(tree.contains(p3));
        StdOut.println(tree.root.left.left.point());
        StdOut.println(tree.root.left.left.isVer());
        StdOut.println(tree.size());
        StdOut.println("------------------");
        
        Point2D p4 = new Point2D(0.4, 0.7);
        tree.insert(p4);
        StdOut.println(tree.contains(p4));
        StdOut.println(tree.root.left.right.point());
        StdOut.println(tree.root.left.right.isVer());
        StdOut.println("------------------");
        
        Point2D p5 = new Point2D(0.9, 0.6);
        tree.insert(p5);
        StdOut.println(tree.contains(p5));
        StdOut.println(tree.root.right.point());
        StdOut.println(tree.root.right.isVer());
        StdOut.println("------------------");
        
        Point2D p6 = new Point2D(0.9, 0.7);
        StdOut.println(tree.contains(p6));
        StdOut.println("------------------");
        
        // test for range search 
        StdOut.println("*************************");
        for (Point2D point : tree.range(new RectHV(0.69, 0, 1, 0.21))) {
            StdOut.println(point);  // (0.7, 0.2)
        }
        
        StdOut.println("*************************");
        for (Point2D point : tree.range(new RectHV(0, 0, 1.0, 1.0))) {
            StdOut.println(point);  // all
        }
        
        StdOut.println("*************************");
        for (Point2D point : tree.range(new RectHV(0.39, 0.39, 0.51, 0.71))) {
            StdOut.println(point);  // (0.4, 0.7), (0.5, 0.4)
        }
    }

}
