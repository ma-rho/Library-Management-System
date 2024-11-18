package LibrarySystemGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage extends JFrame {

    // Constructor
    public SignUpPage() {
        // Set up the frame
        setTitle("Sign Up Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Labels
        JLabel labelTitle = new JLabel("Sign Up");
        labelTitle.setBounds(160, 10, 100, 30);
        add(labelTitle);

        JLabel labelFirstName = new JLabel("First Name:");
        labelFirstName.setBounds(50, 50, 100, 30);
        add(labelFirstName);

        JLabel labelLastName = new JLabel("Last Name:");
        labelLastName.setBounds(50, 90, 100, 30);
        add(labelLastName);

        JLabel labelUsername = new JLabel("Username:");
        labelUsername.setBounds(50, 130, 100, 30);
        add(labelUsername);

        JLabel labelPassword = new JLabel("Password:");
        labelPassword.setBounds(50, 170, 100, 30);
        add(labelPassword);

        // Text fields and password field
        JTextField fieldFirstName = new JTextField();
        fieldFirstName.setBounds(150, 50, 200, 30);
        add(fieldFirstName);

        JTextField fieldLastName = new JTextField();
        fieldLastName.setBounds(150, 90, 200, 30);
        add(fieldLastName);

        JTextField fieldUsername = new JTextField();
        fieldUsername.setBounds(150, 130, 200, 30);
        add(fieldUsername);

        JPasswordField fieldPassword = new JPasswordField();
        fieldPassword.setBounds(150, 170, 200, 30);
        add(fieldPassword);

        // Buttons
        JButton buttonSignUp = new JButton("Sign Up");
        buttonSignUp.setBounds(150, 220, 90, 30);
        add(buttonSignUp);

        JButton buttonBack = new JButton("Back");
        buttonBack.setBounds(260, 220, 90, 30);
        add(buttonBack);

        // Button actions
        buttonSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = fieldFirstName.getText();
                String lastName = fieldLastName.getText();
                String username = fieldUsername.getText();
                String password = new String(fieldPassword.getPassword());

                // For demonstration, display input in a dialog box
                if (!firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUpPage.this,
                            "Sign Up Successful!\n" +
                                    "Name: " + firstName + " " + lastName + "\n" +
                                    "Username: " + username,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SignUpPage.this,
                            "Please fill out all fields.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WelcomePage().setVisible(true);
                dispose(); // Close LibrarySystemGUI.LogInPage
                // You can navigate to a previous frame here if needed
            }
        });

        // Set frame visible
        setVisible(true);
    }

    // LibrarySystemGUI.Main method
    public static void main(String[] args) {
        // Create and display the LibrarySystemGUI.SignUpPage
        SwingUtilities.invokeLater(() -> new SignUpPage());
    }
}
