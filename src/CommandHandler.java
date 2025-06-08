/**
 * Handles commands related to managing parking lots and trucks using AVL trees
 * for efficient storage and retrieval.
 */
public class CommandHandler {
    private AVLTree parkingLots; // Stores all parking lots by capacity constraint
    private AVLTree notEmptyWaitingQueueParkingLots; // Stores parking lots with non-empty waiting queues
    private AVLTree notFullParkingLots; // Stores parking lots that are not at full capacity
    private AVLTree notEmptyReadyQueueParkingLots; // Stores parking lots with non-empty ready queues

    /**
     * Constructs a CommandHandler, initializing AVL trees to manage parking lots.
     */
    public CommandHandler() {
        this.parkingLots = new AVLTree();
        this.notEmptyWaitingQueueParkingLots = new AVLTree();
        this.notFullParkingLots = new AVLTree();
        this.notEmptyReadyQueueParkingLots = new AVLTree();
    }

    /**
     * Creates a new parking lot if one with the specified capacity constraint does not exist.
     * Adds the new parking lot to the general list and to the list of non-full lots.
     *
     * @param capacityConstraint the capacity constraint for trucks in the parking lot
     * @param truckLimit the maximum number of trucks allowed in the parking lot
     */
    public void createParkingLot(int capacityConstraint, int truckLimit) {
        // Search for an existing parking lot with the given capacity constraint
        ParkingLot existingLot = parkingLots.search(capacityConstraint);
        if (existingLot == null) {
            // Create a new parking lot if none exists
            ParkingLot newLot = new ParkingLot(capacityConstraint, truckLimit);
            // Insert the new parking lot into the AVL trees
            parkingLots.insert(newLot);
            notFullParkingLots.insert(newLot);
        }
    }

    /**
     * Deletes a parking lot with the specified capacity constraint from all relevant AVL trees.
     *
     * @param capacityConstraint the capacity constraint of the parking lot to delete
     */
    public void deleteParkingLot(int capacityConstraint) {
        // Search for the parking lot with the given capacity constraint
        ParkingLot existingLot = parkingLots.search(capacityConstraint);
        if (existingLot != null) {
            // Remove the parking lot from all AVL trees where it may exist
            parkingLots.delete(existingLot);
            notEmptyWaitingQueueParkingLots.delete(existingLot);
            notEmptyReadyQueueParkingLots.delete(existingLot);
            notFullParkingLots.delete(existingLot);
        }
    }

    /**
     * Adds a truck to an appropriate parking lot with a matching or smaller capacity constraint.
     * If a parking lot is found, the truck is added to the waiting section, and relevant trees are updated.
     *
     * @param newTruck the truck to add
     * @return the capacity constraint of the parking lot where the truck was added, or -1 if no suitable lot exists
     */
    public int addTruck(Truck newTruck) {
        int capacity = newTruck.getRemainingCapacity();
        // Search for a parking lot with a matching capacity constraint
        ParkingLot targetLot = notFullParkingLots.search(capacity);

        if (targetLot != null) {
            // If the waiting list is empty, insert the lot into the notEmptyWaitingQueueParkingLots tree
            if (targetLot.isWaitingListEmpty()) {
                notEmptyWaitingQueueParkingLots.insert(targetLot);
            }
            // Add the truck to the waiting section of the parking lot
            targetLot.addTruckToWaiting(newTruck);
            // If the parking lot is now full, remove it from the notFullParkingLots tree
            if (!targetLot.hasSpace()) {
                notFullParkingLots.delete(targetLot);
            }
            return capacity;
        }

        // If no exact match is found, search for the largest smaller capacity constraint
        targetLot = notFullParkingLots.findLargestSmaller(capacity);
        if (targetLot != null) {
            if (targetLot.isWaitingListEmpty()) {
                notEmptyWaitingQueueParkingLots.insert(targetLot);
            }
            targetLot.addTruckToWaiting(newTruck);
            if (!targetLot.hasSpace()) {
                notFullParkingLots.delete(targetLot);
            }
            return targetLot.getCapacityConstraint();
        }
        return -1; // No suitable parking lot found
    }

    /**
     * Moves a truck from the waiting section to the ready section of a parking lot with a specified capacity.
     * If the specified capacity has no trucks, attempts to find the next larger capacity with a truck.
     *
     * @param capacityConstraint the capacity constraint of the target parking lot
     * @return a string with the truck's ID and parking lot capacity if successful, or "-1" if no truck is available
     */
    public String ready(int capacityConstraint) {
        // Search for a parking lot with the given capacity constraint in the notEmptyWaitingQueueParkingLots tree
        ParkingLot targetLot = notEmptyWaitingQueueParkingLots.search(capacityConstraint);

        if (targetLot != null) {
            // Peek at the first truck in the waiting section
            Truck readyTruck = targetLot.getWaitingSection().peek();
            if (readyTruck != null) {
                // Move the truck to the ready section
                targetLot.moveTruckToReady();
                // If the ready section now has one truck, insert the lot into the notEmptyReadyQueueParkingLots tree
                if (targetLot.getReadySection().size() == 1) {
                    notEmptyReadyQueueParkingLots.insert(targetLot);
                }
                // If the waiting list is now empty, remove the lot from the notEmptyWaitingQueueParkingLots tree
                if (targetLot.isWaitingListEmpty()) {
                    notEmptyWaitingQueueParkingLots.delete(targetLot);
                }
                return readyTruck.getTruckId() + " " + capacityConstraint;
            }
        }

        // If no exact match is found, search for the smallest larger capacity constraint
        targetLot = notEmptyWaitingQueueParkingLots.findSmallestLarger(capacityConstraint);

        if (targetLot != null && targetLot.hasWaitingTrucks()) {
            Truck readyTruck = targetLot.getWaitingSection().peek();
            if (readyTruck != null) {
                targetLot.moveTruckToReady();
                if (targetLot.getReadySection().size() == 1) {
                    notEmptyReadyQueueParkingLots.insert(targetLot);
                }
                if (targetLot.isWaitingListEmpty()) {
                    notEmptyWaitingQueueParkingLots.delete(targetLot);
                }
                return readyTruck.getTruckId() + " " + targetLot.getCapacityConstraint();
            }
        }
        return "-1"; // No truck found to move to ready section
    }

    /**
     * Loads a specified amount into trucks in the ready section, starting from a parking lot with a given capacity.
     * Redistributes trucks if necessary after loading, and returns a summary of loaded trucks.
     *
     * @param capacityConstraint the capacity constraint of the starting parking lot
     * @param loadAmount the amount of load to distribute among ready trucks
     * @return a string summary of truck IDs and new parking lot capacities or "-1" if no trucks were loaded
     */
    public String load(int capacityConstraint, int loadAmount) {
        // Search for a parking lot with the given capacity constraint in the notEmptyReadyQueueParkingLots tree
        ParkingLot currentLot = notEmptyReadyQueueParkingLots.search(capacityConstraint);
        if (currentLot == null) {
            // If no exact match is found, search for the smallest larger capacity constraint
            currentLot = notEmptyReadyQueueParkingLots.findSmallestLarger(capacityConstraint);
        }
        StringBuilder output = new StringBuilder();

        while (loadAmount > 0 && currentLot != null) {
            while (loadAmount > 0 && !currentLot.getReadySection().isEmpty()) {
                // Dequeue a truck from the ready section
                Truck truck = currentLot.getReadySection().dequeue();
                // If the parking lot is not in the notFullParkingLots tree, insert it
                if (notFullParkingLots.search(currentLot.getCapacityConstraint()) == null) {
                    notFullParkingLots.insert(currentLot);
                }

                int remainingCapacity = truck.getRemainingCapacity();
                int lotCapacity = currentLot.getCapacityConstraint();
                // Calculate the load to give to the truck
                int loadToGive = Math.min(loadAmount, Math.min(lotCapacity, remainingCapacity));

                // Update the truck's current load
                truck.setCurrentLoad(truck.getCurrentLoad() + loadToGive);
                loadAmount -= loadToGive;

                if (truck.isFull()) {
                    truck.setCurrentLoad(0);
                }
                // Add the truck back to an appropriate parking lot
                int result = addTruck(truck);

                output.append(truck.getTruckId())
                        .append(" ")
                        .append(result != -1 ? result : "-1")
                        .append(" - ");
            }
            if (currentLot.getReadySection().isEmpty()) {
                // If the ready section is empty, remove the lot from the notEmptyReadyQueueParkingLots tree
                notEmptyReadyQueueParkingLots.delete(currentLot);
            }
            // Search for the next smallest larger capacity constraint
            currentLot = notEmptyReadyQueueParkingLots.findSmallestLarger(capacityConstraint);
        }
        return !output.isEmpty() ? output.substring(0, output.length() - 3) : "-1";
    }

    /**
     * Counts the total number of trucks in all parking lots with a capacity greater than the specified constraint.
     *
     * @param capacityConstraint the minimum capacity constraint for counting trucks
     * @return the total count of trucks in all matching parking lots
     */
    public int count(int capacityConstraint) {
        int truckCount = 0;
        // Search for the smallest larger capacity constraint in the parkingLots tree
        ParkingLot targetLot = parkingLots.findSmallestLarger(capacityConstraint);

        while (targetLot != null) {
            // Add the total truck count of the current lot to the overall count
            truckCount += targetLot.getTotalTruckCount();
            // Search for the next smallest larger capacity constraint
            targetLot = parkingLots.findSmallestLarger(targetLot.getCapacityConstraint());
        }
        return truckCount;
    }
}