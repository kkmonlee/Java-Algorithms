package Mathematics;

/**
 * Fibonacci series is an infinite sequence of integers... you know the deal
 */
public class Fibonacci {

    /**
     * Fibonacci sequence using recursion
     * @param index of the number (starting at 0)
     * @return nth Fibonacci number
     */
    public static int fibonacci(int index) {
        if (index == 0) return 0;
        else if (index == 1) return 1;
        else return fibonacci(index - 1) + fibonacci(index - 2);
    }
}
