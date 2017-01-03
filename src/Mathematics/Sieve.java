package Mathematics;

/**
 * Sieve of Eratosthenes is an algorithm used to find prime numbers up to some
 * predefined integer n.
 *
 * Algorithm:
 *  For all numbers m: 2 .. n, if m is unmarked:
 *      add m to primes list
 *      mark all it's multiples, lesser or equal than n (k * m <= n, k >= 2)
 *  Else if m is marked, it is a composite number
 *
 *  Computational complexity: O(nlog(log(n)))
 *
 *
 */
public class Sieve {

    /**
     * Prints prime numbers up to upperBound
     * @param upperBound predefined integer n
     */
    public void EratosthenesSieve(int upperBound) {

        int mSqrt = (int) Math.sqrt(upperBound);
        boolean[] isComposite = new boolean[upperBound + 1];

        for (int i = 2; i <= mSqrt; i++) {
            if (!isComposite[i]) {
                System.out.println(i + " ");

                for (int j = i * i; j <= upperBound; j += i) {
                    isComposite[j] = true;
                }
            }
        }

        for (int i = mSqrt; i <= upperBound; i++) {
            if (!isComposite[i]) {
                System.out.println(i + " ");
            }
        }
    }
}
