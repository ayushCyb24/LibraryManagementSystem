import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class LibraryManagementSystem {
    private static boolean isUserLoggedIn = false;
    private static String currentUser;
    private static HashMap<String, String> registeredUsers = new HashMap<>();
    private static ArrayList<String[]> bookList = new ArrayList<>();
    private static JTabbedPane tabbedPane; // Store tabbed pane reference

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryManagementSystem::showLibraryUI);
    }

    public static void showLibraryUI() {
        JFrame frame = new JFrame("📚 Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Library Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(34, 47, 62));

        tabbedPane = new JTabbedPane();

        // User Login & Registration Panel
        JPanel userPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        userPanel.setBackground(new Color(52, 73, 94));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register ✅");
        JButton loginButton = new JButton("Login 🔑");

        styleButton(registerButton);
        styleButton(loginButton);

        userPanel.add(new JLabel("Name:"));
        userPanel.add(nameField);
        userPanel.add(new JLabel("Email:"));
        userPanel.add(emailField);
        userPanel.add(new JLabel("Password:"));
        userPanel.add(passwordField);
        userPanel.add(registerButton);
        userPanel.add(loginButton);

        JLabel loginStatus = new JLabel("Not logged in", JLabel.CENTER);
        loginStatus.setForeground(Color.WHITE);
        userPanel.add(loginStatus);

        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showErrorDialog(frame, "All fields are required!");
            } else if (registeredUsers.containsKey(email) && !registeredUsers.get(email).equals(name)) {
                showErrorDialog(frame, "Email already exists with a different name!");
            } else {
                registeredUsers.put(email, name);
                JOptionPane.showMessageDialog(frame, "User Registered Successfully!");
            }
        });

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                showErrorDialog(frame, "Email and Password are required!");
            } else if (registeredUsers.containsKey(email)) {
                isUserLoggedIn = true;
                currentUser = email;
                loginStatus.setText("Logged in as: " + currentUser);
                JOptionPane.showMessageDialog(frame, "Login Successful!");

                // Unlock Books Tab!
                tabbedPane.setComponentAt(1, createBookManagementPanel(frame));
            } else {
                showErrorDialog(frame, "User not found! Please register.");
            }
        });

        // Book Management Panel (Locked Until User Logs In)
        JPanel bookPanel = createLockedBookPanel();

        // Add Panels to Tabs
        tabbedPane.addTab("User Management", userPanel);
        tabbedPane.addTab("Library Books", bookPanel);

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JPanel createLockedBookPanel() {
        JPanel lockedPanel = new JPanel();
        lockedPanel.setBackground(new Color(44, 62, 80));
        JLabel lockedMessage = new JLabel("🔒 Please log in to access the library!", JLabel.CENTER);
        lockedMessage.setForeground(Color.WHITE);
        lockedMessage.setFont(new Font("Arial", Font.BOLD, 16));
        lockedPanel.add(lockedMessage);
        return lockedPanel;
    }

    private static JPanel createBookManagementPanel(JFrame frame) {
        JPanel bookPanel = new JPanel(new BorderLayout());
        bookPanel.setBackground(new Color(44, 62, 80));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JButton addBookButton = new JButton("Add Book 📚");
        JButton showBooksButton = new JButton("Show Books 📖");

        styleButton(addBookButton);
        styleButton(showBooksButton);

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);
        formPanel.add(addBookButton);
        formPanel.add(showBooksButton);

        JTable bookTable = new JTable(new String[0][3], new String[]{"Title", "Author", "Year"});
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        bookPanel.add(formPanel, BorderLayout.NORTH);
        bookPanel.add(bookScrollPane, BorderLayout.CENTER);

        addBookButton.addActionListener(e -> {
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty() || yearField.getText().isEmpty()) {
                showErrorDialog(frame, "All fields are required!");
            } else {
                try {
                    int year = Integer.parseInt(yearField.getText());
                    bookList.add(new String[]{titleField.getText(), authorField.getText(), String.valueOf(year)});
                    JOptionPane.showMessageDialog(frame, "Book Added Successfully!");
                } catch (NumberFormatException ex) {
                    showErrorDialog(frame, "Year must be a valid number!");
                }
            }
        });

        showBooksButton.addActionListener(e -> {
            if (bookList.isEmpty()) {
                showErrorDialog(frame, "No books available!");
            } else {
                String[][] booksArray = new String[bookList.size()][3];
                for (int i = 0; i < bookList.size(); i++) {
                    booksArray[i] = bookList.get(i);
                }
                bookTable.setModel(new javax.swing.table.DefaultTableModel(booksArray, new String[]{"Title", "Author", "Year"}));
            }
        });

        return bookPanel;
    }

    private static void showErrorDialog(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, "❌ Error: " + message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void styleButton(JButton button) {
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
