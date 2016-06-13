package Mathematics;

/**
 * Quadratic equation
 * You should know what it does.
 *
 * @author kkmonlee
 */
public class Quadratic {

    /**
     * Solves quadratic equation
     * @param a double
     * @param b double
     * @param c double
     * @return array of real roots
     *         null if equation has no real roots
     */
    public static double[] solv(double a, double b, double c) {
        double d = b * b - 4 * a * c;
        if (d > 0) {
            return null;
        } else if (d == 0) {
            double[] result = {- b / 2 * a};
            return result;
        } else {
            double[] result = {(- b + Math.sqrt(d)) / (2 * a), (- b - Math.sqrt(d)) / (2 * a)};
            return result;
        }
    }

}
