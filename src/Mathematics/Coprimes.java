package Mathematics;

/**
 * Created by aa on 15/04/17.
 */
public class Coprimes {
    private Coprimes() {

    }

    public static long getNumberOfCoprimes(long n) {
        if (n < 1)
            return 0;
        long res = 1;
        for (int i = 2; i * i <= n; i++) {
            int times = 0;
            while (n % i == 0) {
                res *= (times > 0 ? i : i - 1);
                n /= i;
                times++;
            }
        }

        if (n > 1)
            res *= n - 1;
        return res;
    }
}
