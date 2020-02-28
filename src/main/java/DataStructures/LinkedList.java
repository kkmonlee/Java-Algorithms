package DataStructures;

/**
 * Linked List is a common structure used to store data of a variable length
 *
 * @author kkmonlee
 */

public class LinkedList {

    private Node first;
    private Node last;
    private int size;

    /**
     * Constructor
     */
    public LinkedList() {
        this.size = 0;
    }

    /**
     * Insert the element at the end of the list
     * @param i element
     */
    public void add(int i) {
        Node n = new Node(i);
        if (size == 0) {
            this.first = n;
            this.last = n;
        } else {
            this.last.next = n;
            this.last = n;
        }

        size++;
    }

    /**
     * Return value at index i
     * @param i index
     * @throws IndexOutOfBoundsException if the index is greater or equal to the size of the list
     * @throws IllegalArgumentException if i is negative
     * @return value at index i
     */
    public int get(int i) {
        if (i >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i + ", size:" + this.size);
        }
        if (i < 0) {
            throw new IllegalArgumentException("Index is less than 0");
        }
        Node curr = first;
        for (int j = 0; j < i; j++) {
            curr = curr.next;
        }
        return curr.value;
    }

    /**
     * Deletes value at index i
     * @param i index of the element to be deleted
     * @throws IndexOutOfBoundsException if the index is greater or equal to the size of the list
     * @throws IllegalArgumentException if i is negative
     */
    public void remove(int i) {
        if (i >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + i + ", size:" + this.size);
        }
        if (i < 0) {
            throw new IllegalArgumentException("Index is less than 0");
        }
        if (i == 0) {
            first = first.next;
        } else {
            Node curr = first;
            for (int j = 0; j < i - 1; j++) { //find the previous one
                curr = curr.next;
            }
            curr.next = curr.next.next; //and step over the deleted element
            if (i == size - 1) { //if we are deleting the last element
                last = curr;
            }
        }
        size--;
    }

    /**
     * Returns the total size of the list
     * @return size
     */
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node curr = first;
        for (int i = 0; i < this.size; i++) {
            builder.append(curr.value);
            builder.append(" ");
            curr = curr.next;
        }
        return builder.toString();
    }


    private class Node {

        private int value;
        private Node next;

        private Node(int value) {
            this.value = value;
        }
    }
}
