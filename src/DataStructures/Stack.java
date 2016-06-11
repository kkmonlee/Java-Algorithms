package DataStructures;

/**
 * Stack is a simple data structure using LIFO (last in first out)
 * The element inserted into the structure as the last one will be removed
 * as the first one.
 *
 * @author kkmonlee
 */

public class Stack {
    private Node first;
    private int size;

    public Stack() {
        this.size = 0;
    }

    /**
     * Inserts the element at the top of the stack
     * Complexity - O(1)
     * @param i element to be stored
     */
    public void push(int i) {
        Node n = new Node(i);

        Node currFirst = first;
        first = n;
        n.next = currFirst;

        size++;
    }

    /**
     * Removes the element, which is at the top of the stack
     * Complexity - O(1)
     * @return value of the element
     */
    public int pop() {
        if (size == 0) {
            throw new IllegalStateException("Stack is empty.");
        }
        int value = first.value;
        first = first.next;
        size--;

        return value;
    }

    /**
     * Return the value of the element at the top of the stack
     * Complexity - O(1)
     * @return value of the element at the top of the stack
     */
    public int top() {
        if (size == 0) {
            throw new IllegalStateException("Stack is empty.");
        }
        return first.value;
    }

    /**
     * Returns the size of the stack
     * @return size of the stack
     */
    public int getSize() {
        return this.size;
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
