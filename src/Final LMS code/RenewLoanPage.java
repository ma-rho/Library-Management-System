package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RenewLoanPage {
    private JFrame frame;
    private PatronDashboard patronDashboard;
    private JTable loansTable;
    private String username;

    public RenewLoanPage(PatronDashboard patronDashboard, String username) {
        this.patronDashboard = patronDashboard;
        this.username = username;

        frame = new JFrame("Renew Loan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Renew Borrowed Books", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JButton renewButton = new JButton("Renew Selected Loan");
        JButton backButton = new JButton("Back");

        renewButton.setBackground(new Color(0, 102, 204));
        renewButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        loansTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(loansTable);
        loadBorrowedBooks();

        renewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renewLoan();
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
        buttonPanel.add(renewButton);
        buttonPanel.add(backButton);

        frame.add(title, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
    
    private void loadBorrowedBooks() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = """
                SELECT l.loan_id, b.title, l.due_date 
                FROM loans l 
                JOIN books b ON l.book_id = b.book_id 
                JOIN users u ON l.user_id = u.user_id 
                WHERE u.username = ? AND l.return_date IS NULL
            """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            loansTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Loan ID", "Book Title", "Due Date"}
            ));

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) loansTable.getModel();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("loan_id"),
                        rs.getString("title"),
                        rs.getDate("due_date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading borrowed books.");
        }
    }
    private void renewLoan() {
        int selectedRow = loansTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a loan to renew.");
            return;
        }

        int loanId = (int) loansTable.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Extend due date by 7 days
            String query = "UPDATE loans SET due_date = DATE_ADD(due_date, INTERVAL 7 DAY) WHERE loan_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loanId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Loan renewed successfully! Due date extended by 7 days.");
            loadBorrowedBooks(); // Refresh table
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error renewing loan.");
        }
    }
}
