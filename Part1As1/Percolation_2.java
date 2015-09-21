import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation_2 {
    private int num;
    private WeightedQuickUnionUF uf;
    private byte[] status;
    private final byte OPEN = 0x01;
    private final byte TOP = 0x02;
    private final byte BOTTOM = 0x04;

    // create N-by-N grid, with all sites blocked
    public Percolation_2(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N <= 0");
        num = N;
        uf = new WeightedQuickUnionUF(N*N+1);
        status = new byte[N*N+1];
    }
   
    private void check(int i, int j) {
        if (i < 1 || i > num || j < 1 || j > num) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
    }
    
    // helper function for union: change root's status
    private void unionStatus(int p, int q) {
        int i = uf.find(p);
        int j = uf.find(q);
        byte temp = (byte) (status[i] | status[j]);
        status[i] = temp;
        status[j] = temp;
        uf.union(i, j);
    }
    
    // helper function for connect site's neighbor
    private void conNeighbor(int i, int j) {
        int middle = num*(i-1)+j-1;
        int up = num*(i-1)+j-1-num;
        int down = num*(i-1)+j-1+num;
        int left = num*(i-1)+j-2;
        int right = num*(i-1)+j;
        
        if (left >= 0 && left % num != num-1 && status[left] > 0) {
            unionStatus(left, middle);
        }
        if (right <= num*num-1 && right % num != 0 && status[right] > 0) {
            unionStatus(right, middle);
        }
        if (up >= 0 && status[up] > 0) {
            unionStatus(up, middle);
        }
        if (down <= num*num-1 && status[down] > 0) {
            unionStatus(down, middle);
        }
    }
    
    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        check(i, j);
        if (!isOpen(i, j)) {
            int index = num*(i-1)+j-1;
            if (i == num) {
                status[index] = (byte) (status[index] | BOTTOM);
            } 
            if (i == 1) {
                status[index] = (byte) (status[index] | TOP);
                unionStatus(index, num*num);
            } 
            status[index] = (byte) (status[index] | OPEN);
            conNeighbor(i, j);
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        check(i, j);
        return ((byte) (status[num*(i-1)+j-1] % 2)) == 1;
    }
   
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        check(i, j);
        return ((byte) (status[uf.find(num*(i-1)+j-1)] / 2 % 2)) == 1;
    }
    
    // does the system percolate?
    public boolean percolates() {
        byte x = status[uf.find(num*num)];
        return ((byte) (x / 2 / 2 % 2)) == 1 && ((byte) (x / 2 % 2)) == 1;
    }

    public static void main(String[] args) {
    }

}
