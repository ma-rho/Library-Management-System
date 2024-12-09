package library.management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PatronDashboard {
    private JFrame frame;
    private LoginPage loginPage;
    private String username;

    public PatronDashboard(LoginPage loginPage, String username) {
        this.loginPage = loginPage;
        this.username = username;

        frame = new JFrame("Patron Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome, " + username, JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewBooksButton = new JButton("View Available Books");
        JButton borrowBooksButton = new JButton("Borrow Books");
        JButton returnBooksButton = new JButton("Return Books");
        JButton loanHistoryButton = new JButton("Loan History");
        JButton backButton = new JButton("Back");

        viewBooksButton.setBackground(new Color(0, 102, 204));
        viewBooksButton.setForeground(Color.WHITE);
        borrowBooksButton.setBackground(new Color(0, 102, 204));
        borrowBooksButton.setForeground(Color.WHITE);
        returnBooksButton.setBackground(new Color(0, 102, 204));
        returnBooksButton.setForeground(Color.WHITE);
        loanHistoryButton.setBackground(new Color(0, 102, 204));
        loanHistoryButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        viewBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new ViewBooksPage(PatronDashboard.this);
                new ViewBooksPage();
                frame.dispose();
            }
        });

        borrowBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BorrowBooksPage(PatronDashboard.this, username);
                frame.dispose();
            }
        });

        returnBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReturnBooksPage(PatronDashboard.this, username);
                frame.dispose();
            }
        });

        loanHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoanHistoryPage(PatronDashboard.this, username);
                frame.dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                loginPage.show();
            }
        });

        buttonPanel.add(viewBooksButton);
        buttonPanel.add(borrowBooksButton);
        buttonPanel.add(returnBooksButton);
        buttonPanel.add(loanHistoryButton);
        buttonPanel.add(backButton);

        frame.add(title, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }
}
