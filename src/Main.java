public class Main {
    public static void main(String[] args) {
        // Set the input and output files from the command line arguments
        String inputFile = args[0];
        String outputFile = args[1];

        // Create a FileHandler instance to process the input file
        FileHandler fileHandler = new FileHandler();
        // Process the input file and write results to the output file
        fileHandler.processInputFile(inputFile, outputFile);
    }
}