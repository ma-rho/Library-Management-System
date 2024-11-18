package LibrarySystemGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame {

    // Constructor
    public WelcomePage() {
        // Initialize components
        initComponents();
    }

    private void initComponents() {
        // Components
        JLabel jLabel1 = new JLabel();
        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();

        // JFrame properties
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Welcome Page");
        setSize(400, 300);
        setLayout(null);

        // Welcome label
        jLabel1.setText("Welcome");
        jLabel1.setBounds(160, 30, 100, 30);
        add(jLabel1);

        // Log In button
        jButton1.setText("Log In");
        jButton1.setBounds(150, 80, 100, 30);
        add(jButton1);

        // Log In button action
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Open LoginPage
                new LogInPage().setVisible(true);
                dispose(); // Close LibrarySystemGUI.WelcomePage
            }
        });

        // Sign Up button
        jButton2.setText("Sign Up");
        jButton2.setBounds(150, 130, 100, 30);
        add(jButton2);

        // Sign Up button action
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Open LibrarySystemGUI.SignUpPage
                new SignUpPage().setVisible(true);
                dispose(); // Close LibrarySystemGUI.WelcomePage
            }
        });
    }

    // LibrarySystemGUI.Main method to launch the LibrarySystemGUI.WelcomePage
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage().setVisible(true));
    }
}
