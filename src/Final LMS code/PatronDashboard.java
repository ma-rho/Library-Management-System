package library.management.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatronDashboard {
    private JFrame frame;
    private LoginPage loginPage;
    private String username;

    //JFrame created for GUI window so users can easily navigate the main menu options available to them
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
        JButton renewLoanButton = new JButton("Renew Loan");
        JButton backButton = new JButton("Back");
        JButton recommendedBooksButton = new JButton("Recommended Books");


        recommendedBooksButton.setBackground(new Color(0, 102, 204));
        recommendedBooksButton.setForeground(Color.WHITE);
        viewBooksButton.setBackground(new Color(0, 102, 204));
        viewBooksButton.setForeground(Color.WHITE);
        borrowBooksButton.setBackground(new Color(0, 102, 204));
        borrowBooksButton.setForeground(Color.WHITE);
        returnBooksButton.setBackground(new Color(0, 102, 204));
        returnBooksButton.setForeground(Color.WHITE);
        loanHistoryButton.setBackground(new Color(0, 102, 204));
        loanHistoryButton.setForeground(Color.WHITE);
        renewLoanButton.setBackground(new Color(0, 102, 204));
        renewLoanButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        //Functionality for Recommended books button
        recommendedBooksButton.addActionListener(e -> displayRecommendedBooks());

        //functionality added to buttons so that when the button is clicked, the relevant page is loaded
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
                new BorrowBooksPage(library.management.gui.PatronDashboard.this, username);
                frame.dispose();
            }
        });

        returnBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ReturnBooksPage(library.management.gui.PatronDashboard.this, username);
                frame.dispose();
            }
        });

        loanHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoanHistoryPage(library.management.gui.PatronDashboard.this, username);
                frame.dispose();
            }
        });

        renewLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RenewLoanPage(library.management.gui.PatronDashboard.this, username);
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

        //buttons are added to the screen
        buttonPanel.add(viewBooksButton);
        buttonPanel.add(borrowBooksButton);
        buttonPanel.add(returnBooksButton);
        buttonPanel.add(loanHistoryButton);
        buttonPanel.add(renewLoanButton);
        buttonPanel.add(backButton);
        buttonPanel.add(recommendedBooksButton);

        frame.add(title, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void displayRecommendedBooks() {
        JFrame recommendationsFrame = new JFrame("Recommended Books");
        recommendationsFrame.setSize(600, 400);
        recommendationsFrame.setLayout(new BorderLayout());
    
        JLabel title = new JLabel("Recommended Books", JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));
    
        JTable recommendationsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(recommendationsTable);
    
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT b.title, b.author, AVG(r.rating) AS avg_rating " +
                           "FROM books b " +
                           "JOIN ratings r ON b.book_id = r.book_id " +
                           "GROUP BY b.book_id " +
                           "ORDER BY avg_rating DESC " +
                           "LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
    
            recommendationsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Title", "Author", "Average Rating"}
            ));
    
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) recommendationsTable.getModel();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getDouble("avg_rating")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        recommendationsFrame.add(title, BorderLayout.NORTH);
        recommendationsFrame.add(scrollPane, BorderLayout.CENTER);
        recommendationsFrame.setVisible(true);
    }
}