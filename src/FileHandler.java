import java.io.*;

/**
 * Handles file input and output operations for processing parking lot and truck commands.
 * Reads commands from an input file, processes them through CommandHandler, and writes
 * results to an output file.
 */
public class FileHandler {
    private final CommandHandler commandHandler; // Manages commands related to parking lots and trucks

    /**
     * Constructs a FileHandler and initializes a CommandHandler to process commands.
     */
    public FileHandler() {
        this.commandHandler = new CommandHandler();
    }

    /**
     * Processes commands from an input file and writes results to an output file.
     * Each line in the input file represents a command with optional parameters.
     *
     * @param inputFilePath  the path to the input file containing commands
     * @param outputFilePath the path to the output file for writing command results
     */
    public void processInputFile(String inputFilePath, String outputFilePath) {
        // Try-with-resources ensures BufferedReader and BufferedWriter are closed after use
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            // Read each line from the input file
            while ((line = reader.readLine()) != null) {
                // Split the line into command and parameters
                String[] tokens = line.split(" ");
                String command = tokens[0];
                String output;
                int capacityConstraint;

                // Process the command based on its type
                switch (command) {
                    case "create_parking_lot":
                        // Create a new parking lot with the specified capacity and truck limit
                        capacityConstraint = Integer.parseInt(tokens[1]);
                        int truckLimit = Integer.parseInt(tokens[2]);
                        commandHandler.createParkingLot(capacityConstraint, truckLimit);
                        break;

                    case "delete_parking_lot":
                        // Delete the parking lot with the specified capacity constraint
                        capacityConstraint = Integer.parseInt(tokens[1]);
                        commandHandler.deleteParkingLot(capacityConstraint);
                        break;

                    case "add_truck":
                        // Add a new truck with the specified ID and capacity
                        int truckId = Integer.parseInt(tokens[1]);
                        int capacity = Integer.parseInt(tokens[2]);
                        Truck new_truck = new Truck(truckId, capacity);
                        int out_truck = commandHandler.addTruck(new_truck);
                        output = String.valueOf(out_truck);
                        writer.write(output);
                        writer.newLine(); // Write output to file and start a new line
                        break;

                    case "ready":
                        // Mark the parking lot with the specified capacity constraint as ready
                        capacityConstraint = Integer.parseInt(tokens[1]);
                        output = commandHandler.ready(capacityConstraint);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "load":
                        // Load the specified amount into the parking lot with the given capacity constraint
                        capacityConstraint = Integer.parseInt(tokens[1]);
                        int loadAmount = Integer.parseInt(tokens[2]);
                        output = commandHandler.load(capacityConstraint, loadAmount);
                        writer.write(output);
                        writer.newLine();
                        break;

                    case "count":
                        // Count the number of trucks in the parking lot with the specified capacity constraint
                        capacityConstraint = Integer.parseInt(tokens[1]);
                        int truckCount = commandHandler.count(capacityConstraint);
                        output = String.valueOf(truckCount);
                        writer.write(output);
                        writer.newLine();
                        break;

                    default:
                        // Outputs an error message if an unknown command is encountered
                        System.out.println("Unknown command: " + command);
                }
            }
        } catch (IOException e) {
            // Print stack trace if an I/O error occurs
            e.printStackTrace();
        }
    }
}