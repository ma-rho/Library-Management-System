import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class LibraryManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Initialize the library file with predefined books if it's empty
            FileWriter csvWriter = new FileWriter("Library Management System.csv", true);
            BufferedReader csvReader = new BufferedReader(new FileReader("Library Management System.csv"));

            if (csvReader.readLine() == null) { // Check if file is empty
                csvWriter.append("ISBN, Author, Title, Publication Year\n");
                String[][] predefinedBooks = {
                        {"33333", "Richardson", "Java Programming", "2022"},
                        {"66445", "Leach", "Web Development", "1980"}
                };
                for (String[] book : predefinedBooks) {
                    csvWriter.append(String.join(", ", book)).append("\n");
                }
            }
            csvReader.close();
            csvWriter.close();

            // Main menu for user actions
            while (true) {
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add a Book");
                System.out.println("2. Delete a Book");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        addBook(scanner);
                        break;
                    case "2":
                        deleteBook(scanner);
                        break;
                    case "3":
                        System.out.println("Exiting the program. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Method to add a book
    private static void addBook(Scanner scanner) throws IOException {
        Set<String> existingISBNs = new HashSet<>();
        try (BufferedReader csvReader = new BufferedReader(new FileReader("Library Management System.csv"))) {
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] fields = line.split(", ");
                if (fields.length > 0) {
                    existingISBNs.add(fields[0].trim()); // Assuming ISBN is the first field
                }
            }
        }

        System.out.println("\nEnter details for a new book:");
        System.out.print("Enter ISBN: ");
        String ISBN = scanner.nextLine();

        if (existingISBNs.contains(ISBN)) {
            System.out.println("Error: This ISBN already exists in the catalog!");
            return;
        }

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Publication Year: ");
        String year = scanner.nextLine();

        try (FileWriter csvWriter = new FileWriter("Library Management System.csv", true)) {
            csvWriter.append(ISBN).append(", ")
                    .append(author).append(", ")
                    .append(title).append(", ")
                    .append(year).append("\n");

            System.out.println("Book added successfully!");
        }
    }

    // Method to delete a book
    private static void deleteBook(Scanner scanner) throws IOException {
        System.out.print("\nEnter the ISBN of the book you want to delete: ");
        String deleteISBN = scanner.nextLine();

        File inputFile = new File("Library Management System.csv");
        File tempFile = new File("temp.csv");
        boolean isDeleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(tempFile)) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(", ");
                if (fields.length > 0 && fields[0].trim().equals(deleteISBN)) {
                    isDeleted = true; // Skip writing this line to the temp file
                    continue;
                }
                writer.write(currentLine + "\n");
            }

            if (isDeleted) {
                System.out.println("Book with ISBN " + deleteISBN + " has been deleted.");
            } else {
                System.out.println("Book with ISBN " + deleteISBN + " not found.");
            }
        }

        // Replace the original file with the temp file
        if (!inputFile.delete()) {
            System.out.println("Could not delete the original file.");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename the temp file.");
        }
    }
}