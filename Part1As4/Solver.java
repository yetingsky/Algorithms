import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;


public class Solver {
    private boolean solvable = false;
    private int moves = 0;
    private MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
//    private MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();  
    private Stack<Board> boardStack = new Stack<Board>();
    
///* using double MinPQ */
//    public Solver(Board initial) {
//        Board board = null;
//        Board boardTwin = null;
//        SearchNode node = null;
//        SearchNode nodeTwin = null;
//        pq.insert(new SearchNode(initial, true));
//        pqTwin.insert(new SearchNode(initial.twin(), false));
//        while (!pq.isEmpty() && !pqTwin.isEmpty()) {
//            node = pq.delMin();
//            nodeTwin = pqTwin.delMin();
//            board = node.getBoard();
//            boardTwin = nodeTwin.getBoard();
//            if (board.isGoal()) {
//                solvable = true;
//                moves = node.move;
//                break;
//            }
//            if (boardTwin.isGoal()) {
//                solvable = false;
//                moves = -1;
//                break;
//            }
//            for (Board neigh: board.neighbors()) {
//                if (node.prev == null || !node.prev.getBoard().equals(neigh)) {
//                    pq.insert(new SearchNode(neigh, node));
//                }
//            }
//               
//            for (Board neighTwin: boardTwin.neighbors()) {
//                if (nodeTwin.prev == null || !nodeTwin.prev.getBoard().equals(neighTwin)) {
//                    pqTwin.insert(new SearchNode(neighTwin, nodeTwin));
//                }
//            }
//                
//        }

//       if (solvable) {
//           while (node != null) {
//               boardStack.push(node.board);
//               node = node.prev;
//           }
//       }   
//    }
    
    /* using only one MinPQ */
    public Solver(Board initial) {
        Board board = null;
        SearchNode node = null;
        pq.insert(new SearchNode(initial, true));
        pq.insert(new SearchNode(initial.twin(), false));
        while (!pq.isEmpty()) {
            node = pq.delMin();
            board = node.getBoard();
            if (board.isGoal()) {
                break;
            }
            for (Board neigh: board.neighbors()) {
                if (node.prev == null || !node.prev.getBoard().equals(neigh)) {
                    pq.insert(new SearchNode(neigh, node));
                }
            }
        }
        
        if (node.notTwin) {
            solvable = true;
            moves = node.move;
            while (node != null) {
                boardStack.push(node.board);
                node = node.prev;
            }
        } else {
            solvable = false;
            moves = -1;
        }
    }
    
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode prev;
        private int move;
        private int priority;
        private boolean notTwin;
        
        public SearchNode(Board board, boolean notTwin) {
            this.board = board;
            prev = null;
            move = 0;
            priority = board.manhattan();
            this.notTwin = notTwin;
        }
        
        public SearchNode(Board board, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            this.move = prev.move + 1;
            this.notTwin = prev.notTwin;
            priority = board.manhattan() + this.move;
        }
        
        public Board getBoard() {
            return board;     
        }
        
        public int compareTo(SearchNode that) {
            if (priority < that.priority)
                return -1;
            else if (priority > that.priority)
                return 1;
            else {
                if (board.hamming() >= that.board.hamming())
                    return 1;
                else
                    return -1;
            }
        }
        
    }
    
    public boolean isSolvable() {
        return solvable;
    }
    
    public int moves() {
        return moves;
    }
    
    public Iterable<Board> solution() {
        if (!solvable)
            return null;
        return boardStack;
    }
    
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}