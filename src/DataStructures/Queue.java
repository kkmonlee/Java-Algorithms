package DataStructures;

/**
 * Queue is a data container in which stored elements are removed in the same
 * order as they were inserted into the structure. FIFO (First In First Out)
 *
 * @author kkmonlee
 */

public class Queue {
    private Node first;
    private Node last;
    private int size;

    public Queue() {
        this.size = 0;
    }

    /**
     * Insert the element at the end of the queue
     * @param i element
     */
    public void addLast(int i) {
        Node n = new Node(i);

        if (getSize() == 0) {
            this.first = n;
            this.last = n;
        } else {
            this.last.next = n;
            this.last = n;
        }
        size++;
    }

    /**
     * Remove the first element
     * @return element
     */
    public int deleteFirst() {
        if (getSize() == 0) throw new IllegalStateException("Queue is empty.");

        int value = first.value;
        first = first.next;

        size--;
        return value;
    }

    /**
     * Return the first element
     * @return element
     */
    public int getFirst() {
        if (getSize() == 0) throw new IllegalStateException("Queue is empty.");
        return first.value;
    }

    /**
     * Get number of contained elements
     * @return length of the queue
     */
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Node curr = first;
        for (int i = 0; i < this.size; i++) {
            builder.append(curr.value).append(" ");
            curr = curr.next;
        }
        return builder.toString();
    }

    /**
     * Inner representation of the element (node of the linked list)
     */
    private class Node {
        private int value;
        private Node next;

        private Node(int value) {
            this.value = value;
        }
    }

}
