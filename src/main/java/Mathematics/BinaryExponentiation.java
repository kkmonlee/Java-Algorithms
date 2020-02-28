package Mathematics;

public class BinaryExponentiation {
    /**
     * Normal multiplication is done in O(n).
     * a^b = a * a * a * ... * a
     *
     * Binary exponentiation algorithm approaches the multiplication problem in O(log n)
     * The idea is to split the work using the binary representation of the exponent.
     * 3^13 = 3^1101 = 3^8 * 3^4 * 3^1
     * -> 3^1 = 3
     * -> 3^2 = (3^1)^2 = 3^2 = 9
     * -> 3^3 = (3^2)^2 = 9^2 = 81
     * -> 3^4 = (3^3)^2 = 81^2 = 6561
     *
     * Skip 3^2 because the 2nd bit is not set.
     *
     * 3^13 = 6561 * 81 * 3 = 1594323
     */

    private static long recursive_binpow(long a, long b) {
        if (b == 0) return 1;
        long res = recursive_binpow(a, b / 2);
        if (b % 2 > 0) return res * res * a;
        else return res * res;
    }

    private static long iterative_binpow(long a, long b) {
        long res = 1;
        while (b > 0) {
            if ((b & 1) > 0) res *= a;
            a = a * a;
            b >>= 1;
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(recursive_binpow(3, 13));
        System.out.println(iterative_binpow(3, 13));
    }
}
