package advance;

import java.io.*;
import java.util.*;

public class AdvanceApp {
    // table data as key,value pairs
    private static List<List<String[]>> table = new ArrayList<>();
    private static String fileName = "";
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Check if file name was given when running the program
        if (args.length > 0) {
            fileName = args[0];
        } else {
            // Ask user for file name
            System.out.print("Input valid file name: ");
            fileName = scanner.nextLine();
        }
        
        // Try to read the file
        if (readFile()) {
            printTable();
            showMenu();
        } else {
            // If file doesn't exist, ask again
            while (!readFile()) {
                System.out.print("Input valid file name: ");
                fileName = scanner.nextLine();
            }
            printTable();
            showMenu();
        }
    }    
    
    // Read file and parse into table
    private static boolean readFile() {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return false;
            }
            
            table.clear(); // Clear existing data
            BufferedReader reader = new BufferedReader(new FileReader(file)); //for file reading
            String line;
            
            // Read each line from file
            while ((line = reader.readLine()) != null) {
                List<String[]> row = new ArrayList<>(); //list of string arrayss
                
                // Split by ),( to get cells
                String[] cells = line.split("\\),\\("); // regex escaping this is "),("
                
                for (String cell : cells) {
                    // Remove extra parentheses
                    cell = cell.replace("(", "").replace(")", "");
                    
                    // Split by comma to get key and value
                    String[] keyValue = cell.split(",", 2); // Split into max 2 parts
                    if (keyValue.length == 2) {
                        row.add(new String[]{keyValue[0], keyValue[1]});
                    } else {
                        // If no comma, treat whole thing as key with empty value
                        row.add(new String[]{cell, ""});
                    }
                }
                
                table.add(row);
            }
            reader.close();
            return true;
            
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }
    }
    
    // Save table back to file
    private static void saveFile() {
        try {
            //FileWriter: opens the file for writing (creates if doesn't exist, overwrites if exists)
            //PrintWriter: Wraps FileWriter to provide convenient print() and println()
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
            
            // Write each row
            for (int i = 0; i < table.size(); i++) {
                List<String[]> row = table.get(i);
                
                //Add parentheses around each cell
                for (int j = 0; j < row.size(); j++) {
                    String[] keyValue = row.get(j);
                    writer.print("(" + keyValue[0] + "," + keyValue[1] + ")");
                    if (j < row.size() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
            writer.close();
            
        } catch (IOException e) { // file writing errors
            System.out.println("Error saving file: " + e.getMessage());
        } catch (Exception e) { //other
            System.out.println("Unexpected error saving file: " + e.getMessage());
        }
    }
    
    // random generator
    public static String generateRandomASCII() {
        Random var0 = new Random();
        String var1 = "";
    
        for(int k = 0; k < 3; ++k) {
            char var3 = (char)(33 + var0.nextInt(94));
            var1 = var1 + var3;
        }
    
        return var1;
    }

    // Print table in nice format
    private static void printTable() {
        for (int i = 0; i < table.size(); i++) {
            List<String[]> row = table.get(i);
            // each cell in row
            for (int j = 0; j < row.size(); j++) {
                String[] keyValue = row.get(j);
                System.out.print(keyValue[0] + "," + keyValue[1]);
                if (j < row.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
    
    // Show main menu
    private static void showMenu() {
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("[ search ] - Search");
            System.out.println("[ edit ] - Edit");
            System.out.println("[ add_row ] - Add Row");
            System.out.println("[ print ] - Print");
            System.out.println("[ sort ] - Sort");
            System.out.println("[ reset ] - Reset");
            System.out.println("[ x ] - Exit");
            
            System.out.print("Choose option: ");
            String choice = scanner.nextLine();
            
            // Handle user choice
            switch (choice.toLowerCase()) {
                case "search":
                    searchTable();
                    break;
                case "edit":
                    editTable();
                    break;
                case "add_row":
                    addRow();
                    break;
                case "print":
                    printTable();
                    break;
                case "sort":
                    sortTable();
                    break;
                case "reset":
                    resetTable();
                    break;
                case "x":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
    
    // Search for text in table
    private static void searchTable() {
        try {
            System.out.print("Search term: ");
            String searchTerm = scanner.nextLine();
            
            if (searchTerm.isEmpty()) {
                System.out.println("Search term cannot be empty!");
                return;
            }
            
            boolean found = false;
            
            // Look through all cells
            for (int i = 0; i < table.size(); i++) {
                List<String[]> row = table.get(i);
                for (int j = 0; j < row.size(); j++) {
                    String[] keyValue = row.get(j);
                    String key = keyValue[0];
                    String value = keyValue[1];
                    
                    // Count occurrences in key
                    int keyCount = 0;
                    int index = 0;
                    // key.indexOf(searchTerm, index) : Searches for searchTerm in key, starting at position index
                    // -1 is substring is not found
                    while ((index = key.indexOf(searchTerm, index)) != -1) {
                        keyCount++;
                        index++;
                    }
                    
                    // Count occurrences in value
                    int valueCount = 0;
                    index = 0;
                    while ((index = value.indexOf(searchTerm, index)) != -1) {
                        valueCount++;
                        index++;
                    }
                    
                    // Print results based on where found
                    if (keyCount > 0 && valueCount > 0) {
                        System.out.println(keyCount + " <" + searchTerm + "> at key and " + valueCount + "<" + searchTerm + "> at value of [" + i + "," + j + "]");
                        found = true;
                    } else if (keyCount > 0) {
                        System.out.println(keyCount + " <" + searchTerm + "> at key of [" + i + "," + j + "]");
                        found = true;
                    } else if (valueCount > 0) {
                        System.out.println(valueCount + " <" + searchTerm + "> at value of [" + i + "," + j + "]");
                        found = true;
                    }
                }
            }
            
            if (!found) {
                System.out.println("Search term '" + searchTerm + "' not found in table");
            }
            
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }
    
    // Edit a cell in the table
    private static void editTable() {
        try {
            int row, col;
    
            // === FIRST PROMPT: row,col ===
            while (true) {
                System.out.print("Edit cell [row,col]: ");
                String input = scanner.nextLine();
    
                // Validate [row,col] format
                if (!input.startsWith("[") || !input.endsWith("]") || !input.contains(",")) {
                    System.out.println("Invalid format! Use [row,col] format");
                    continue;
                }
    
                String[] parts = input.replace("[", "").replace("]", "").split(",");
                if (parts.length != 2) {
                    System.out.println("Invalid format! Use [row,col] format");
                    continue;
                }
    
                try {
                    row = Integer.parseInt(parts[0].trim()) - 1;
                    col = Integer.parseInt(parts[1].trim()) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid numbers! Use numbers for row and col");
                    continue;
                }
    
                // Validate range
                if (row < 0 || row >= table.size()) {
                    System.out.println("Invalid row! Row must be between 1 and " + table.size());
                    continue;
                }
    
                if (col < 0 || col >= table.get(row).size()) {
                    System.out.println("Invalid column! Column must be between 1 and " + table.get(row).size());
                    continue;
                }
    
                // Exit first loop if row & col are valid
                break;
            }
    
            // Target cell
            String[] keyValue = table.get(row).get(col);
            String oldKey = keyValue[0];
            String oldValue = keyValue[1];
    
            // === SECOND PROMPT: key/value/both ===
            String editChoice;
            while (true) {
                System.out.print("key, value or both? : ");
                editChoice = scanner.nextLine().toLowerCase().trim();
                if (editChoice.equals("key") || editChoice.equals("value") || editChoice.equals("both")) {
                    break; // valid choice
                }
                System.out.println("Invalid choice! Use 'key', 'value', or 'both'");
            }
    
            // === THIRD PROMPT: New Value ===
            while (true) {
                System.out.print("Input new value: ");
                String newInput = scanner.nextLine();
    
                if (editChoice.equals("key")) {
                    keyValue[0] = newInput;
                    System.out.println("Old key: " + oldKey + " -> New key: " + newInput);
                    break;
    
                } else if (editChoice.equals("value")) {
                    keyValue[1] = newInput;
                    System.out.println("Old value: " + oldValue + " -> New value: " + newInput);
                    break;
    
                } else { // both
                    String[] bothParts = newInput.split(",", 2);
                    if (bothParts.length == 2) {
                        keyValue[0] = bothParts[0];
                        keyValue[1] = bothParts[1];
                        System.out.println("Old: " + oldKey + "," + oldValue +
                                           " -> New: " + bothParts[0] + "," + bothParts[1]);
                        break; // done
                    } else {
                        System.out.println("For both, use format: key,value");
                    }
                }
            }
            // Save and print table
            saveFile();
            printTable();
    
        } catch (Exception e) {
            System.out.println("Error editing cell: " + e.getMessage());
        }
    }
    
    
    // Add new row with random data
    private static void addRow() {
        try {
            int numCells;
    
            // === LOOP UNTIL VALID INPUT ===
            while (true) {
                System.out.print("No. of Cells: ");
                String input = scanner.nextLine();
    
                // Check if input is a number
                try {
                    numCells = Integer.parseInt(input.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number! Please enter a valid number");
                    continue; // ask again
                }
    
                // Validate number of cells
                if (numCells <= 0) {
                    System.out.println("Number of cells must be greater than 0");
                    continue; // ask again
                }
    
                if (numCells > 20) {
                    System.out.println("Too many cells! Maximum is 20");
                    continue; // ask again
                }
                break;
            }
    
            // Create new row
            List<String[]> newRow = new ArrayList<>();
    
            // Generate random cell content using ASCII generation
            for (int i = 0; i < numCells; i++) {
                String randomKey = generateRandomASCII();
                String randomValue = generateRandomASCII();
                newRow.add(new String[]{randomKey, randomValue});
            }  
            table.add(newRow);   
            saveFile();
            printTable();   
        } catch (Exception e) {
            System.out.println("Error adding row: " + e.getMessage());
        }
    }
    
    
    // Sort a specific row
    private static void sortTable() {
        try {
            int rowIndex;
    
            // === FIRST PROMPT: Row to sort ===
            while (true) {
                System.out.print("Row to sort: ");
                String rowInput = scanner.nextLine();
    
                // Validate row number input
                try {
                    rowIndex = Integer.parseInt(rowInput.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number! Please enter a valid row number");
                    continue; // ask again
                }
    
                // Validate row range
                if (rowIndex < 0 || rowIndex >= table.size()) {
                    System.out.println("Invalid row! Row must be between 0 and " + (table.size() - 1));
                    continue; // ask again
                }
    
                if (table.get(rowIndex).isEmpty()) {
                    System.out.println("Row is empty! Cannot sort");
                    return; // No point in sorting an empty row
                }   
                break;
            }
    
            // === SECOND PROMPT: Order ===
            String order;
            while (true) {
                System.out.print("Order (asc/desc): ");
                order = scanner.nextLine().toLowerCase().trim();
    
                if (order.equals("asc") || order.equals("desc")) {
                    break; // valid
                }
                System.out.println("Invalid order! Use 'asc' or 'desc'");
            }
    
            // Sort the row based on key values
            List<String[]> row = table.get(rowIndex);
            if (order.equals("desc")) {
                Collections.sort(row, (a, b) -> b[0].compareTo(a[0]));
            } else {
                Collections.sort(row, (a, b) -> a[0].compareTo(b[0]));
            }
            printTable();
    
        } catch (Exception e) {
            System.out.println("Error sorting: " + e.getMessage());
        }
    }
    
    
    // Reset table with new dimensions
    private static void resetTable() {
        try {
            int rows;
            int cols;
    
            // === FIRST PROMPT: Number of rows ===
            while (true) {
                System.out.print("Number of rows: ");
                String rowInput = scanner.nextLine();
    
                // Validate integer input
                try {
                    rows = Integer.parseInt(rowInput.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number! Please enter a valid number for rows");
                    continue; // ask again
                }
    
                // Validate range
                if (rows <= 0) {
                    System.out.println("Number of rows must be greater than 0");
                    continue;
                }
    
                if (rows > 50) {
                    System.out.println("Too many rows! Maximum is 50");
                    continue;
                }   
                break;
            }
    
            // === SECOND PROMPT: Number of columns ===
            while (true) {
                System.out.print("Number of columns: ");
                String colInput = scanner.nextLine();
    
                // Validate integer input
                try {
                    cols = Integer.parseInt(colInput.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number! Please enter a valid number for columns");
                    continue; // ask again
                }
    
                // Validate range
                if (cols <= 0) {
                    System.out.println("Number of columns must be greater than 0");
                    continue;
                }
    
                if (cols > 20) {
                    System.out.println("Too many columns! Maximum is 20");
                    continue;
                }
                break;
            }
    
            // Clear existing table
            table.clear();
    
            // Fill table with random ASCII key/value pairs
            for (int i = 0; i < rows; i++) {
                List<String[]> row = new ArrayList<>();
                for (int j = 0; j < cols; j++) {
                    String randomKey = generateRandomASCII();
                    String randomValue = generateRandomASCII();
                    row.add(new String[]{randomKey, randomValue});
                }
                table.add(row);
            }
    
            // Save changes and print table
            saveFile();
            printTable();
    
        } catch (Exception e) {
            System.out.println("Error resetting table: " + e.getMessage());
        }
    }    
}