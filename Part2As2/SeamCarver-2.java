import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

public class SeamCarver {
//    private Color[][] color;
    private int[][] coinfo;
    private double[][] energy;
    private boolean transposed;
    private boolean horizontal;
    private int height;  // actually height
    private int width;   // actually width
    
    public SeamCarver(Picture picture) {
//        color = new Color[picture.height()][picture.width()];
        coinfo = new int[picture.height()][picture.width()];
        energy = new double[picture.height()][picture.width()];
        height = picture.height();
        width = picture.width();
        transposed = false;
        
        // set color matrix
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                coinfo[i][j] = picture.get(j, i).getRGB();
            }
        }
        // set energy matrix
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                setEnergy(i, j);
            }
        }
    }
    
    private void setEnergy(int i, int j) {
        if (i == 0 || i == heightR()- 1 || 
            j == 0 || j == widthR() - 1)
            energy[i][j] =  1000.0;
        else {
            int rx = ((coinfo[i][j-1] & (0xff << 16)) >> 16) - ((coinfo[i][j+1] & (0xff << 16)) >> 16);
            int gx = ((coinfo[i][j-1] & (0xff << 8)) >> 8) - ((coinfo[i][j+1] & (0xff << 8)) >> 8);
            int bx = (coinfo[i][j-1] & (0xff))- (coinfo[i][j+1] & (0xff));
            int ry = ((coinfo[i-1][j] & (0xff << 16)) >> 16) - ((coinfo[i+1][j] & (0xff << 16)) >> 16);
            int gy = ((coinfo[i-1][j] & (0xff << 8)) >> 8) - ((coinfo[i+1][j] & (0xff << 8)) >> 8);
            int by = (coinfo[i-1][j] & (0xff)) - (coinfo[i+1][j] & (0xff));
//            int rx = color[i][j-1].getRed() - color[i][j+1].getRed();
//            int gx = color[i][j-1].getGreen() - color[i][j+1].getGreen();
//            int bx = color[i][j-1].getBlue() - color[i][j+1].getBlue();
//            int ry = color[i-1][j].getRed() - color[i+1][j].getRed();
//            int gy = color[i-1][j].getGreen() - color[i+1][j].getGreen();
//            int by = color[i-1][j].getBlue() - color[i+1][j].getBlue();
            energy[i][j] =  Math.sqrt((double) (rx*rx + gx*gx + bx*bx + ry*ry + gy*gy + by*by));
        }
    }
    
    private void transpose() {
        transposed = !transposed;
//        Color[][] newColor = new Color[heightR()][widthR()];
        int[][] newCoinfo = new int[heightR()][widthR()];
        double[][] newEnergy = new double[heightR()][widthR()];
        for (int i = 0; i < widthR(); i++) {
            for (int j = 0; j < heightR(); j++) {
//                newColor[j][i] = color[i][j];
                newCoinfo[j][i] = coinfo[i][j];
                newEnergy[j][i] = energy[i][j];
            }
        }
//        color = newColor;
        coinfo = newCoinfo;
        energy = newEnergy;
    }
    
    public Picture picture() {
        Picture newpic = new Picture(width(), height());
        if (transposed)
            transpose();
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                newpic.set(i, j, new Color(coinfo[j][i]));
//                newpic.set(i, j, color[j][i]);
            }
        }
        clear();
        return newpic;
    }
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
    private int heightR() {
        if (transposed)
            return width();
        else
            return height();
    }
    
    private int widthR() {
        if (transposed)
            return height();
        else 
            return width();
    }
    
    public double energy(int x, int y) {
        if (!transposed)
            return energy[y][x];
        else
            return energy[x][y];
    }
    
    public int[] findHorizontalSeam() {
        if (!transposed)
            transpose();
        horizontal = true;
        int[] temp = findVerticalSeam();
        horizontal = false;
        return temp;
    }
    
    public int[] findVerticalSeam() {
        if (transposed && !horizontal)
            transpose();
        if (widthR() == 1 || widthR() == 2) {
            int[] path = new int[heightR()];
            for (int i = heightR()-1; i >= 0; i--) {
                path[i] = 0;
            }
            return path;
        }
        double[][] disTo = new double[heightR()][widthR()];
        int[][] edgeTo = new int[heightR()][widthR()];
        for (int i = 0; i < heightR(); i++) {
            for (int j = 0; j < widthR(); j++) {
                if (i == 0)
                    disTo[i][j] = 1000.00;
                else
                    disTo[i][j] = Double.POSITIVE_INFINITY;
                edgeTo[i][j] = -1;
            }
        }
        
        for (int i = 0; i < heightR()-1; i++) {
            for (int j = 1; j < widthR()-1; j++) {  
                relax(i, j, disTo, edgeTo);
            }
        }
        
        double min = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int j = 0; j < widthR(); j++) {
            if (min > disTo[heightR()-1][j]) {
                min = disTo[heightR()-1][j];
                minIndex = j;
            }    
        }
//        System.out.println(minIndex);
//        System.out.println(edgeTo[height()-1][minIndex]);
        int[] path = new int[heightR()];
        for (int i = heightR()-1; i >= 0; i--) {
            path[i] = minIndex;
            if (minIndex != -1)
                minIndex = edgeTo[i][minIndex];
        }        
        return path;
    }
    
    private void relax(int i, int j, double[][] disTo, int[][] edgeTo) {
        if (disTo[i+1][j-1] > disTo[i][j] + energy[i+1][j-1]) {
            disTo[i+1][j-1] = disTo[i][j] + energy[i+1][j-1];
            edgeTo[i+1][j-1] = j;
        }
            
        if (disTo[i+1][j] > disTo[i][j] + energy[i+1][j]) {
            disTo[i+1][j] = disTo[i][j] + energy[i+1][j];
            edgeTo[i+1][j] = j; 
        }
           
        if (disTo[i+1][j+1] > disTo[i][j] + energy[i+1][j+1]) {
            disTo[i+1][j+1] = disTo[i][j] + energy[i+1][j+1];
            edgeTo[i+1][j+1] = j;
        } 
    }
    
    public void removeHorizontalSeam(int[] seam) {
        if (!transposed)
            transpose();
        horizontal = true;
        removeVerticalSeam(seam);
        horizontal = false;
    }
    
    public void removeVerticalSeam(int[] seam) {
        if (transposed && !horizontal)
            transpose();
        if (seam == null)
            throw new NullPointerException();
        if (seam.length != heightR())
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] >= widthR() || seam[i] < 0) {
                throw new IllegalArgumentException();
            }
            if (i != 0 && Math.abs(seam[i]-seam[i-1]) > 1) 
                throw new IllegalArgumentException();
            if (seam[i] != widthR()-1) {
//                System.arraycopy(color[i], seam[i]+1, color[i], seam[i], widthR()-seam[i]-1);
                System.arraycopy(coinfo[i], seam[i]+1, coinfo[i], seam[i], widthR()-seam[i]-1);
                System.arraycopy(energy[i], seam[i]+1, energy[i], seam[i], widthR()-seam[i]-1);
            }
//            color[i][widthR()-1] = null;
            coinfo[i][widthR()-1] = -1;
            energy[i][widthR()-1] = -1.0;       
        }        
        if (!horizontal)
            width--;
        else
            height--;
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] - 1 >= 0)
                setEnergy(i, seam[i]-1);
            if (seam[i] < widthR()) {
                setEnergy(i, seam[i]);
            }
        }
//        clear();
    }
    
    private void clear() {
//        Color[][] newColor = new Color[heightR()][widthR()];
        int[][] newCoinfo = new int[heightR()][widthR()];
        double[][] newEnergy = new double[heightR()][widthR()];
        for (int i = 0; i < heightR(); i++) {
            for (int j = 0; j < widthR(); j++) {
                newCoinfo[i][j] = coinfo[i][j];
                newEnergy[i][j] = energy[i][j];
            }
        }
        coinfo = newCoinfo;
        energy = newEnergy;
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver carver = new SeamCarver(picture);
        int[] verticalSeam = carver.findVerticalSeam();
        for (int x : verticalSeam)
            StdOut.print(x + " ");
        StdOut.println("}");
    }
}
