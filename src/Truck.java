/**
 * Represents a truck with a unique ID, maximum capacity, and a current load.
 * Provides methods to manage and retrieve truck load information.
 */
public class Truck {
    private final int truckId; // Unique identifier for the truck
    private final int maxCapacity; // Maximum capacity of the truck
    private int currentLoad; // Current load of the truck

    /**
     * Constructs a Truck instance with the specified ID and maximum capacity.
     *
     * @param truckId the unique identifier for the truck
     * @param maxCapacity the maximum load capacity of the truck
     */
    public Truck(int truckId, int maxCapacity) {
        this.truckId = truckId;
        this.maxCapacity = maxCapacity;
        this.currentLoad = 0; // Initialize the truck with no load
    }

    /**
     * Gets the unique ID of the truck.
     *
     * @return the truck's unique identifier
     */
    public int getTruckId() {
        return truckId;
    }

    /**
     * Calculates the remaining capacity of the truck.
     *
     * @return the remaining capacity that can be loaded into the truck
     */
    public int getRemainingCapacity() {
        return maxCapacity - currentLoad;
    }

    /**
     * Sets the current load of the truck. Ensures that the load does not exceed
     * the truck's maximum capacity.
     *
     * @param loadAmount the new load amount to set
     */
    public void setCurrentLoad(int loadAmount) {
        // Prevent overloading by setting the load to the minimum of the specified load or max capacity
        currentLoad = Math.min(loadAmount, maxCapacity);
    }

    /**
     * Checks if the truck is full.
     *
     * @return {@code true} if the current load is equal to or greater than the max capacity; {@code false} otherwise
     */
    public boolean isFull() {
        return (currentLoad >= maxCapacity);
    }

    /**
     * Gets the current load of the truck.
     *
     * @return the current load amount in the truck
     */
    public int getCurrentLoad() {
        return currentLoad;
    }
}
