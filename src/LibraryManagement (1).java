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
            }
            csvReader.close();
            csvWriter.close();

            // Main menu for user actions
            while (true) {
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add a Book\n2. Delete a Book\n3. Update a Book\n4. Exit");
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
                        updateBook(scanner);
                        break;
                    case "4":
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
        String ISBN = getValidISBN(scanner, existingISBNs);
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        String year = getValidYear(scanner);

        try (FileWriter csvWriter = new FileWriter("Library Management System.csv", true)) {
            csvWriter.append(ISBN).append(", ")
                    .append(author).append(", ")
                    .append(title).append(", ")
                    .append(year).append("\n");

            System.out.println("Book added successfully!");
        }
    }

    // Method to get a valid ISBN (5 digits)
    private static String getValidISBN(Scanner scanner, Set<String> existingISBNs) {
        String ISBN;
        while (true) {
            System.out.print("Enter ISBN (5 digits): ");
            ISBN = scanner.nextLine();
            if (ISBN.matches("\\d{5}") && !existingISBNs.contains(ISBN)) {
                break; // Valid ISBN and not a duplicate
            }
            if (existingISBNs.contains(ISBN)) {
                System.out.println("Error: This ISBN already exists in the catalog!");
            } else {
                System.out.println("Invalid ISBN. It must be a 5-digit number.");
            }
        }
        return ISBN;
    }

    // Method to get a valid Publication Year (4 digits)
    private static String getValidYear(Scanner scanner) {
        String year;
        while (true) {
            System.out.print("Enter Publication Year (4 digits): ");
            year = scanner.nextLine();
            if (year.matches("\\d{4}")) {
                break; // Valid 4-digit year
            }
            System.out.println("Invalid year. It must be a 4-digit number.");
        }
        return year;
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

    // Method to update a book
    private static void updateBook(Scanner scanner) throws IOException {
        System.out.print("\nEnter the ISBN of the book you want to update: ");
        String targetISBN = scanner.nextLine();

        File inputFile = new File("Library Management System.csv");
        File tempFile = new File("temp.csv");
        boolean isUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(tempFile)) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] fields = currentLine.split(", ");
                if (fields.length > 0 && fields[0].trim().equals(targetISBN)) {
                    System.out.println("\nBook found: ");
                    System.out.println("ISBN: " + fields[0]);
                    System.out.println("Author: " + fields[1]);
                    System.out.println("Title: " + fields[2]);
                    System.out.println("Publication Year: " + fields[3]);

                    System.out.println("\nWhat would you like to update?\n1. ISBN\n2. Author\n3. Title\n4. Publication Year");
                    System.out.print("Enter your choice: ");
                    String updateChoice = scanner.nextLine();

                    switch (updateChoice) {
                        case "1":
                            fields[0] = getValidISBN(scanner, new HashSet<>()); // Validate new ISBN
                            break;
                        case "2":
                            System.out.print("Enter new Author: ");
                            fields[1] = scanner.nextLine();
                            break;
                        case "3":
                            System.out.print("Enter new Title: ");
                            fields[2] = scanner.nextLine();
                            break;
                        case "4":
                            fields[3] = getValidYear(scanner); // Validate new year
                            break;
                        default:
                            System.out.println("Invalid choice. No updates made.");
                            writer.write(currentLine + "\n");
                            continue;
                    }
                    isUpdated = true;
                    System.out.println("Book updated successfully!");
                    System.out.println("Updated details: " + String.join(", ", fields));
                    writer.write(String.join(", ", fields) + "\n");
                } else {
                    writer.write(currentLine + "\n");
                }
            }
            if (!isUpdated) {
                System.out.println("Book with ISBN " + targetISBN + " not found.");
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