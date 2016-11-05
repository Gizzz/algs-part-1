import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid;
    private int gridSize;

    private WeightedQuickUnionUF uf;

    private int virtualTopIndex;
    private int virtualBottomIndex;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        gridSize = n;

        grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = 0;
            }
        }

        int ufSize = n * n + 2;
        virtualTopIndex = ufSize - 2;
        virtualBottomIndex = ufSize - 1;
        uf = new WeightedQuickUnionUF(ufSize);

        // connect nodes to virtual top
        for (int i = 0; i < n; i++) {
            uf.union(virtualTopIndex, i);
        }

        // connect nodes to virtual bottom
        int lastRowFirstColIndex = n * (n - 1);
        int lastRowLastColIndex = n * n - 1;
        for (int i = lastRowFirstColIndex; i <= lastRowLastColIndex; i++) {
            uf.union(virtualBottomIndex, i);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validateRowCol(row, col);

        if (isOpen(row, col)) return;

        grid[row - 1][col - 1] = 1;

        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }

        if (row < gridSize && isOpen(row + 1, col)) {
            uf.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }

        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }

        if (col < gridSize && isOpen(row, col + 1)) {
            uf.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);

        return grid[row - 1][col - 1] == 1;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validateRowCol(row, col);

        boolean isGridCellFull = isOpen(row, col) && uf.connected(virtualTopIndex, xyTo1D(row, col));
        return isGridCellFull;
    }

    // does the system percolate?
    public boolean percolates() {
        if (gridSize == 1) {
            return isFull(1, 1);
        }

        return uf.connected(virtualTopIndex, virtualBottomIndex);
    }

    private void validateRowCol(int row, int col) {
        if (row < 1 || row > gridSize ) {
            throw new IndexOutOfBoundsException("row");
        }

        if (col < 1 || col > gridSize ) {
            throw new IndexOutOfBoundsException("col");
        }
    }

    private int xyTo1D(int row, int col) {
        validateRowCol(row, col);

        return (row - 1) * gridSize + (col - 1);
    }

    public static void main(String[] args) {
        int n = 1;
        Percolation percolation = new Percolation(n);

        StdOut.println("for n = 1");
        StdOut.println("should not percolate (false): " + percolation.percolates());

        percolation.open(1, 1);

        StdOut.println("should percolate (true): " + percolation.percolates());
        StdOut.println();


        n = 2;
        percolation = new Percolation(n);

        StdOut.println("for n = 2");
        StdOut.println("should not be connected (false): " + percolation.uf.connected(0, 2));

        percolation.open(1, 2);
        percolation.open(2, 2);

        StdOut.println("should be connected (true): " + percolation.uf.connected(0, 2));
        StdOut.println();

        // print out grid state

//        StdOut.println("grid state:");
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                StdOut.print(percolation.grid[i][j] + " ");
//            }
//
//            StdOut.println();
//        }

    }
}
