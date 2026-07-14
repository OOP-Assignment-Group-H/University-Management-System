package view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SignUpForm extends JFrame {

    // Components
    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private JButton signUpButton;
    private JRadioButton studentRadio;
    private JRadioButton lecturerRadio;
    private JRadioButton adminRadio;
    private ButtonGroup roleGroup;

    public SignUpForm() {

        setTitle("University Management System - Sign Up");
        setSize(1000, 700);     // Increased height
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        //==================== LEFT PANEL ====================

// 1. Load the background image asset (file-based, not classpath-based)
        java.io.File fctFile = new java.io.File("recources/images/FCT.jpg");
        if (!fctFile.exists()) {
            System.out.println("FCT.jpg NOT FOUND at: " + fctFile.getAbsolutePath());
        }
        Image backgroundImage = new ImageIcon(fctFile.getPath()).getImage();

// 2. Create the panel with the dynamic background image
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

// Use BoxLayout aligned along the Y_AXIS (Top to Bottom)
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20)); // Padding: Top, Left, Bottom, Right

// ==================== UNIVERSITY LOGO ====================

        java.io.File logoFile = new java.io.File("recources/images/logo.png");
        if (logoFile.exists()) {
            Image logoImg = new ImageIcon(logoFile.getPath()).getImage();
            Image scaledLogo = logoImg.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            ImageIcon resizedLogo = new ImageIcon(scaledLogo);

            JLabel logoLabel = new JLabel(resizedLogo);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
            leftPanel.add(logoLabel);
        } else {
            System.out.println("logo.png NOT FOUND at: " + logoFile.getAbsolutePath());
        }

// Add a gap between the logo and the titles
        leftPanel.add(Box.createVerticalStrut(30));

// ==================== TITLES OVERLAY ====================

// Main Title: Faculty of Computing and Technology
        JLabel mainTitle = new JLabel("Faculty of Computing and Technology");
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 25));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        leftPanel.add(mainTitle);

// Add a tiny gap between the main title and the subtitle
        leftPanel.add(Box.createVerticalStrut(8));

// Subtitle: University of Kelaniya
        JLabel subTitle = new JLabel("University of Kelaniya");
        subTitle.setForeground(Color.BLACK); // Slightly off-white for contrast
        subTitle.setFont(new Font("Arial", Font.BOLD, 22));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        leftPanel.add(subTitle);
        //==================== RIGHT PANEL ====================

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel heading = new JLabel("Create Account");
        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setBounds(135, 30, 280, 40);
        rightPanel.add(heading);

        //---------------- Full Name ----------------

        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setBounds(60, 90, 150, 20);
        rightPanel.add(nameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(60, 115, 350, 40);
        fullNameField.setBackground(Color.WHITE);
        fullNameField.setBorder(new Rounderborder(20, new Color(210, 210, 210)));
        rightPanel.add(fullNameField);

        //---------------- Username ----------------

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setBounds(60, 170, 150, 20);
        rightPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(60, 195, 350, 40);
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(new Rounderborder(20, new Color(210, 210, 210)));
        rightPanel.add(usernameField);

        //---------------- Email ----------------

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setBounds(60, 250, 150, 20);
        rightPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(60, 275, 350, 40);
        emailField.setBackground(Color.WHITE);
        emailField.setBorder(new Rounderborder(20, new Color(210, 210, 210)));
        rightPanel.add(emailField);

        //---------------- Password ----------------

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passLabel.setBounds(60, 330, 150, 20);
        rightPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 355, 350, 40);
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(new Rounderborder(20, new Color(210, 210, 210)));
        rightPanel.add(passwordField);

        //---------------- Confirm Password ----------------

        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 16));
        confirmLabel.setBounds(60, 410, 180, 20);
        rightPanel.add(confirmLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(60, 435, 350, 40);
        confirmPasswordField.setBackground(Color.WHITE);
        confirmPasswordField.setBorder(new Rounderborder(20, new Color(210, 210, 210)));
        rightPanel.add(confirmPasswordField);

        //---------------- Role ----------------

        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleLabel.setBounds(60, 490, 150, 20);
        rightPanel.add(roleLabel);

        studentRadio = new JRadioButton("Student");
        studentRadio.setBounds(60, 520, 100, 25);
        studentRadio.setBackground(Color.WHITE);

        lecturerRadio = new JRadioButton("Lecturer");
        lecturerRadio.setBounds(170, 520, 100, 25);
        lecturerRadio.setBackground(Color.WHITE);

        adminRadio = new JRadioButton("Admin");
        adminRadio.setBounds(290, 520, 100, 25);
        adminRadio.setBackground(Color.WHITE);

        roleGroup = new ButtonGroup();
        roleGroup.add(studentRadio);
        roleGroup.add(lecturerRadio);
        roleGroup.add(adminRadio);

        rightPanel.add(studentRadio);
        rightPanel.add(lecturerRadio);
        rightPanel.add(adminRadio);

        //---------------- Sign Up Button ----------------

        signUpButton = new JButton("SIGN UP");
        signUpButton.setBounds(60, 575, 350, 45);
        signUpButton.setBackground(new Color(110, 20, 35));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createEmptyBorder());
        signUpButton.addActionListener(e -> registerUser());

        rightPanel.add(signUpButton);

        //---------------- Login Label ----------------

        JLabel loginLabel = new JLabel("Already have an account? Login");
        loginLabel.setBounds(120, 630, 240, 25);
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(loginLabel);

        // Click "Login" to go back to the Login window
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                SwingUtilities.invokeLater(() -> new MyFrame().setVisible(true));
                dispose();
            }
        });

        //---------------- Add Panels ----------------

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        setVisible(true);
    }


    // Register User


    private void registerUser() {

        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        String role = "";

        if (studentRadio.isSelected()) {
            role = "Student";
        } else if (lecturerRadio.isSelected()) {
            role = "Lecturer";
        } else if (adminRadio.isSelected()) {
            role = "Admin";
        }

        // Empty fields
        if (fullName.isEmpty()
                || username.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Role validation
        if (role.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select your role.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Email validation
        if (!email.contains("@") || !email.contains(".")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Password length
        if (password.length() < 6) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least 6 characters.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Password match
        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // ---------------- Save to database ----------------
        String sql = "INSERT INTO users (full_name, username, email, password, role) VALUES (?, ?, ?, ?, ?)";

        try (java.sql.Connection conn = db.DBConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role.toLowerCase());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Registration Successful! Please log in with your new account.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Go to Login screen
            SwingUtilities.invokeLater(() -> new MyFrame().setVisible(true));
            dispose();

        } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
            JOptionPane.showMessageDialog(
                    this,
                    "That username or email is already registered.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database error: " + ex.getMessage(),
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Rounded Border Class


    static class Rounderborder extends AbstractBorder {

        private final int radius;
        private final Color color;

        public Rounderborder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g,
                                int x, int y, int width, int height) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.draw(new RoundRectangle2D.Double(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    radius,
                    radius
            ));

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 15, 10, 15);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = 15;
            insets.right = 15;
            insets.top = 10;
            insets.bottom = 10;
            return insets;
        }
    }

    static class RoundedBorder extends AbstractBorder {

        // all RoundedBorder code

    }

}