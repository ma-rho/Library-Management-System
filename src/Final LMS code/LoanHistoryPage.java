package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoanHistoryPage {
    private JFrame frame;
    private PatronDashboard patronDashboard;
    private JTable historyTable;
    private String username;

    public LoanHistoryPage(PatronDashboard patronDashboard, String username) {
        this.patronDashboard = patronDashboard;
        this.username = username;

        frame = new JFrame("Loan History");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Loan History", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JButton payFineButton = new JButton("Pay Fine");
        JButton backButton = new JButton("Back");

        payFineButton.setBackground(new Color(0, 102, 204));
        payFineButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        historyTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(historyTable);
        loadLoanHistory();

        payFineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payFine();
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
        buttonPanel.add(payFineButton);
        buttonPanel.add(backButton);

        frame.add(title, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadLoanHistory() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT l.loan_id, b.title, l.loan_date, l.due_date, l.return_date, l.fine " +
                    "FROM loans l " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "JOIN users u ON l.user_id = u.user_id " +
                    "WHERE u.username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            historyTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Loan ID", "Book Title", "Loan Date", "Due Date", "Return Date", "Fine"}
            ));

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) historyTable.getModel();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("loan_id"),
                        rs.getString("title"),
                        rs.getDate("loan_date"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date"),
                        rs.getDouble("fine")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payFine() {
        int selectedRow = historyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a loan with a fine to pay.");
            return;
        }

        double fine = (double) historyTable.getValueAt(selectedRow, 5);
        if (fine <= 0) {
            JOptionPane.showMessageDialog(frame, "No fine to pay for the selected loan.");
            return;
        }

        int loanId = (int) historyTable.getValueAt(selectedRow, 0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE loans SET fine = 0 WHERE loan_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loanId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Fine paid successfully!");
            loadLoanHistory(); // Refresh table
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error paying fine.");
        }
    }
}
