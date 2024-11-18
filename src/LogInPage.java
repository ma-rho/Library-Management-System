package LibrarySystemGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInPage extends JFrame {

    // Components
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JTextField jTextField1;
    private JPasswordField jPasswordField1;
    private JButton jButton1;
    private JButton jButton2;

    // Constructor
    public LogInPage() {
        initComponents();
    }

    private void initComponents() {
        // Initialize components
        jLabel1 = new JLabel("Log In");
        jLabel2 = new JLabel("Username or Email");
        jLabel3 = new JLabel("Password");
        jTextField1 = new JTextField();
        jPasswordField1 = new JPasswordField();
        jButton1 = new JButton("Log In");
        jButton2 = new JButton("Back");

        // JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Log In Page");
        setSize(400, 300);
        setLayout(null);

        // Set bounds and add components
        jLabel1.setBounds(160, 20, 80, 30);
        add(jLabel1);

        jLabel2.setBounds(40, 70, 150, 30);
        add(jLabel2);

        jTextField1.setBounds(180, 70, 150, 30);
        jTextField1.setText(""); // Placeholder
        add(jTextField1);

        jLabel3.setBounds(40, 120, 150, 30);
        add(jLabel3);

        jPasswordField1.setBounds(180, 120, 150, 30);
        jPasswordField1.setText(""); // Placeholder
        add(jPasswordField1);

        jButton1.setBounds(100, 180, 80, 30);
        add(jButton1);

        jButton2.setBounds(200, 180, 80, 30);
        add(jButton2);

        // Log In button action
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                handleLogIn();
            }
        });

        // Back button action
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                handleBack();
            }
        });
    }

    // Handle log-in logic
    private void handleLogIn() {
        String usernameOrEmail = jTextField1.getText();
        String password = new String(jPasswordField1.getPassword());

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username/email and password.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Dummy check for log-in (replace with actual validation logic)
            if (usernameOrEmail.equals("user") && password.equals("password")) {
                JOptionPane.showMessageDialog(this, "Log In Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Navigate to another page or dashboard here
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Handle back button logic
    private void handleBack() {
        new WelcomePage().setVisible(true);
        dispose(); // Close LibrarySystemGUI.LogInPage
    }

    // LibrarySystemGUI.Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LogInPage().setVisible(true));
    }
}
