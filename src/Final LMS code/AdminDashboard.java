package library.management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard {
    private JFrame frame;
    private LoginPage loginPage;

    public AdminDashboard(LoginPage loginPage) {
        this.loginPage = loginPage;

        frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Admin Dashboard", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton manageBooksButton = new JButton("Manage Books");
        JButton manageUsersButton = new JButton("Manage Users");
        JButton manageLoansButton = new JButton("Manage Loans");
        JButton backButton = new JButton("Back");

        manageBooksButton.setBackground(new Color(0, 102, 204));
        manageBooksButton.setForeground(Color.WHITE);
        manageUsersButton.setBackground(new Color(0, 102, 204));
        manageUsersButton.setForeground(Color.WHITE);
        manageLoansButton.setBackground(new Color(0, 102, 204));
        manageLoansButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        manageBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageBooksPage(AdminDashboard.this);
                frame.dispose();
            }
        });

        manageUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageUsersPage();
                frame.dispose();
            }
        });

        manageLoansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageLoansPage(AdminDashboard.this);
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

        buttonPanel.add(manageBooksButton);
        buttonPanel.add(manageUsersButton);
        buttonPanel.add(manageLoansButton);
        buttonPanel.add(backButton);

        frame.add(title, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }
}
