import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] prob;
    private int total;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("N or T is invalid");
        total = T;
        prob = new double[T]; 
        int num;  
        Percolation perc;
        for (int time = 0; time < T; time++) {
            num = 0;
            perc = new Percolation(N);
            while (!perc.percolates()) {
                int i = StdRandom.uniform(1, N+1);
                int j = StdRandom.uniform(1, N+1);
                if (!perc.isOpen(i, j)) {
                    perc.open(i, j);
                    num++;
                }
            }
            prob[time] = ((double) num) / (N*N); 
            perc = null;
        }
    }
    
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(prob);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(prob);
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt((double) total);
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt((double) total);
    }

    // test client (described below)
    public static void main(String[] args)  {
    }

}
