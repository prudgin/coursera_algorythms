import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PercolationTest {

    public static final int N = 40;
    public static final int T = 500;

    public static void main(String[] args) {
        List<int[]> points = test1();
//        List<int[]> points = Arrays.asList(
//                new int[]{2, 2},
//                new int[]{1, 2},
//                new int[]{2, 3},
//                new int[]{3, 3}
//        );

        if (points != null) {
            points.forEach(s -> System.out.println(Arrays.toString(s)));
            int n = N;
            Percolation perc = new Percolation(n);
            for (int[] point : points) {
                int row = point[0];
                int col = point[1];
                System.out.println("open: " + row + ", " + col);
//                if (row == 3 && col == 3) {
//                    System.out.println("===================== going verbose");
//                    perc.verbose = true;
//                }
                perc.open(row, col);
                System.out.println("full: " + perc.isFull(row, col));
                for (int i = 0; i < n; i++) {
//                    byte[] toPrint = Arrays.copyOfRange(perc.rootGrid, i * n, (i + 1) * n);
//                    System.out.println(Arrays.toString(toPrint));
                }
                System.out.println("percolates: " + perc.percolates());
            }
        }

    }

    public static List<int[]> test1() {
        int n = N;
        int t = T;
        Percolation percNew = new Percolation(n);
        PercolationOld percOld = new PercolationOld(n);
        List<int[]> points = new ArrayList<>();

        boolean testOk = true;
        int i = 0;
        while (testOk && i <= t) {
            i++;
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            testOk = openAndCheck(percNew, percOld, row, col);
            points.add(new int[]{row, col});
        }

        if (!testOk) {
            return points;
        } else {
            System.out.println("test good");
            return null;
        }
    }

    public static boolean openAndCheck(Percolation percNew, PercolationOld percOld, int row, int col) {
        boolean testOk = true;
        percNew.open(row, col);
        percOld.open(row, col);
        if (percNew.isFull(row, col) != percOld.isFull(row, col)) {
            System.out.println("Full not equal [" + row + ", " + col + "]");
            testOk = false;
        }
        if (percNew.percolates() != percOld.percolates()) {
            System.out.println("Percolate not equal [" + row + ", " + col + "]");
            testOk = false;
        }
        return testOk;
    }


}
