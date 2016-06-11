package Mathematics;

/**
 * Factorial is a function of a non-negative integer n, whose output is a product
 * of all positive integers less or equal than n.
 */
public class Factorial {
    /**
     * Returns the factorial of a number n using recursion
     * @param number number n >= 0
     * @return the factorial of a number n
     */
    public static int factorial(int number) {
        if (number < 0) throw new IllegalArgumentException("Negative argument");
        if (number == 0 || number == 1) return 1;
        return number * factorial(number - 1);
    }
}
