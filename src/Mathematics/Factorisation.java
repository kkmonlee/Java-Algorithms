package Mathematics;

/**
 * Integer factorisation is a process to break down a number into product of its prime divisors
 */
public class Factorisation {

    /**
     * Factorises the number n
     * @param number number to be factorised
     */
    public static void factorise(int number) {
        int remainder = number;

        for (int i = 2; i <= Math.sqrt(remainder); i++) {
            // For all i less or equal to sqrt(remainder)
            // try to factorise the remainder
            while (remainder % i == 0) {
                remainder = remainder / i;
                System.out.print(i + " ");
            }
        }
        // If the remainder is greater than 1, than it must be a prime
        // otherwise it would be already broken down
        if (remainder > 1) {
            System.out.print(remainder);
        }
    }
}
