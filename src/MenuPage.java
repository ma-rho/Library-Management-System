package LibrarySystemGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuPage extends JFrame {

    // UI Components
    private JLabel titleLabel;
    private JButton manageBooksButton;
    private JButton managePatronsButton;
    private JButton returnBookButton;
    private JButton logOutButton;

    // Constructor
    public MenuPage() {
        initComponents();
    }

    /**
     * Initializes the UI components and layout.
     */
    private void initComponents() {
        // Initialize components
        titleLabel = new JLabel("Menu");
        manageBooksButton = new JButton("Manage Books");
        managePatronsButton = new JButton("Manage Patrons");
        returnBookButton = new JButton("Return Books");
        logOutButton = new JButton("Log Out");

        // JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Library Management System - Main Menu");
        setSize(400, 400);
        setLayout(null);

        // Set bounds and add components
        titleLabel.setBounds(170, 20, 100, 30);
        add(titleLabel);

        manageBooksButton.setBounds(120, 70, 150, 30);
        add(manageBooksButton);

        managePatronsButton.setBounds(120, 120, 150, 30);
        add(managePatronsButton);

        returnBookButton.setBounds(120, 170, 150, 30);
        add(returnBookButton);

        logOutButton.setBounds(120, 220, 150, 30);
        add(logOutButton);

        // Add action listeners
        manageBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                manageBooksActionPerformed(evt);
            }
        });

        managePatronsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                issueBookActionPerformed(evt);
            }
        });

        returnBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                returnBookActionPerformed(evt);
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                logOutActionPerformed(evt);
            }
        });
    }

    // Action listeners for buttons
    private void manageBooksActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Navigating to Manage Books page...");
        new ManageBookPage().setVisible(true);
        dispose(); // Close LibrarySystemGUI.LogInPage
    }

    private void issueBookActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Navigating to Issue Book page...");
    }

    private void returnBookActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Navigating to Return Books page...");
    }

    private void logOutActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Logging out...");
        new WelcomePage().setVisible(true);
        dispose(); // Close LibrarySystemGUI.LogInPage
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPage().setVisible(true));
    }
}
