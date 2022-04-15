package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationOld {

    public final WeightedQuickUnionUF grid;
    public final int n;
    public final boolean[] openGrid;
    public final boolean[] fullGrid;
    public int openCount;
    public boolean doPercolate;

    // creates n-by-n grid, with all sites initially blocked
    public PercolationOld(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(" n <= 0");
        }
        // creates a n x n grid with 2 virtual elements;
        doPercolate = false;
        this.n = n;
        int gridLen = n * n + 1; // one top virtual node
        this.grid = new WeightedQuickUnionUF(gridLen);
        this.openGrid = new boolean[gridLen];
        this.fullGrid = new boolean[gridLen];
        openCount = 0;
        openGrid[0] = true;
        fullGrid[0] = true;
        // openGrid[gridLen - 1] = true;

    }

    public static void main(String[] args) {
        // this is my fucking intent
    }

    public int vector2index(int row, int col) {
        // row - 1 = count of full rows, -1 for index starting with 0, +1 for virtual element
        return (row - 1) * n + col - 1 + 1;
    }

    public void openUp(int row, int col) {
        int curIndex = vector2index(row, col);
        int neighIndex = vector2index(row - 1, col);
        if (row == 1) {
            grid.union(0, curIndex);
        } else {
            if (openGrid[neighIndex]) {
                grid.union(curIndex, neighIndex);
            }
        }
    }

    public void openDown(int row, int col) {
        int curIndex = vector2index(row, col);
        int neighIndex = vector2index(row + 1, col);
        if (row < n && openGrid[neighIndex]) {
            grid.union(curIndex, neighIndex);
        }

    }

    public void openLeft(int row, int col) {
        int curIndex = vector2index(row, col);
        int neighIndex = vector2index(row, col - 1);
        if (col != 1) {
            if (openGrid[neighIndex]) {
                grid.union(curIndex, neighIndex);
            }
        }
    }

    public void openRight(int row, int col) {
        int curIndex = vector2index(row, col);
        int neighIndex = vector2index(row, col + 1);
        if (col != n) {
            if (openGrid[neighIndex]) {
                grid.union(curIndex, neighIndex);
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > n || row < 1 || col > n || col < 1) {
            throw new IllegalArgumentException("row or col > n" + row + " " + col);
        }
        int curIndex = vector2index(row, col);
        if (!openGrid[curIndex]) {
            openUp(row, col);
            openDown(row, col);
            openLeft(row, col);
            openRight(row, col);
            openGrid[curIndex] = true;
            openCount++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > n || row < 1 || col > n || col < 1) {
            throw new IllegalArgumentException("row or col > n" + row + " " + col);
        }
        return openGrid[vector2index(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > n || row < 1 || col > n || col < 1) {
            throw new IllegalArgumentException("row or col > n" + row + " " + col);
        }
        int curIndex = vector2index(row, col);
        if (fullGrid[curIndex]) {
            return true;
        }
        if (openGrid[curIndex]) {
            if ((grid.find(0) == grid.find(curIndex))) {
                fullGrid[curIndex] = true;
                if (row == n) {
                    this.doPercolate = true;
                }
                return true;
            }
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // iterate over bottom row
        if (doPercolate) {
            return true;
        }
        for (int i = 1; i <= n; i++) {
            if (this.isOpen(n, i) && this.isFull(n, i)) {
                doPercolate = true;
                return true;
            }
        }
        return false;
    }
}
