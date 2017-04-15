package Mathematics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aa on 15/04/17.
 */
public class Primes {

    public static final Map<Long, Long> getPrimeFactorisation(long number) {
        Map<Long, Long> map = new HashMap<>();
        long n = number;
        int c = 0;

        for (long i = 2; i * i <= n; i++) {
            c = 0;
            while (n % i == 0) {
                n /= i;
                c++;
            }

            Long p = map.get(i);
            if (p == null)
                p = 0L;
            p += c;
            map.put(i, p);
        }

        if (n > 1) {
            Long p = map.get(n);
            if (p == null)
                p = 0L;
            p += 1;
            map.put(n, p);
        }
        return map;
    }

    public static final boolean isPrime(long number) {
        if (number == 1)
            return false;
        if (number < 4)
            return true;
        if (number % 2 == 0)
            return false;
        if (number < 9)
            return true;
        if (number % 3 == 0)
            return false;
        long r = (long) (Math.sqrt(number));

        int f = 5;
        while (f <= r) {
            if (number % f == 0)
                return false;
            if (number % (f + 2) == 0)
                return false;
            f += 6;
        }
        return true;
    }

    private static boolean[] sieve = null;

    public static final boolean sieveOfEratosthenes(int number) {
        if (number == 1) {
            return false;
        }

        if (sieve == null || number >= sieve.length) {
            int start = 2;
            if (sieve == null) {
                sieve = new boolean[number + 1];
            } else if (number >= sieve.length) {
                sieve = Arrays.copyOf(sieve, number + 1);
            }

            for (int i = start; i <= Math.sqrt(number); i++) {
                if (!sieve[i]) {
                    for (int j = i * 2; j <= number; j += i) {
                        sieve[j] = true;
                    }
                }
            }
        }
        return !sieve[number];
    }
}
