import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class Board {
    private int N;
    private char[] board;
//    private static char[] goal;
    private int manPoint = 0;
    private int hamPoint = 0;
    private int blank;
    
    public Board(int[][] blocks) {
        N = blocks[0].length;
        board = new char[N*N];
//        goal = new char[N*N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i*N+j] = (char) blocks[i][j];
//                goal[i*N+j] = (char) (i * N + j + 1);
                if (blocks[i][j] == 0) {
                    blank = i*N+j;
                }
            }
        }
        getManPoint();
        getHamPoint(); 
    }
                                     
    public int dimension() {
        return N;
    }
    
    private void getManPoint() {
        int loc1;
        int loc2;
        for (int i = 0; i < N*N; i++) {
            if (board[i] != 0) {
                loc1 = i / N - (board[i]-1) / N;
                loc2 = i % N - (board[i]-1) % N;
                if (loc1 < 0) loc1 = -loc1;
                if (loc2 < 0) loc2 = -loc2;
//                loc1 = (loc1 > 0) ? loc1:-loc1;
//                loc2 = (loc2 > 0) ? loc2:-loc2;
                manPoint += (loc1 + loc2);
            }
        }
    }
    
    private void getHamPoint() {
        for (int i = 0; i < N*N; i++) {
            if (board[i] != 0 && board[i] != i + 1)
                hamPoint++;
        }        
    }
    
    public int hamming() {
        return hamPoint;
    }
    
    public int manhattan() {
        return manPoint;
    }
    
    public boolean isGoal() {
        for (int i = 0; i < N*N-1; i++) {
            if (board[i] != i+1)
                return false;
        }
        return true; 
    }
    
    private void swap(int[][] block, int i, int j) {
        int temp;
        temp = block[i / N][i % N];
        block[i / N][i % N] = block[j / N][j % N];
        block[j / N][j % N] = temp; 
    }
        
    public Board twin() {
        int[][] twinBoard = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                twinBoard[i][j] = board[i*N+j];
            }
        }
        if (blank / N == 1) {
            swap(twinBoard, 0, 1);
        } else {
            swap(twinBoard, N, N+1);
        }
        
        return new Board(twinBoard);
    }
     
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int i = 0; i < N*N; i++) {
            if (that.board[i] != this.board[i])
                return false;
        }
        return true;
    }
    
    public Iterable<Board> neighbors() {
        Stack<Board> neighbor = new Stack<Board>();
        
        int up = blank - N;
        int down = blank + N;
        int left = blank - 1;
        int right = blank + 1;
        
        int[][] neigh = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                neigh[i][j] = board[i*N+j];
            }
        }
        
//        int temp;
//        if (up >= 0) {
//            temp = neigh[up / N][up % N];
//            neigh[up / N][up % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;
//            neighbor.push(new Board(neigh));
//            temp = neigh[up / N][up % N];
//            neigh[up / N][up % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;  
//        }
//        if (down <= N*N-1) {
//            temp = neigh[down / N][down % N];
//            neigh[down / N][down % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;
//            neighbor.push(new Board(neigh));
//            temp = neigh[down / N][down % N];
//            neigh[down / N][down % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;
//        }
//        if (left >= 0 && blank % N != 0) {
//            temp = neigh[left / N][left % N];
//            neigh[left / N][left % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;
//            neighbor.push(new Board(neigh));
//            temp = neigh[left / N][left % N];
//            neigh[left / N][left % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;
//        }
//        if (right <= N*N-1 && blank % N != N-1) {
//            temp = neigh[right / N][right % N];
//            neigh[right / N][right % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;
//            neighbor.push(new Board(neigh));
//            temp = neigh[right / N][right % N];
//            neigh[right / N][right % N] = neigh[blank / N][blank % N];
//            neigh[blank / N][blank % N] = temp;
//        }
        
        
        if (up >= 0) {
            swap(neigh, up, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, up, blank);
        }
        if (down <= N*N-1) {
            swap(neigh, down, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, down, blank);
        }
        if (left >= 0 && blank % N != 0) {
            swap(neigh, left, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, left, blank);
        }
        if (right <= N*N-1 && blank % N != N-1) {
            swap(neigh, right, blank);
            neighbor.push(new Board(neigh));
            swap(neigh, right, blank);
        }
        
        return neighbor;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N*N; i++) {
                if (i != 0 && i % N == 0)
                    s.append("\n");
                s.append(String.format("%2d ", (int) board[i]));       
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        int[][] blocks2 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board other = new Board(blocks2);
        Board twin = initial.twin();
        
        StdOut.println(initial);
        StdOut.println(initial.dimension());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.isGoal());
        StdOut.println(initial.equals(other));
        StdOut.println(twin);
        StdOut.println("----------------");
        for (Board board: initial.neighbors())
            StdOut.println(board);
    }
}