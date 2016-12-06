import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] blocks;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new NullPointerException();

        int[][] deepCopy = new int[blocks.length][blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                deepCopy[i][j] = blocks[i][j];
            }
        }

        this.blocks = deepCopy;
    }

    // board dimension n
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                int expectedValue = i * dimension() + (j + 1);
                if (blocks[i][j] != expectedValue && blocks[i][j] != 0) {
                    hamming += 1;
                }
            }
        }

        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                int actualValue = blocks[i][j];
                int expectedValue = i * dimension() + (j + 1);
                if (actualValue == expectedValue || blocks[i][j] == 0) continue;

                int actualValueRowIndex = (actualValue - 1) / dimension();
                int actualValueColIndex = (actualValue - 1) % dimension();

                manhattan += Math.abs(i - actualValueRowIndex) + Math.abs(j - actualValueColIndex);
            }
        }

        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean isGoal = true;

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                int expectedValue = i * dimension() + (j + 1);
                boolean isExpectedValueOrZero = blocks[i][j] == expectedValue || blocks[i][j] == 0;
                boolean isLastBlock = (i == blocks.length - 1) && (j == blocks[i].length - 1);
                boolean isLastBlockIsZero = isLastBlock && blocks[i][j] == 0;

                if (!isExpectedValueOrZero && !isLastBlockIsZero) {
                    isGoal = false;
                }
            }
        }

        return isGoal;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int i1 = 0, i2 = 0, j1 = 0, j2 = 0;

        boolean isFirstFound = false;

        outerloop:
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] != 0) {
                    if (!isFirstFound) {
                        i1 = i;
                        j1 = j;

                        isFirstFound = true;
                    } else {
                        i2 = i;
                        j2 = j;

                        break outerloop;
                    }
                }
            }
        }

        // create new board and swap blocks
        int[][] blocks = this.blocks;
        int swap = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = swap;

        return new Board(blocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board)y;
        return this.toString().equals(that.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();

        int zeroRowIndex = 0;
        int zeroColIndex = 0;

        outerloop:
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] == 0) {
                    zeroRowIndex = i;
                    zeroColIndex = j;

                    break outerloop;
                }
            }
        }

        if (zeroRowIndex > 0) {
            int[][] blocks = cloneInstanceBlocks();
            int swap = blocks[zeroRowIndex - 1][zeroColIndex];
            blocks[zeroRowIndex - 1][zeroColIndex] = 0;
            blocks[zeroRowIndex][zeroColIndex] = swap;

            neighbors.enqueue(new Board(blocks));
        }

        if (zeroRowIndex < (dimension() - 1)) {
            int[][] blocks = cloneInstanceBlocks();
            int swap = blocks[zeroRowIndex + 1][zeroColIndex];
            blocks[zeroRowIndex + 1][zeroColIndex] = 0;
            blocks[zeroRowIndex][zeroColIndex] = swap;

            neighbors.enqueue(new Board(blocks));
        }

        if (zeroColIndex > 0) {
            int[][] blocks = cloneInstanceBlocks();
            int swap = blocks[zeroRowIndex][zeroColIndex - 1];
            blocks[zeroRowIndex][zeroColIndex - 1] = 0;
            blocks[zeroRowIndex][zeroColIndex] = swap;

            neighbors.enqueue(new Board(blocks));
        }

        if (zeroColIndex < (dimension() - 1)) {
            int[][] blocks = cloneInstanceBlocks();
            int swap = blocks[zeroRowIndex][zeroColIndex + 1];
            blocks[zeroRowIndex][zeroColIndex + 1] = 0;
            blocks[zeroRowIndex][zeroColIndex] = swap;

            neighbors.enqueue(new Board(blocks));
        }

        return neighbors;
    }

    private int[][] cloneInstanceBlocks() {
        int[][] blocksCopy = new int[dimension()][dimension()];

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                blocksCopy[i][j] = blocks[i][j];
            }
        }

        return blocksCopy;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        int n = dimension();
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }

            s.append("\n");
        }

        return s.toString();
    }


    // unit tests (not graded)
    public static void main(String[] args) {

        int[][] blocks = new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };

        Board board = new Board(blocks);
        StdOut.println(board);
        StdOut.println("//---------------------------");

        Iterable<Board> neighbors = board.neighbors();
        for (Board item : neighbors) {
            StdOut.println(item);
        }

    }
}