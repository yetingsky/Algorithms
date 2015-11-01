import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


public class SAP {
    private Digraph graph;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new NullPointerException();
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > graph.V() || w < 0 || w > graph.V())
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > graph.V() || w < 0 || w > graph.V())
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[1];
    } 
    
    private int[] helper(BreadthFirstDirectedPaths bfs1, BreadthFirstDirectedPaths bfs2) {
//        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
//        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        int length = Integer.MAX_VALUE, ancestor = 0;
        for (int i = 0, temp = 0; i < graph.V(); i++) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                temp = bfs1.distTo(i) + bfs2.distTo(i);
                if (temp < length) {
                    length = temp;
                    ancestor = i;
                }   
            }
        }
        return new int[] {length, ancestor};
    }

    private int[] modify(int[] a) {
        if (a[0] == Integer.MAX_VALUE) {
            a[0] = -1;
            a[1] = -1;
        }
        return a;
    }
        
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new NullPointerException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new NullPointerException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[1];
    }
        
    // do unit testing of this class
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
//        int length   = sap.length(35083, 48629);
//        int ancestor = sap.ancestor(35083, 48629);
        int length   = sap.length(7093, 18765);
        int ancestor = sap.ancestor(7093, 18765);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//        while (!StdIn.isEmpty()) {
//            int v = StdIn.readInt();
//            int w = StdIn.readInt();
//            int length   = sap.length(v, w);
//            int ancestor = sap.ancestor(v, w);
//            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//        }
    }

}
