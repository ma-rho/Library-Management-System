import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class LibraryManagement {
    public static void main(String[] args) {
        Scanner addBook = new Scanner(System.in);
        try {
            FileWriter csvWriter = new FileWriter("Library Management System.csv", true);
            csvWriter.append("ISBN, Author, Title, Publication Year, Access Mode\n");

            //Writing data to file
            String[][] predefinedBooks = {
                    {"35667", "Jones", "Introduction to Programming", "2010", "1"},
                    {"33333", "Richardson", "Java Programming", "2022", "1"},
                    {"66445", "Leach", "Web Development", "1980", "1"}
            };

            //Write each list to the file
            for (String[] book : predefinedBooks) {
                csvWriter.append(String.join(", ", book)).append("\n");
            }

            // Check for duplicates by reading the current ISBNs in the file
            Set<String> existingISBNs = new HashSet<>();
            BufferedReader csvReader = new BufferedReader(new FileReader("Library Management System.csv"));
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] fields = line.split(", ");
                if (fields.length > 0) {
                    existingISBNs.add(fields[0].trim());  // Assuming ISBN is the first field
                }
            }
            csvReader.close();

            // Add books to library catalog
            System.out.println("Enter details for a new book to add: ");

            // Input the new book details
            System.out.println("Enter ISBN: ");
            String ISBN = addBook.nextLine();

            // Check if ISBN already exists
            if (existingISBNs.contains(ISBN)) {
                System.out.println("Error: This ISBN already exists in the catalog!");
            } else {
                System.out.println("Enter Author: ");
                String author = addBook.nextLine();
                System.out.println("Enter Title: ");
                String title = addBook.nextLine();
                System.out.println("Enter Publication Year: ");
                String year = addBook.nextLine();
                System.out.println("Enter Access Mode (1 for updatable, 0 for non-updatable): ");
                String accessMode = addBook.nextLine();

                csvWriter.append(ISBN).append(", ")
                        .append(author).append(", ")
                        .append(title).append(", ")
                        .append(year).append(", ")
                        .append(accessMode).append("\n");

                System.out.println("Book added successfully!");
                csvWriter.close();
            }

            csvWriter.close(); // Close the file
            addBook.close(); // Close the scanner
        } catch (IOException e) {
            System.out.println("An error occurred." + e.getMessage());
        }
    }
}
