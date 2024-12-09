package library.management.gui;

import library.management.db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageUsersPage {
    private JFrame frame;
    private JTable usersTable;
    private JTextField searchField;

    public ManageUsersPage() {
        // Frame setup
        frame = new JFrame("Manage Users");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Manage Users", JLabel.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0, 102, 204));
        searchButton.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search by Username, Email, Phone, or Role:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Users Table
        usersTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(usersTable);

        // Back Button Panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the ManageUsersPage frame
            new AdminDashboard(new LoginPage() ); // Open AdminDashboard
        });
        backPanel.add(backButton);

        // Load all users on page load
        loadUsersData("");

        // Search Button Action
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText();
            loadUsersData(searchQuery);
        });

        // Adding Components to Frame
        frame.add(titleLabel, BorderLayout.NORTH);       // Title at the top
        frame.add(searchPanel, BorderLayout.NORTH);      // Search bar below the title
        frame.add(scrollPane, BorderLayout.CENTER);      // Table in the center
        frame.add(backPanel, BorderLayout.SOUTH);        // Back button at the bottom

        frame.setVisible(true);
    }

    private void loadUsersData(String searchQuery) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT user_id, username, password, role, email, phone " +
                    "FROM users " +
                    "WHERE username LIKE ? OR email LIKE ? OR phone LIKE ? OR role LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            String wildcardSearch = "%" + searchQuery + "%";
            stmt.setString(1, wildcardSearch);
            stmt.setString(2, wildcardSearch);
            stmt.setString(3, wildcardSearch);
            stmt.setString(4, wildcardSearch);

            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"User ID", "Username", "Password", "Role", "Email", "Phone"}, 0
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"), // Include password
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("phone")
                });
            }

            usersTable.setModel(model);

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No users found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading users data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ManageUsersPage(); // Test entry point
    }
}


