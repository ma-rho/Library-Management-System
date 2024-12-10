package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageBooksPage {
    private JFrame frame;
    private AdminDashboard adminDashboard;
    private JTable booksTable;
    private JTextField isbnField, titleField, authorField, yearField, availableField;

    //JFrame created for thw window where admin users can add, update or delete the books in the library
    public ManageBooksPage(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;

        frame = new JFrame("Manage Books");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Books", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        isbnField = new JTextField();
        titleField = new JTextField();
        authorField = new JTextField();
        yearField = new JTextField();
        availableField = new JTextField();

        formPanel.add(new JLabel("ISBN:"));
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);
        formPanel.add(new JLabel("Available (true/false):"));
        formPanel.add(availableField);

        JButton addBookButton = new JButton("Add Book");
        JButton updateBookButton = new JButton("Update Book");
        JButton deleteBookButton = new JButton("Delete Book");
        JButton backButton = new JButton("Back");

        addBookButton.setBackground(new Color(0, 0, 204));
        addBookButton.setForeground(Color.WHITE);
        updateBookButton.setBackground(new Color(0, 102, 204));
        updateBookButton.setForeground(Color.WHITE);
        deleteBookButton.setBackground(new Color(255, 0, 0));
        deleteBookButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBookButton);
        buttonPanel.add(updateBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(backButton);

        booksTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(booksTable);
        loadBooksData();

        //button functionality added so when buttons are clicked, the associated function is run
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        updateBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });

        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                adminDashboard.show();
            }
        });

        //layout of the window is defined
        frame.add(title, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(formPanel, BorderLayout.WEST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    //function to load all the books available in the library from the database
    private void loadBooksData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            booksTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Book ID", "ISBN", "Title", "Author", "Year", "Available"}
            ));

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) booksTable.getModel();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publication_year"),
                        rs.getBoolean("is_available")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //function to add a book to the library - admin user is required to add all elements of the book to successfully add it to the library, e.g. title, author, ISBN number, etc.
    private void addBook() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String year = yearField.getText();
        String available = availableField.getText();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || year.isEmpty() || available.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled out.");
            return;
        }

        //database is updated if connected successfully
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO books (isbn, title, author, publication_year, is_available) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, isbn);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setInt(4, Integer.parseInt(year));
            stmt.setBoolean(5, Boolean.parseBoolean(available));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Book added successfully!");
            loadBooksData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding book.");
        }
    }

    //function created for admin user to update books - all fields need to be filled in with the updated details which need to be changed, then click on book and press update button
    private void updateBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a book to update.");
            return;
        }

        int bookId = (int) booksTable.getValueAt(selectedRow, 0);
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String year = yearField.getText();
        String available = availableField.getText();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || year.isEmpty() || available.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled out.");
            return;
        }

        //database is updated if connected successfully
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE books SET isbn = ?, title = ?, author = ?, publication_year = ?, is_available = ? WHERE book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, isbn);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setInt(4, Integer.parseInt(year));
            stmt.setBoolean(5, Boolean.parseBoolean(available));
            stmt.setInt(6, bookId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Book updated successfully!");
            loadBooksData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating book.");
        }
    }

    //function for admin user to delete a book from the library - all fields need to be completed and the book needs to be selected to be successfully deleted
    private void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a book to delete.");
            return;
        }

        int bookId = (int) booksTable.getValueAt(selectedRow, 0);

        //database is updated if connected successfully
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM books WHERE book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Book deleted successfully!");
            loadBooksData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting book.");
        }
    }
}
