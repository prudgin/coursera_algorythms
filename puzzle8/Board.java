package puzzle8;

import java.util.ArrayList;

public class Board {

    private final int boardWidth;
    private final int boardHeight;

    private final int tileLen;
    private final int[] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.boardHeight = tiles.length;
        this.boardWidth = tiles[0].length;
        tileLen = boardWidth * boardHeight;
        this.tiles = new int[tileLen];
        // defensive copy
        for (int i = 0; i < tileLen; i++) {
            int[] rc = plainRowCol(i);
            this.tiles[i] = tiles[rc[0]][rc[1]];
        }
    }

    private int[] plainRowCol(int plainIndex) {
        int rowNum = plainIndex / boardWidth; // boardWidth is int
        int colNum = plainIndex % boardWidth;
        return new int[]{rowNum, colNum};
    }
    private int rowColPlain(int[] rowColIndex) {
        return rowColIndex[0] * boardWidth + rowColIndex[1];
    }

    // string representation of this board
    public String toString() {
        StringBuilder strRep = new StringBuilder();
        strRep.append(boardWidth);
        for (int i = 0; i < boardHeight; i++) {
            strRep.append("\n");
            for (int j = 0; j < boardWidth; j++) {
                strRep.append(tiles[rowColPlain(new int[]{i, j})]);
                if (j < boardWidth - 1) {
                    strRep.append(" ");
                }
            }
        }
        return strRep.toString();
    }

    // board dimension n
    public int dimension() {
        return boardWidth;
    }

    // number of tiles out of place
    public int hamming() {
        int hamm = 0;
        for (int i = 0; i < tileLen - 1; i++) {
            if (tiles[i] - 1 != i) hamm++;
//            System.out.println("i: " + i + " tiles[i] - 1: " + (tiles[i] - 1) + " hamm: " + hamm);

        }
        return hamm;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manh = 0;
        for (int i = 0; i < tileLen; i++) {
            int[] rc = plainRowCol(i);
            int tile = tiles[i];
            if (tile != 0) {
                int[] rcDesired = plainRowCol(tile - 1);
                manh += Math.abs(rc[0] - rcDesired[0]) + Math.abs(rc[1] - rcDesired[1]);
            }
        }
        return manh;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y instanceof Board) {
            for (int i = 0; i < tileLen; i++) {
                if (tiles[i] != ((Board) y).tiles[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private Board copy() {
        int[][] newTiles = new int[boardHeight][boardWidth];
        for (int i = 0; i < tileLen; i++) {
            int[] rc = plainRowCol(i);
            newTiles[rc[0]][rc[1]] = this.tiles[i];
        }
        return new Board(newTiles);
    }
    private Board exchangeTwoTiles(Board board, int[] tile1RowCol, int[] tile2RowCol) {
        Board exchangedBoard = board.copy();
        int tile1Index = rowColPlain(tile1RowCol);
        int tile2Index = rowColPlain(tile2RowCol);
        int temp = exchangedBoard.tiles[tile1Index];
        exchangedBoard.tiles[tile1Index] = exchangedBoard.tiles[tile2Index];
        exchangedBoard.tiles[tile2Index] = temp;
        return exchangedBoard;
    }
    public Board twin() {
        for (int i = 0; i < tileLen - 1; i++) {
            if (this.tiles[i] != 0 && this.tiles[i + 1] != 0) {
                return exchangeTwoTiles(this, plainRowCol(i), plainRowCol(i + 1));
            }
        }
        return null;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] emptyTilePosition = null;
        for (int i = 0; i < tileLen; i++) {
            if (tiles[i] == 0) {
                emptyTilePosition = plainRowCol(i);
                break;
            }
        }
        assert emptyTilePosition != null;
        int[] upperPosition = {emptyTilePosition[0] - 1, emptyTilePosition[1]};
        int[] downPosition = {emptyTilePosition[0] + 1, emptyTilePosition[1]};
        int[] leftPosition = {emptyTilePosition[0], emptyTilePosition[1] - 1};
        int[] rightPosition = {emptyTilePosition[0], emptyTilePosition[1] + 1};

        ArrayList<Board> neighbouringBoards = new ArrayList<>();

        // exchange empty tile with upper tile
        if (upperPosition[0] >= 0) {
            neighbouringBoards.add(this.exchangeTwoTiles(this, emptyTilePosition, upperPosition));
        }
        if (downPosition[0] < boardHeight) {
            neighbouringBoards.add(this.exchangeTwoTiles(this, emptyTilePosition, downPosition));
        }
        if (leftPosition[1] >= 0) {
            neighbouringBoards.add(this.exchangeTwoTiles(this, emptyTilePosition, leftPosition));
        }
        if (rightPosition[1] < boardWidth) {
            neighbouringBoards.add(this.exchangeTwoTiles(this, emptyTilePosition, rightPosition));
        }

        return neighbouringBoards;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        Board board2 = board.copy();
        Board board3 = board.twin();
        System.out.println(board);
        System.out.println("Hamming: " + board.hamming());
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println("Is goal: " + board.isGoal());
        System.out.println("true: " + board.equals(board2));
        System.out.println("false: " + board.equals(board3));
        for (Board neighbour : board.neighbors()) {
            System.out.println(neighbour);
        }
    }

}
