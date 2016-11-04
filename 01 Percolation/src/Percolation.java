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

        int rowIndex = row - 1;
        int colIndex = col - 1;

        grid[rowIndex][colIndex] = 1;

        if (rowIndex > 0 && isOpen(row - 1, col)) {
            uf.union(xyTo1D(rowIndex, colIndex), xyTo1D(rowIndex - 1, colIndex));
        }

        if (rowIndex < gridSize - 1 && isOpen(row + 1, col)) {
            uf.union(xyTo1D(rowIndex, colIndex), xyTo1D(rowIndex + 1, colIndex));
        }

        if (colIndex > 0 && isOpen(row, col - 1)) {
            uf.union(xyTo1D(rowIndex, colIndex), xyTo1D(rowIndex, colIndex - 1));
        }

        if (colIndex < gridSize - 1 && isOpen(row, col + 1)) {
            uf.union(xyTo1D(rowIndex, colIndex), xyTo1D(rowIndex, colIndex + 1));
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);

        int rowIndex = row - 1;
        int colIndex = col - 1;

        return grid[rowIndex][colIndex] == 1;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validateRowCol(row, col);

        int rowIndex = row - 1;
        int colIndex = col - 1;

        return uf.connected(virtualTopIndex, xyTo1D(rowIndex, colIndex));
    }

    // does the system percolate?
    public boolean percolates() {
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

    private int xyTo1D(int rowIndex, int colIndex) {
        validateRowCol(rowIndex + 1, colIndex + 1);

        return rowIndex * gridSize + colIndex;
    }

    public static void main(String[] args) {
        int n = 2;
        Percolation percolation = new Percolation(n);
        percolation.open(1, 2);
        percolation.open(2, 2);

//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                StdOut.print(percolation.grid[i][j] + " ");
//            }
//
//            StdOut.print("\n");
//        }

        StdOut.println("is connected: " + percolation.uf.connected(0, 2));
    }
}
