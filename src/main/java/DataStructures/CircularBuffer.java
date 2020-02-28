package DataStructures;

/**
 * A circular buffer is an array based queue implementation used for
 * buffeing data flows. It has 1 array of fixed length and 2 pointers.
 * The first pointer points the first stored element
 * The second pointer points to the first empty position of the array.
 *
 * @author kkmonlee
 */
public class CircularBuffer<ENTITY> {

    private int size;
    private Object[] array;
    private int pointer; // First empty position

    /**
     * Constructor
     * @param length size of the buffer
     */
    public CircularBuffer(int length) {
        this.array = new Object[length];
        this.size = 0;
        pointer = 0;
    }

    /**
     * Add an element at the end of the array
     * @param i element
     */
    public void addLast(ENTITY i) {
        if (this.size == array.length) {
            throw new IllegalStateException("Buffer is full!");
        }
        array[pointer] = i;

        pointer = modulo((pointer + 1), array.length);
        size++;
    }

    /**
     * Return and delete the first element
     * @return first element
     */
    public ENTITY getFirst() {
        if (this.size == 0) {
            throw new IllegalStateException("Buffer is empty!");
        }
        ENTITY value = (ENTITY) array[modulo((pointer - size), array.length)];

        array[modulo((pointer - size), array.length)] = null;
        size--;

        return value;
    }

    /**
     * x = number mod(modulo)
     * @param number number
     * @param modulo modulo
     * @return the least non-negative remainder
     */
    private int modulo(int number, int modulo) {
        if (number >= 0) {
            return number % modulo;
        }
        int result = number % modulo;
        return result == 0 ? 0 : result + modulo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Content: ");
        for (int i = 0; i < size; i++) {
            builder.append(array[modulo((pointer - size + i), array.length)]).append(" ");
        }
        builder.append("\\nfirst index: ").append(modulo((pointer - size), array.length)).append(", last index:").append(pointer - 1).append(", size: ").append(size);
        return builder.toString();
    }

    /**
     * Number of occupied positions
     * @return number of elements present in the buffer
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Size of the buffer
     * @return maximal number of elements that could be stored in the buffer
     */
    public int getLength() {
        return array.length;
    }

}
