# AVL Truck Simulator – Parking Lot Management with AVL Trees

**Course:** CMPE250 – Data Structures & Algorithms  
**Institution:** Boğaziçi University

---

## Project Overview

The **AVL Truck Simulator** is a Java-based application simulating a dynamic parking lot system. It leverages a self-balancing **AVL Tree** to manage parking lots and trucks, ensuring efficient operations even as data scales. Key functionalities are exposed through a command-driven interface, enabling batch processing of parking lot and truck operations.

---

##  Repository Structure

```
avl-truck-simulator/
├── src/
│   ├── Main.java            # Entry point
│   ├── AVLTree.java         # AVL tree implementation for ParkingLot nodes
│   ├── CommandHandler.java  # Parses and executes commands
│   ├── FileHandler.java     # Reads commands from input files
│   ├── FileComparator.java  # Utility to compare expected vs. actual outputs
│   ├── ParkingLot.java      # Defines parking lot properties and queues
│   ├── Queue.java           # Simple FIFO queue for Truck instances
│   └── Truck.java           # Represents a truck entity
├── test-cases/
│   ├── inputs/              # Sample input command files
│   └── outputs/             # Expected output files
├── README.md                # Project documentation (this file)
├── .gitignore               # Common ignore patterns
└── LICENSE                  # Project license (MIT)
```

---

## Core Components

### **AVLTree** (`AVLTree.java`)
- Implements a self-balancing binary search tree.
- Stores `ParkingLot` nodes keyed by `capacityConstraint`.
- Provides **O(log n)** insertion, deletion, and search.

### **ParkingLot** (`ParkingLot.java`)
- Attributes:
  - `capacityConstraint` (int): Maximum cargo per truck.
  - `truckLimit` (int): Maximum number of trucks.
- Contains two `Queue<Truck>`:
  - `waitingSection`: Trucks waiting to load.
  - `readySection`: Trucks ready to load or depart.
- Maintains count of active trucks.

### **Truck** (`Truck.java`)
- Attributes:
  - `truckId` (int): Unique identifier.
  - `capacity` (int): Maximum capacity.
  - `currentLoad` (int): Load accumulated.

### **Queue** (`Queue.java`)
- A generic FIFO queue supporting enqueue, dequeue, and isEmpty.

### **CommandHandler** (`CommandHandler.java`)
- Manages four AVL trees for:
  1. All parking lots.
  2. Non-full parking lots.
  3. Parking lots with waiting trucks.
  4. Parking lots with ready trucks.
- Executes commands:
  - `create_parking_lot`
  - `delete_parking_lot`
  - `add_truck`
  - `ready`
  - `load`
  - `count`

### **FileHandler** & **FileComparator**
- `FileHandler`: Reads input commands line-by-line, invoking `CommandHandler`.
- `FileComparator`: Verifies actual outputs against expected results in `test-cases/outputs`.

---

##  Command Reference

All commands are provided in an input file. Each output is written to standard output.

| Command                                  | Description                                                                                             | Output                                |
|------------------------------------------|---------------------------------------------------------------------------------------------------------|---------------------------------------|
| `create_parking_lot <capacity> <limit>`  | Adds a new parking lot if none exists with that capacity.                                               | (none)                                |
| `delete_parking_lot <capacity>`          | Removes an existing parking lot by capacity.                                                            | (none)                                |
| `add_truck <truckId> <capacity>`         | Registers a truck and assigns to the smallest fitting parking lot’s waiting queue.                       | `<assignedLotCapacity>` or `-1`       |
| `ready <capacity>`                       | Moves one truck from waiting to ready in the specified or next available lot.                           | `<truckId> <lotCapacity>` or `-1`     |
| `load <capacity> <loadAmount>`           | Distributes `loadAmount` among ready trucks, starting at given lot, rebalancing as trucks depart/full.   | Sequence of `<truckId> <lotCapacity>` or `-1` |
| `count <capacity>`                       | Counts all trucks in parking lots with `capacityConstraint >= capacity`.                                | `<count>`                             |

---

##  Build & Run Instructions

1. **Compile** all source files:
    ```bash
    javac src/*.java
    ```
2. **Execute** the simulator with an input file:
    ```bash
    java -cp src Main < test-cases/inputs/sample_commands.txt > actual_output.txt
    ```
3. **Validate** results:
    ```bash
    java -cp src FileComparator actual_output.txt test-cases/outputs/expected_output.txt
    ```

##  License

This project is licensed under the MIT License.  
