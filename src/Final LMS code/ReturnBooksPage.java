package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReturnBooksPage {
    private JFrame frame;
    private PatronDashboard patronDashboard;
    private JTable loansTable;
    private String username;

    public ReturnBooksPage(PatronDashboard patronDashboard, String username) {
        this.patronDashboard = patronDashboard;
        this.username = username;

        frame = new JFrame("Return Books");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Return Books", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JButton returnButton = new JButton("Return Selected Book");
        JButton backButton = new JButton("Back");

        returnButton.setBackground(new Color(0, 102, 204));
        returnButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        loansTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(loansTable);
        loadLoanedBooks();

        returnButton.addActionListener(e -> returnBook());
        backButton.addActionListener(e -> {
            frame.dispose();
            patronDashboard.show();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(returnButton);
        buttonPanel.add(backButton);

        frame.add(title, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Trigger notification for due books
        sendNotification();
    }

    private void loadLoanedBooks() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT l.loan_id, b.title, l.due_date, l.return_date, l.fine " +
                    "FROM loans l " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "WHERE u.username = ? AND l.return_date IS NULL";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            loansTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Loan ID", "Book Title", "Due Date", "Return Date", "Fine"}
            ));

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) loansTable.getModel();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("loan_id"),
                        rs.getString("title"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date"),
                        rs.getDouble("fine")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnBook() {
        int selectedRow = loansTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a book to return.");
            return;
        }

        int loanId = (int) loansTable.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Update the loan record with return_date
            String query = "UPDATE loans SET return_date = CURDATE() WHERE loan_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loanId);
            stmt.executeUpdate();

            // Prompt for rating
            String ratingInput = JOptionPane.showInputDialog(frame, 
                "Please rate the book \"" + "\" (1-5):", "Rate Book", JOptionPane.PLAIN_MESSAGE);
            int rating = Integer.parseInt(ratingInput);

            if (rating >= 1 && rating <= 5) {
                String ratingQuery = "INSERT INTO ratings (book_id, rating) " +
                                    "VALUES ((SELECT book_id FROM loans WHERE loan_id = ?), ?)";
                PreparedStatement ratingStmt = conn.prepareStatement(ratingQuery);
                ratingStmt.setInt(1, loanId);
                ratingStmt.setInt(2, rating);
                ratingStmt.executeUpdate();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid rating. Please provide a number between 1 and 5.");
            }

            // Make the book available again
            String bookQuery = "UPDATE books SET is_available = TRUE WHERE book_id = (SELECT book_id FROM loans WHERE loan_id = ?)";
            PreparedStatement bookStmt = conn.prepareStatement(bookQuery);
            bookStmt.setInt(1, loanId);
            bookStmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Book returned successfully!");
            loadLoanedBooks(); // Refresh the table

            // Trigger notification to check for upcoming due dates
            sendNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// this method checks if the book needs to be returned within 14 days
    private void sendNotification() {
        SwingUtilities.invokeLater(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT l.loan_id, b.title, l.due_date " +
                        "FROM loans l " +
                        "JOIN books b ON l.book_id = b.book_id " +
                        "JOIN users u ON l.user_id = u.user_id " +
                        "WHERE u.username = ? AND l.return_date IS NULL " +
                        "AND DATEDIFF(l.due_date, CURDATE()) <= 14";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                StringBuilder message = new StringBuilder("The following books are due soon:\n");
                boolean hasDueBooks = false;

                while (rs.next()) {
                    hasDueBooks = true;
                    int loanId = rs.getInt("loan_id");
                    String bookTitle = rs.getString("title");
                    Date dueDate = rs.getDate("due_date");

                    message.append("- [Loan ID: ").append(loanId).append("] ")
                            .append(bookTitle).append(" (Due: ").append(dueDate).append(")\n");
                }

                if (hasDueBooks) {
                    JOptionPane.showMessageDialog(frame, message.toString(),
                            "Upcoming Due Dates", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while checking for due books.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
