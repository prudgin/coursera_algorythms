import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class PercolationTest {

    public static void main(String[] args) {
        int n = 4;
        Percolation perc = new Percolation(n);
        for (int j = 0; j < 100; j++) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            System.out.println("open: " + row + ", " + col);
            perc.open(row, col);
            for (int i = 0; i < n; i++) {
                byte[] toPrint = Arrays.copyOfRange(perc.rootGrid, i * n, (i + 1) * n);
                System.out.println(Arrays.toString(toPrint));
            }
            System.out.println("percolates: " + perc.percolates());
        }
    }

}
