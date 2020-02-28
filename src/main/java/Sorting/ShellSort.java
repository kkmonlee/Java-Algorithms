package Sorting;
import Base.StdIn;
import Base.StdOut;

/**
 * Shellsort provides static methods for sorting an array with
 * Knuth's incremenet sequence
 *
 * @author kkmonlee
 */
public class ShellSort {

    private ShellSort() { }

    /**
     * Rearranges the array in ascending order
     * @param a the array to be sorted
     */
    public static void sort(Comparable[] a) {
        int N = a.length;

        int h = 1;

        while (h < N / 3)
            h = 3 * h + 1;

        while (h >= 1) {
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h && less(a[j], a[j - h]); j -= h) {
                    exch(a, j, j - h);
                }
            }
            assert isHsorted(a, h);
            h /= 3;
        }
        assert isSorted(a);
    }

    // Is v < w?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // Exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // Check if array is sorted
    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }

    // Is the array h-sorted?
    private static boolean isHsorted(Comparable[] a, int h) {
        for (int i = h; i < a.length; i++)
            if (less(a[i], a[i - h])) return false;
        return true;
    }

    // Print array to StdOut
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i]);
    }

    // Reads in a sequence of strings from standard input and shellsorts them
    // Prints them into StdOut in ascending order
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        ShellSort.sort(a);
        show(a);
    }
}
