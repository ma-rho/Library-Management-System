package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BorrowBooksPage {
    private JFrame frame;
    private PatronDashboard patronDashboard;
    private JTable booksTable;
    private String username;

    public BorrowBooksPage(PatronDashboard patronDashboard, String username) {
        this.patronDashboard = patronDashboard;
        this.username = username;

        frame = new JFrame("Borrow Books");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Borrow Books", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JButton borrowButton = new JButton("Borrow Selected Book");
        JButton backButton = new JButton("Back");

        borrowButton.setBackground(new Color(0, 102, 204));
        borrowButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        booksTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(booksTable);
        loadAvailableBooks();

        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrowBook();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                patronDashboard.show();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(borrowButton);
        buttonPanel.add(backButton);

        frame.add(title, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadAvailableBooks() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books WHERE is_available = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            booksTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Book ID", "ISBN", "Title", "Author", "Year"}
            ));

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) booksTable.getModel();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("book_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publication_year")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void borrowBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
            return;
        }

        int bookId = (int) booksTable.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE books SET is_available = FALSE WHERE book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookId);
            stmt.executeUpdate();

            String userQuery = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("user_id");
                String loanQuery = "INSERT INTO loans (book_id, user_id, due_date) VALUES (?, ?, DATE_ADD(CURDATE(), INTERVAL 14 DAY))";
                PreparedStatement loanStmt = conn.prepareStatement(loanQuery);
                loanStmt.setInt(1, bookId);
                loanStmt.setInt(2, userId);
                loanStmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Book borrowed successfully! Due in 14 days.");
                loadAvailableBooks(); // Refresh the table
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
