package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewBooksPage {
    private JFrame frame;
    private JTable booksTable;
    private JTextField searchField;

    public ViewBooksPage() {
        // Frame setup
        frame = new JFrame("View Books");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("All Books", JLabel.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0, 102, 204));
        searchButton.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search by Title, ISBN, Author, or Year:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Books Table
        booksTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(booksTable);

        // Back Button Panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the ViewBooksPage frame
            new PatronDashboard(new LoginPage(), "Patron"); // Open PatronDashboard
        });
        backPanel.add(backButton);

        // Load all books on page load
        loadBooksData("");

        // Search Button Action
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText();
            loadBooksData(searchQuery);
        });

        // Adding Components to Frame
        frame.add(titleLabel, BorderLayout.NORTH);       // Title at the top
        frame.add(searchPanel, BorderLayout.NORTH);      // Search bar below the title
        frame.add(scrollPane, BorderLayout.CENTER);      // Table in the center
        frame.add(backPanel, BorderLayout.SOUTH);        // Back button at the bottom

        frame.setVisible(true);
    }

    private void loadBooksData(String searchQuery) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT book_id, isbn, title, author, publication_year, is_available " +
                    "FROM books " +
                    "WHERE title LIKE ? OR isbn LIKE ? OR author LIKE ? OR CAST(publication_year AS CHAR) LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            String wildcardSearch = "%" + searchQuery + "%";
            stmt.setString(1, wildcardSearch);
            stmt.setString(2, wildcardSearch);
            stmt.setString(3, wildcardSearch);
            stmt.setString(4, wildcardSearch);

            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Book ID", "ISBN", "Title", "Author", "Year", "Availability"}, 0
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publication_year"),
                        rs.getBoolean("is_available") ? "Available" : "Unavailable"
                });
            }

            booksTable.setModel(model);

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No books found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading books data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewBooksPage(); // Test entry point
    }
}

