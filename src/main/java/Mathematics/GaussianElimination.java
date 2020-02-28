package Mathematics;

/**
 * Created by aa on 03/01/17.
 */
public class GaussianElimination {

    private static final double EPSILON = 1e-8;
    private final int m;
    private final int n;
    private double[][] a;

    private GaussianElimination(double[][] A, double[] b) {
        m = A.length;
        n = A[0].length;

        if (b.length != m) throw new IllegalArgumentException("Dimensions disagree");

        a = new double[m][n + 1];
        for (int i = 0; i < m; i++) {
            System.arraycopy(A[i], 0, a[i], 0, n);
        }

        for (int i = 0; i < m; i++) {
            a[i][n] = b[i];
        }

        forwardElimination();
        assert certifySolution(A, b);
    }

    private void forwardElimination() {
        for (int p = 0; p < Math.min(m, n); p++) {

            int max = p;
            for (int i = p+1; i < m; i++) {
                if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
                    max = i;
                }
            }

            swap(p, max);

            if (Math.abs(a[p][p]) <= EPSILON) {
                continue;
            }

            pivot(p);
        }
    }

    private void swap(int row1, int row2) {
        double[] temp = a[row1];
        a[row1] = a[row2];
        a[row2] = temp;
    }

    private void pivot(int p) {
        for (int i = p + 1; i < m; i++) {
            double alpha = a[i][p] / a[p][p];
            for (int j = p; j <= n; j++) {
                a[i][j] -= alpha * a[p][j];
            }
        }
    }

    private double[] primal() {
        double[] x = new double[n];
        for (int i = Math.min(n - 1, m - 1); i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += a[i][j] * x[j];
            }

            if (Math.abs(a[i][i]) > EPSILON)
                x[i] = (a[i][n] - sum) / a[i][i];
            else if (Math.abs(a[i][n] - sum) > EPSILON)
                return null;
        }

        for (int i = n; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += a[i][j] * x[j];
            }
            if (Math.abs(a[i][n] - sum) > EPSILON)
                return null;
        }

        return x;
    }

    private boolean isFeasible() {
        return primal() != null;
    }

    private boolean certifySolution(double[][] A, double[] b) {
        if (!isFeasible()) return true;
        double[] x = primal();
        for (int i = 0; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            if (Math.abs(sum - b[i]) > EPSILON) {
                System.out.println("not feasible");
                System.out.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    private static void test(double[][] A, double[] b) {
        System.out.println("----------------------------------------------------");
        System.out.println("----------------------------------------------------");
        GaussianElimination gaussian = new GaussianElimination(A, b);
        double[] x = gaussian.primal();
        if (gaussian.isFeasible()) {
            for (double aX : x) {
                System.out.printf("%.6f\n", aX);
            }
        }
        else {
            System.out.println("System is infeasible");
        }
        System.out.println();
        System.out.println();
    }

    private static void test7() {
        double[][] A = {
                { 0, 1,  1 },
                { 2, 4, -2 },
                { 0, 3, 15 },
                { 2, 8, 14 }
        };
        double[] b = { 4, 2, 36, 42 };
        test(A, b);
    }

    public static void main(String[] args) {
        test7();
    }
}
