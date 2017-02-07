package Mathematics;

/**
 * Natural number n is perfect, if it's value is equal to a sum of all its positive
 * divisors.
 */
public class PerfectNumber {
    /**
     * Checks whether the number is perfect
     * @param number tested number
     * @return true if the number is perfect, false otherwise
     */
    public static boolean isPerfect(long number) {
        // Odd perfect number does not exist
        // If it does, it has a higher value than long can represent
        if (number % 2 == 1)
            return false;

        long result = 1; // trivial
        long i = 2;
        while ((i * i) <= number) { // until i <= sqrt(number)
            if (number % i == 0) {
                result += i;
                result += number / i;
            }
            i++;
        }
        if ((i * i) == number) {
            // perfect square, sqrt(number) was added twice
            result -= i;
        }

        return result == number;
    }

}
