import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Subset {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        In in = new In(args[0]); 
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }
        
        for (int k = Integer.parseInt(args[0]); k > 0; k--)
            StdOut.println(rq.dequeue());
//        for (int k = 3; k > 0; k++)
//            StdOut.println(rq.dequeue());
    }

}
