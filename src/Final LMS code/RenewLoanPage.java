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
}
