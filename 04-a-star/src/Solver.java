import edu.princeton.cs.algs4.*;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        public final Board board;
        public final SearchNode prevSearchNode;
        public final int moves;

        SearchNode(Board board, SearchNode prevSearchNode, int moves) {
            this.board = board;
            this.prevSearchNode = prevSearchNode;
            this.moves = moves;
        }

        public int compareTo(SearchNode that) {
            return this.priority() - that.priority();
        }

        private int priority() {
            return board.manhattan() + moves;
        }
    }

    private final SearchNode solutionSearchNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException();

        Board twinBoard = initial.twin();

        SearchNode initialNode = new SearchNode(initial, null, 0);
        SearchNode twinInitialNode = new SearchNode(twinBoard, null, 0);

        MinPQ<SearchNode> queue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();
        queue.insert(initialNode);
        twinQueue.insert(twinInitialNode);

        SearchNode currentNode = queue.delMin();
        SearchNode twinCurrentNode = twinQueue.delMin();
        int moves = 1;

        while (!currentNode.board.isGoal() && !twinCurrentNode.board.isGoal()) {
            Iterable<Board> neighbors = currentNode.board.neighbors();
            for (Board b : neighbors) {
                if (currentNode.prevSearchNode != null && b == currentNode.prevSearchNode.board) {
                    continue;
                }

                queue.insert(new SearchNode(b, currentNode, moves));
            }

            Iterable<Board> twinNeighbors = twinCurrentNode.board.neighbors();
            for (Board b : twinNeighbors) {
                if (twinCurrentNode.prevSearchNode != null && b == twinCurrentNode.prevSearchNode.board) {
                    continue;
                }

                twinQueue.insert(new SearchNode(b, twinCurrentNode, moves));
            }

            currentNode = queue.delMin();
            twinCurrentNode = twinQueue.delMin();
            moves += 1;
        }

        if (currentNode.board.isGoal()) {
            solutionSearchNode = currentNode;
        } else if (twinCurrentNode.board.isGoal()) {
            solutionSearchNode = null;
        } else {
            throw new UnknownError("unexpected condition");
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solutionSearchNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;

        return solutionSearchNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> boards = new Stack<>();

        SearchNode currentSearchNode = solutionSearchNode;
        boards.push(currentSearchNode.board);

        while (currentSearchNode.prevSearchNode != null) {
            currentSearchNode = currentSearchNode.prevSearchNode;
            boards.push(currentSearchNode.board);
        }

        return boards;
    }


    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
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
