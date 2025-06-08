/**
 * A Queue implementation for managing Truck objects, with operations to add, remove,
 * and inspect trucks in a first-in-first-out (FIFO) manner.
 */
public class Queue {

    /**
     * Represents a node in the queue, holding a Truck and a reference to the next node.
     */
    private static class Node {
        Truck truck; // The truck held by this node
        Node next;   // The next node in the queue

        /**
         * Constructs a Node with the specified Truck.
         *
         * @param truck the Truck to store in this node
         */
        Node(Truck truck) {
            this.truck = truck;
            this.next = null;
        }
    }

    private Node front, rear; // Pointers to the front and rear of the queue
    private int size;         // Current number of trucks in the queue

    /**
     * Constructs an empty Queue.
     */
    public Queue() {
        this.front = this.rear = null;
        this.size = 0;
    }

    /**
     * Adds a truck to the rear of the queue.
     *
     * @param truck the Truck to add to the queue
     */
    public void enqueue(Truck truck) {
        Node newNode = new Node(truck);
        if (rear == null) {
            // If the queue is empty, the new node is both front and rear
            front = rear = newNode;
        } else {
            // Attach the new node to the end and update the rear pointer
            rear.next = newNode;
            rear = newNode;
        }
        size++; // Increment size after adding the truck
    }

    /**
     * Removes and returns the truck at the front of the queue.
     *
     * @return the Truck at the front of the queue, or {@code null} if the queue is empty
     */
    public Truck dequeue() {
        if (front == null) {
            return null; // Queue is empty
        }
        Truck truck = front.truck;
        front = front.next; // Move front to the next node
        if (front == null) {
            rear = null; // If the queue is now empty, rear is also set to null
        }
        size--; // Decrement size after removing the truck
        return truck;
    }

    /**
     * Returns the truck at the front of the queue without removing it.
     *
     * @return the Truck at the front of the queue, or {@code null} if the queue is empty
     */
    public Truck peek() {
        if (front == null) {
            return null;
        }
        return front.truck;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return {@code true} if the queue has no trucks; {@code false} otherwise
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Gets the current number of trucks in the queue.
     *
     * @return the number of trucks in the queue
     */
    public int size() {
        return size;
    }
}
