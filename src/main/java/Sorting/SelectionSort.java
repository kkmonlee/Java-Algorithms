package Sorting;

/**
 * Selection sort is a simple sorting algorithm but is slower.
 *
 * @author kkmonlee
 */
public class SelectionSort {
    /**
     * Selection sort (descending order)
     * @param array array to be sorted
     */
    public static void selectionSort(int[] array) {
        for (int i = 0; i  < array.length - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] > array[maxIndex]) maxIndex = j;
            }
            int tmp = array[i];
            array[i] = array[maxIndex];
            array[maxIndex] = tmp;
        }
    }
}
