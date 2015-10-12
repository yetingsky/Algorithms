import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.LinkedBag;

public class PointSET {
    
    private SET<Point2D> set;
    
    public PointSET() {
        set = new SET<Point2D>();
    }
    
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    public int size() {      
        return set.size();
    }
    
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        set.add(p);
    }
    
    public boolean contains(Point2D p) {    
        if (p == null)
            throw new NullPointerException();
        return set.contains(p);
    }
    
    public void draw() {
        for (Point2D point : set) {
//            StdOut.println(point);
            point.draw(); 
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();
        LinkedBag<Point2D> list = new LinkedBag<Point2D>();
        for (Point2D point : set) {
            if (rect.distanceSquaredTo(point) == 0)
                list.add(point);
        }
        return list;
    }
    
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();
        double min = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D point : set) {
            if (p.distanceSquaredTo(point) < min) {
                min = p.distanceSquaredTo(point);
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }
    
    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);

        StdDraw.show(0);

        // initialize the data structures with N points from standard input
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        
        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        brute.draw();
        
        StdOut.println("---------------------");
        // print the point in rectangle
        RectHV rect = new RectHV(0.5, 0, 1, 0.5);
        for (Point2D point : brute.range(rect))
            StdOut.println(point);
        
        StdOut.println("---------------------");
        // print the nearest point
        Point2D point = new Point2D(0.488, 1.0);
        StdOut.println(brute.nearest(point));
        
    }

}
