package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private final WeightedQuickUnionUF grid;
    private final int n;

    private final byte[] rootGrid;

    private int openCount;

    private boolean doPercolate;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(" n <= 0");
        }
        doPercolate = false;
        this.n = n;
        int gridLen = n * n;
        this.grid = new WeightedQuickUnionUF(gridLen);
        this.rootGrid = new byte[gridLen];
        for (int i = 0; i < n; i++) {
            setTop(i);
        } // populate top row
        for (int i = (n * (n - 1)); i < n * n; i++) {
            setBottom(i);
        } // populate bottom row
        openCount = 0;
    }

    public static void main(String[] args) {
        // this is my fucking intent


    }

    // 0000(0) = closed, 0001(1) = open, 0010(2) = connected to top, 0100(4) = connected to bottom
    private boolean checkOpen(int index) {
        return (rootGrid[index] & 1) == 1;
    }

    private boolean checkTop(int index) {
        if (index < 0) {
            return false;
        }
        return (rootGrid[index] & 2) == 2;
    }

    private boolean checkBottom(int index) {
        if (index < 0) {
            return false;
        }
        return (rootGrid[index] & 4) == 4;
    }

    private void setOpen(int index) {
        rootGrid[index] |= 1;
    }

    private void setTop(int index) {
        if (index >= 0) {
            rootGrid[index] |= 2;
        }
    }

    private void setBottom(int index) {
        if (index >= 0) {
            rootGrid[index] |= 4;
        }
    }

    private int vector2index(int row, int col) {
        // row - 1 = count of full rows, -1 for index starting with 0
        return (row - 1) * n + col - 1;
    }

    private int initiateNeighbour(int curIndex, int neighIndex) {
        int neighbourRootIndex = -1;
        if (checkOpen(neighIndex)) {
            neighbourRootIndex = grid.find(neighIndex);
            grid.union(curIndex, neighIndex);
        }
        return neighbourRootIndex;
    }

    private int connectUp(int row, int col) {
        int upNeighbourRootIndex = -1;
        if (row > 1) {
            int curIndex = vector2index(row, col);
            int neighIndex = vector2index(row - 1, col);
            upNeighbourRootIndex = initiateNeighbour(curIndex, neighIndex);
        }
        return upNeighbourRootIndex;
    }

    private int connectDown(int row, int col) {
        int downNeighbourRootIndex = -1;
        if (row < n) {
            int curIndex = vector2index(row, col);
            int neighIndex = vector2index(row + 1, col);
            downNeighbourRootIndex = initiateNeighbour(curIndex, neighIndex);
        }
        return downNeighbourRootIndex;
    }

    private int connectLeft(int row, int col) {
        int leftNeighbourRootIndex = -1;
        if (col > 1) {
            int curIndex = vector2index(row, col);
            int neighIndex = vector2index(row, col - 1);
            leftNeighbourRootIndex = initiateNeighbour(curIndex, neighIndex);
        }
        return leftNeighbourRootIndex;
    }

    private int connectRight(int row, int col) {
        int rightNeighbourRootIndex = -1;
        if (col < n) {
            int curIndex = vector2index(row, col);
            int neighIndex = vector2index(row, col + 1);
            rightNeighbourRootIndex = initiateNeighbour(curIndex, neighIndex);
        }
        return rightNeighbourRootIndex;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > n || row < 1 || col > n || col < 1) {
            throw new IllegalArgumentException("row or col > n" + row + " " + col);
        }
        int curIndex = vector2index(row, col);

        if (!checkOpen(curIndex)) {
            int upRoot = connectUp(row, col);
            int downRoot = connectDown(row, col);
            int leftRoot = connectLeft(row, col);
            int rightRoot = connectRight(row, col);

            int curRoot = grid.find(curIndex);

            if (checkTop(upRoot) ||
                    checkTop(downRoot) ||
                    checkTop(leftRoot) ||
                    checkTop(rightRoot) ||
                    checkTop(curIndex)) {

                setTop(upRoot);
                setTop(downRoot);
                setTop(leftRoot);
                setTop(rightRoot);
                setTop(curRoot);
            }

            if (checkBottom(upRoot) ||
                    checkBottom(downRoot) ||
                    checkBottom(leftRoot) ||
                    checkBottom(rightRoot) ||
                    checkBottom(curIndex)) {

                setBottom(upRoot);
                setBottom(downRoot);
                setBottom(leftRoot);
                setBottom(rightRoot);
                setBottom(curRoot);
            }
            if (checkTop(curRoot) && checkBottom(curRoot)) {
                this.doPercolate = true;
            }

            setOpen(curIndex);
            openCount++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > n || row < 1 || col > n || col < 1) {
            throw new IllegalArgumentException("row or col > n" + row + " " + col);
        }
        return checkOpen(vector2index(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > n || row < 1 || col > n || col < 1) {
            throw new IllegalArgumentException("row or col > n" + row + " " + col);
        }
        int curIndex = vector2index(row, col);
        int ancestorIndex = grid.find(curIndex);
        return checkOpen(curIndex) && checkTop(ancestorIndex);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return doPercolate;
    }

}
