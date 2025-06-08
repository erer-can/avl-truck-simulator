/**
 * Represents a parking lot with separate sections for waiting and ready trucks.
 * Provides methods for managing truck movements and checking capacity limits.
 */
public class ParkingLot {
    private int capacityConstraint; // Maximum load capacity per truck in the parking lot
    private int truckLimit; // Maximum number of trucks allowed in the parking lot
    private Queue waitingSection; // Queue of trucks waiting for readiness
    private Queue readySection; // Queue of trucks ready for departure or loading

    /**
     * Constructs a ParkingLot instance with specified capacity constraints and truck limits.
     * Initializes separate queues for waiting and ready trucks.
     *
     * @param capacityConstraint the load capacity limit for each truck
     * @param truckLimit the maximum number of trucks allowed in the parking lot
     */
    public ParkingLot(int capacityConstraint, int truckLimit) {
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
        this.waitingSection = new Queue();
        this.readySection = new Queue();
    }

    /**
     * Gets the capacity constraint for trucks in the parking lot.
     *
     * @return the load capacity limit per truck
     */
    public int getCapacityConstraint() {
        return capacityConstraint;
    }

    /**
     * Gets the queue representing the waiting section of the parking lot.
     *
     * @return the queue of trucks waiting
     */
    public Queue getWaitingSection() {
        return waitingSection;
    }

    /**
     * Gets the queue representing the ready section of the parking lot.
     *
     * @return the queue of trucks ready for departure or loading
     */
    public Queue getReadySection() {
        return readySection;
    }

    /**
     * Adds a truck to the waiting section of the parking lot.
     *
     * @param truck the truck to add to the waiting section
     */
    public void addTruckToWaiting(Truck truck) {
        waitingSection.enqueue(truck);
    }

    /**
     * Moves a truck from the waiting section to the ready section, if available.
     */
    public void moveTruckToReady() {
        if (!waitingSection.isEmpty()) {
            // Dequeue from waiting and enqueue to ready section
            Truck truck = waitingSection.dequeue();
            readySection.enqueue(truck);
        }
    }

    /**
     * Checks if the parking lot has space for additional trucks.
     *
     * @return {@code true} if the total number of trucks is below the truck limit; {@code false} otherwise
     */
    public boolean hasSpace() {
        return (waitingSection.size() + readySection.size()) < truckLimit;
    }

    /**
     * Checks if the waiting section is empty.
     *
     * @return {@code true} if there are no trucks in the waiting section; {@code false} otherwise
     */
    public boolean isWaitingListEmpty() {
        return waitingSection.isEmpty();
    }

    /**
     * Checks if there are any trucks in the waiting section.
     *
     * @return {@code true} if there are trucks in the waiting section; {@code false} otherwise
     */
    public boolean hasWaitingTrucks() {
        return !waitingSection.isEmpty();
    }

    /**
     * Gets the total count of trucks in both the waiting and ready sections.
     *
     * @return the total number of trucks in the parking lot
     */
    public int getTotalTruckCount() {
        return waitingSection.size() + readySection.size();
    }
}
