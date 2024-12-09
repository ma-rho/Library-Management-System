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
}
