package LibrarySystemGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageBookPage extends JFrame {

    // UI Components
    private JComboBox<String> bookDropdown;
    private JTextField isbnTextField, titleTextField, authorTextField, publicationYearTextField;
    private JButton addButton, updateButton, removeButton, backButton;

    public ManageBookPage() {
        setTitle("Library Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500); // Set preferred window size
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(null); // Use absolute layout
        initComponents();
    }

    private void initComponents() {
        // Create labels
        JLabel isbnLabel = new JLabel("ISBN:");
        JLabel titleLabel = new JLabel("Title:");
        JLabel authorLabel = new JLabel("Author:");
        JLabel publicationYearLabel = new JLabel("Publication Year:");

        // Set bounds for labels
        isbnLabel.setBounds(50, 30, 100, 25);
        titleLabel.setBounds(50, 70, 100, 25);
        authorLabel.setBounds(50, 110, 100, 25);
        publicationYearLabel.setBounds(50, 150, 120, 25);

        // Add labels to frame
        add(isbnLabel);
        add(titleLabel);
        add(authorLabel);
        add(publicationYearLabel);

        // Create text fields
        isbnTextField = new JTextField();
        titleTextField = new JTextField();
        authorTextField = new JTextField();
        publicationYearTextField = new JTextField();

        // Set bounds for text fields
        isbnTextField.setBounds(200, 30, 200, 25);
        titleTextField.setBounds(200, 70, 200, 25);
        authorTextField.setBounds(200, 110, 200, 25);
        publicationYearTextField.setBounds(200, 150, 200, 25);

        // Add text fields to frame
        add(isbnTextField);
        add(titleTextField);
        add(authorTextField);
        add(publicationYearTextField);

        // Dropdown for book list
        bookDropdown = new JComboBox<>();
        bookDropdown.addItem("No books available");
        bookDropdown.setBounds(50, 190, 350, 25);

        // Add dropdown to frame
        add(bookDropdown);

        // Create buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        removeButton = new JButton("Remove");
        backButton = new JButton("Back");

        // Set bounds for buttons
        addButton.setBounds(50, 230, 100, 25);
        updateButton.setBounds(180, 230, 100, 25);
        removeButton.setBounds(310, 230, 100, 25);
        backButton.setBounds(180, 270, 100, 25);

        // Add buttons to frame
        add(addButton);
        add(updateButton);
        add(removeButton);
        add(backButton);

        // Set action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddBook();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateBook();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveBook();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBackAction();
            }
        });
    }

    private void handleAddBook() {
        String isbn = isbnTextField.getText();
        String title = titleTextField.getText();
        String author = authorTextField.getText();
        String publicationYear = publicationYearTextField.getText();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || publicationYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String bookDetails = String.format("ISBN: %s, Title: %s, Author: %s, Year: %s", isbn, title, author, publicationYear);
        if (bookDropdown.getItemCount() == 1 && bookDropdown.getItemAt(0).equals("No books available")) {
            bookDropdown.removeAllItems();
        }
        bookDropdown.addItem(bookDetails);
        clearInputFields();
    }

    private void handleUpdateBook() {
        int selectedIndex = bookDropdown.getSelectedIndex();
        if (selectedIndex == -1 || bookDropdown.getItemAt(0).equals("No books available")) {
            JOptionPane.showMessageDialog(this, "No book selected to update.", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String isbn = isbnTextField.getText();
        String title = titleTextField.getText();
        String author = authorTextField.getText();
        String publicationYear = publicationYearTextField.getText();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || publicationYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled to update.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updatedBookDetails = String.format("ISBN: %s, Title: %s, Author: %s, Year: %s", isbn, title, author, publicationYear);
        bookDropdown.insertItemAt(updatedBookDetails, selectedIndex);
        bookDropdown.removeItemAt(selectedIndex + 1);
        clearInputFields();
    }

    private void handleRemoveBook() {
        int selectedIndex = bookDropdown.getSelectedIndex();
        if (selectedIndex == -1 || bookDropdown.getItemAt(0).equals("No books available")) {
            JOptionPane.showMessageDialog(this, "No book selected to remove.", "Remove Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        bookDropdown.removeItemAt(selectedIndex);
        if (bookDropdown.getItemCount() == 0) {
            bookDropdown.addItem("No books available");
        }
    }

    private void handleBackAction() {
        JOptionPane.showMessageDialog(this, "Back To The Menu.");
        new MenuPage().setVisible(true);
        dispose();
    }

    private void clearInputFields() {
        isbnTextField.setText("");
        titleTextField.setText("");
        authorTextField.setText("");
        publicationYearTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManageBookPage().setVisible(true);
            }
        });
    }
}

