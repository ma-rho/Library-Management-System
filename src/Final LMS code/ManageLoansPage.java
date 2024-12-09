package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageLoansPage {
    private JFrame frame;
    private AdminDashboard adminDashboard;
    private JTable loansTable;

    public ManageLoansPage(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;

        frame = new JFrame("Manage Loans");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Loans", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JButton calculateFineButton = new JButton("Calculate Fine");
        JButton backButton = new JButton("Back");

        calculateFineButton.setBackground(new Color(0, 102, 204));
        calculateFineButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.add(calculateFineButton);
        topPanel.add(backButton);

        loansTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(loansTable);
        loadLoansData();

        frame.add(title, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.SOUTH);

        calculateFineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateFine();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                adminDashboard.show();
            }
        });

        frame.setVisible(true);
    }

    private void loadLoansData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT l.loan_id, b.title AS book_title, u.username AS patron, l.loan_date, l.due_date, l.return_date, l.fine " +
                    "FROM loans l " +
                    "JOIN books b ON l.book_id = b.book_id " +
                    "JOIN users u ON l.user_id = u.user_id";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            loansTable.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Loan ID", "Book Title", "Patron", "Loan Date", "Due Date", "Return Date", "Fine"}
            ));

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) loansTable.getModel();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("loan_id"),
                        rs.getString("book_title"),
                        rs.getString("patron"),
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

    private void calculateFine() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE loans " +
                    "SET fine = DATEDIFF(CURDATE(), due_date) * 0.50 " +
                    "WHERE return_date IS NULL AND CURDATE() > due_date";
            PreparedStatement stmt = conn.prepareStatement(query);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Fines calculated for overdue loans.");
                loadLoansData(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(frame, "No overdue loans found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
