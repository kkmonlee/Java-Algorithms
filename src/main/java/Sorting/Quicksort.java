package Sorting;

/**
 * Quicksort algorithm picks 1 random element of the input array (pivot)
 * It reorganises the array in such a way that all elements with higher
 * value than the pivot are located before the pivot and all elements lower
 * value than the pivot are after it.
 *
 * Complexity
 *  O(n^2)
 *
 * @author kkmonlee
 */
public class Quicksort {

    /**
     * Quicksort - pivot the first element, descending order
     * @param array array to be sorted
     * @param left index of the first element which can be edited
     * @param right index of the first element which we can't edit
     */
    public static void quicksort(int[] array, int left, int right) {
        if (left < right) {
            int boundary = left;

            for (int i = left + 1; i < right; i++) {
                if (array[i] > array[left]) {
                    swap(array, i, boundary++);
                }
            }

            swap(array, left, boundary);
            quicksort(array, left, boundary);
            quicksort(array, boundary + 1, right);
        }
    }

    /**
     * Swaps the elements of the array
     * @param array array
     * @param left index of the first element
     * @param right index of the second element
     */
    private static void swap(int[] array, int left, int right) {
        int temp = array[right];

        array[right] = array[left];
        array[left] = temp;
    }

}
