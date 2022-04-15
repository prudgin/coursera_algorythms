package puzzle8;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.LinkedList;

public class Solver {

    private SearchNode originalResult;
    private SearchNode twinResult;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        SearchIterator originalSearch = new SearchIterator(initial);
        SearchIterator twinSearch = new SearchIterator(initial.twin());
        while (originalResult == null && twinResult == null) {
            originalResult = originalSearch.iterate();
            twinResult = twinSearch.iterate();
        }
    }

    private static class SearchIterator {
        Board initial;
        MinPQ<SearchNode> heap;

        private SearchIterator(Board initial) {
            this.initial = initial;
            this.heap = new MinPQ<>();
            SearchNode startNode = new SearchNode(initial, null);
            heap.insert(startNode);
        }

        private SearchNode iterate() {
            SearchNode poppedNode = heap.delMin();
            if (poppedNode.board.isGoal()) {
                return poppedNode;
            }
            for (SearchNode child : poppedNode.spawn()) {
                heap.insert(child);
            }
            return null;
        }
    }

    private static class SearchNode implements Comparable<SearchNode> {

        Board board;
        SearchNode parentNode;
        int priority;
        int moves;
        int heuristicDistance;

        private SearchNode(Board board, SearchNode parentNode) {
            this.board = board;
            this.parentNode = parentNode;
            if (parentNode == null) {
                this.moves = 0;
            } else {
                this.moves = parentNode.moves + 1;
            }

            this.heuristicDistance = this.board.manhattan();
            this.priority = heuristicDistance + moves;
        }

        private ArrayList<SearchNode> spawn() {
            ArrayList<SearchNode> spawnList = new ArrayList<>();
            for (Board child : board.neighbors()) {
                boolean sameAsGrandpa = false;
                if (parentNode != null) {
                    sameAsGrandpa = child.equals(parentNode.board);
                }
                if (!sameAsGrandpa) {
                    spawnList.add(new SearchNode(child, this));
                }
            }
            return spawnList;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return twinResult == null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? originalResult.moves : -1;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            LinkedList<Board> sequenceOfBoards = new LinkedList<>();
            SearchNode parent = originalResult;
            while (parent != null) {
                sequenceOfBoards.addFirst(parent.board);
                parent = parent.parentNode;
            }
            return sequenceOfBoards;
        } else {
            return null;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

//        int[][] ranadomTiles = new int[100][100];
//        for (int i = 0; i < 100; i++) {
//            for (int j = 0; j < 100; j ++) {
//                ranadomTiles[i][j] = j + 1 + i * 100;
//            }
//        }
//        ranadomTiles[99][99] = 0;
//        for (int i = 0; i < 100; i++) {
//            System.out.println(Arrays.toString(ranadomTiles[i]));
//        }
//        Board randomBoard = new Board(ranadomTiles);
//        System.out.println(randomBoard.isGoal());
//        System.out.println(randomBoard.hamming());


//        Board initial = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
//         solve the puzzle
        Solver solver = new Solver(initial);

//        print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
