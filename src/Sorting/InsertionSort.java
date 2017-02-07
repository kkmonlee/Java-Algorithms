package Sorting;

public class InsertionSort {
    /**
     * Insertion sort (descending order)
     * @param array array to be sorted
     */
    public static void insertionSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int j = i + 1;
            int temp = array[j];

            while (j > 0 && temp > array[j - 1]) {
                array[j] = array[j - 1];
                j--;
            }
            array[j] = temp;
        }
    }

}
