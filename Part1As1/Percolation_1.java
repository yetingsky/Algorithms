import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation_1 {
    private int num;
    private WeightedQuickUnionUF uf1;
    private WeightedQuickUnionUF uf2;
    private boolean[] op;

    // create N-by-N grid, with all sites blocked
    public Percolation_1(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N <= 0");
        num = N;
        uf1 = new WeightedQuickUnionUF(N*N+2);
        uf2 = new WeightedQuickUnionUF(N*N+1);
        op = new boolean[N*N];
    }
   
    private void check(int i, int j) {
        if (i < 1 || i > num || j < 1 || j > num) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
    }
    
    private void unionBoth(int p, int q) {
        uf1.union(p, q);
        uf2.union(p, q);
    }
    
    // helper function for connect site's neighbor
    private void conNeighbor(int i, int j) {
        int middle = num*(i-1)+j-1;
        int up = num*(i-1)+j-1-num;
        int down = num*(i-1)+j-1+num;
        int left = num*(i-1)+j-2;
        int right = num*(i-1)+j;
        
        if (left >= 0 && left % num != num-1 && op[left]) {
            unionBoth(left, middle);
        }
        if (right <= num*num-1 && right % num != 0 && op[right]) {
            unionBoth(right, middle);
        }
        if (up >= 0 && op[up]) {
            unionBoth(up, middle);
        }
        if (down <= num*num-1 && op[down]) {
            unionBoth(down, middle);
        }
    }
    
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        check(i, j);
        if (!isOpen(i, j)) {
            op[num*(i-1)+j-1] = true;
            if (i == 1) {
                unionBoth(j-1, num*num);
            }
            if (i == num)
                uf1.union(num*(i-1)+j-1, num*num+1);
            conNeighbor(i, j);
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        check(i, j);
        return op[num*(i-1)+j-1];
    }
   
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        check(i, j);
        return uf2.connected(num*(i-1)+j-1, num*num);
    }
    
    // does the system percolate?
    public boolean percolates() {
        return uf1.connected(num*num, num*num+1);
    }

    public static void main(String[] args) {
    }

}
