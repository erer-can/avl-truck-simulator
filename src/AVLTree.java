/**
 * Implements an AVL tree to store and manage ParkingLot objects based on their capacity constraints.
 * Provides methods for inserting, deleting, and searching for ParkingLots, while maintaining balance.
 */
public class AVLTree {

    /**
     * Represents a node in the AVL tree, containing a ParkingLot and height information.
     */
    private static class Node {
        int capacityConstraint; // Capacity constraint for the ParkingLot in this node
        int height; // Height of this node in the AVL tree
        Node left, right; // Left and right child nodes
        ParkingLot parkingLot; // ParkingLot object stored in this node

        /**
         * Constructs a Node containing a specified ParkingLot.
         * Initializes height to 1 (leaf node).
         *
         * @param parkingLot the ParkingLot object to store in this node
         */
        Node(ParkingLot parkingLot) {
            this.capacityConstraint = parkingLot.getCapacityConstraint();
            this.height = 1; // Initialize height to 1 for a new leaf node
            this.parkingLot = parkingLot;
        }
    }

    private Node root; // Root of the AVL tree

    /**
     * Inserts a ParkingLot into the AVL tree.
     *
     * @param parkingLot the ParkingLot to insert
     */
    public void insert(ParkingLot parkingLot) {
        root = insert(root, parkingLot); // Start insertion from the root
    }

    /**
     * Deletes a ParkingLot from the AVL tree by its capacity constraint.
     *
     * @param parkingLot the ParkingLot to delete
     */
    public void delete(ParkingLot parkingLot) {
        root = delete(root, parkingLot.getCapacityConstraint()); // Start deletion from the root
    }

    /**
     * Searches for a ParkingLot in the AVL tree by its capacity constraint.
     *
     * @param capacityConstraint the capacity constraint of the target ParkingLot
     * @return the ParkingLot if found; {@code null} otherwise
     */
    public ParkingLot search(int capacityConstraint) {
        Node result = search(root, capacityConstraint); // Start search from the root
        return (result != null) ? result.parkingLot : null; // Return the ParkingLot if found
    }

    /**
     * Updates the height of a node based on its children's heights.
     *
     * @param node the node to update
     */
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right)); // Update height based on children's heights
    }

    /**
     * Gets the height of a node.
     *
     * @param node the node to check
     * @return the height of the node, or 0 if null
     */
    private int getHeight(Node node) {
        return (node == null) ? 0 : node.height; // Return 0 if node is null, otherwise return node's height
    }

    /**
     * Calculates the balance factor of a node.
     *
     * @param node the node to calculate the balance factor for
     * @return the balance factor (height difference) of the node's left and right children
     */
    private int getBalanceFactor(Node node) {
        if (node == null) return 0; // Balance factor is 0 if node is null
        return getHeight(node.left) - getHeight(node.right); // Calculate balance factor
    }

    /**
     * Performs a right rotation on a subtree rooted at a given node.
     *
     * @param y the root of the subtree to rotate
     * @return the new root of the rotated subtree
     */
    private Node rightRotate(Node y) {
        Node x = y.left; // Set x as the left child of y
        Node T2 = x.right; // Set T2 as the right child of x

        x.right = y; // Perform rotation
        y.left = T2; // Update left child of y

        updateHeight(y); // Update height of y
        updateHeight(x); // Update height of x

        return x; // Return new root
    }

    /**
     * Performs a left rotation on a subtree rooted at a given node.
     *
     * @param x the root of the subtree to rotate
     * @return the new root of the rotated subtree
     */
    private Node leftRotate(Node x) {
        Node y = x.right; // Set y as the right child of x
        Node T2 = y.left; // Set T2 as the left child of y

        y.left = x; // Perform rotation
        x.right = T2; // Update right child of x

        updateHeight(x); // Update height of x
        updateHeight(y); // Update height of y

        return y; // Return new root
    }

    /**
     * Finds the node with the minimum capacity constraint in a subtree.
     *
     * @param node the root of the subtree
     * @return the node with the smallest capacity constraint
     */
    private Node getMinValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left; // Traverse to the leftmost node
        }
        return current; // Return the leftmost node
    }

    /**
     * Rebalances the tree after insertion.
     *
     * @param node the node to rebalance
     * @param key the key that caused the imbalance
     * @return the new root of the subtree
     */
    private Node rebalance(Node node, int key) {
        int balance = getBalanceFactor(node); // Get balance factor

        // Perform rotations based on balance factor
        if (balance > 1 && key < node.left.capacityConstraint) return rightRotate(node);
        if (balance < -1 && key > node.right.capacityConstraint) return leftRotate(node);
        if (balance > 1 && key > node.left.capacityConstraint) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && key < node.right.capacityConstraint) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node; // Return the (possibly new) root
    }

    /**
     * Rebalances the tree after deletion.
     *
     * @param node the node to rebalance
     * @return the new root of the subtree
     */
    private Node rebalanceAfterDeletion(Node node) {
        int balance = getBalanceFactor(node); // Get balance factor

        // Perform rotations based on balance factor
        if (balance > 1 && getBalanceFactor(node.left) >= 0) return rightRotate(node);
        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalanceFactor(node.right) <= 0) return leftRotate(node);
        if (balance < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node; // Return the (possibly new) root
    }

    /**
     * Inserts a ParkingLot into the subtree rooted at a given node.
     *
     * @param node the root of the subtree
     * @param parkingLot the ParkingLot to insert
     * @return the new root of the subtree
     */
    private Node insert(Node node, ParkingLot parkingLot) {
        if (node == null) return new Node(parkingLot); // Create a new node if the subtree is empty

        // Traverse the tree to find the correct position for the new node
        if (parkingLot.getCapacityConstraint() < node.capacityConstraint) {
            node.left = insert(node.left, parkingLot);
        } else if (parkingLot.getCapacityConstraint() > node.capacityConstraint) {
            node.right = insert(node.right, parkingLot);
        } else {
            return node; // Duplicate keys are not allowed
        }

        updateHeight(node); // Update height of the current node
        return rebalance(node, parkingLot.getCapacityConstraint()); // Rebalance the tree and return the new root
    }

    /**
     * Deletes a ParkingLot with a specific capacity constraint from the subtree.
     *
     * @param node the root of the subtree
     * @param key the capacity constraint of the ParkingLot to delete
     * @return the new root of the subtree
     */
    private Node delete(Node node, int key) {
        if (node == null) return null; // Return null if the subtree is empty

        // Traverse the tree to find the node to delete
        if (key < node.capacityConstraint) {
            node.left = delete(node.left, key);
        } else if (key > node.capacityConstraint) {
            node.right = delete(node.right, key);
        } else {
            // Node to be deleted found
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right; // Replace node with its non-null child
            } else {
                Node successor = getMinValueNode(node.right); // Find the in-order successor
                node.capacityConstraint = successor.capacityConstraint; // Copy successor's data to the current node
                node.parkingLot = successor.parkingLot; // Copy successor's data to the current node
                node.right = delete(node.right, successor.capacityConstraint); // Delete the successor
            }
        }

        if (node == null) return null; // Return null if the node was deleted

        updateHeight(node); // Update height of the current node
        return rebalanceAfterDeletion(node); // Rebalance the tree and return the new root
    }

    private Node search(Node node, int capacityConstraint) {
        if (node == null || node.capacityConstraint == capacityConstraint) return node; // Return node if found or null if not found
        return (capacityConstraint < node.capacityConstraint) ? search(node.left, capacityConstraint) : search(node.right, capacityConstraint); // Traverse the tree
    }

    /**
     * Finds the largest node with a capacity constraint smaller than a given value.
     *
     * @param capacityConstraint the reference capacity constraint
     * @return the ParkingLot with the largest smaller capacity, or null if none found
     */
    public ParkingLot findLargestSmaller(int capacityConstraint) {
        Node result = findLargestSmaller(root, capacityConstraint); // Start search from the root
        return (result != null) ? result.parkingLot : null; // Return the ParkingLot if found
    }

    private Node findLargestSmaller(Node node, int capacityConstraint) {
        Node current = node;
        Node largestSmaller = null;

        while (current != null) {
            if (current.capacityConstraint < capacityConstraint) {
                largestSmaller = current; // Update largestSmaller if current node is smaller than the given value
                current = current.right; // Move to the right child
            } else {
                current = current.left; // Move to the left child
            }
        }
        return largestSmaller; // Return the largest smaller node
    }

    /**
     * Finds the smallest node with a capacity constraint larger than a given value.
     *
     * @param capacityConstraint the reference capacity constraint
     * @return the ParkingLot with the smallest larger capacity, or null if none found
     */
    public ParkingLot findSmallestLarger(int capacityConstraint) {
        Node result = findSmallestLarger(root, capacityConstraint); // Start search from the root
        return (result != null) ? result.parkingLot : null; // Return the ParkingLot if found
    }

    private Node findSmallestLarger(Node node, int capacityConstraint) {
        Node current = node;
        Node smallestLarger = null;

        while (current != null) {
            if (current.capacityConstraint > capacityConstraint) {
                smallestLarger = current; // Update smallestLarger if current node is larger than the given value
                current = current.left; // Move to the left child
            } else {
                current = current.right; // Move to the right child
            }
        }
        return smallestLarger; // Return the smallest larger node
    }
}