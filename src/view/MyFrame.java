package view;

import admin.AdminDashboard;
import lecturer.LecturerPortal;
import student.DashboardApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Login {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyFrame().setVisible(true));
    }
}

// ---------- background image panel with maroon tint ----------
class ImagePanel extends JPanel {

    private final Image backgroundImage;

    public ImagePanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setLayout(null);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.setColor(new Color(60, 8, 10, 165));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

// ---------- plain panel with rounded corners (grey card background) ----------
class RoundedPanel extends JPanel {

    private final int radius;
    private final Color background;
    private final Color borderColor;

    public RoundedPanel(int radius, Color background, Color borderColor) {
        this.radius = radius;
        this.background = background;
        this.borderColor = borderColor;
        setLayout(null);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(background);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        super.paintComponent(g);
    }
}

// ---------- text field with rounded corners and placeholder ----------
class RoundedTextField extends JTextField {

    private final String placeholder;
    private final int radius = 12;

    public RoundedTextField(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setBorder(new EmptyBorder(0, 16, 0, 16));
        setFont(new Font("Arial", Font.PLAIN, 14));
        setForeground(new Color(40, 40, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(new Color(215, 215, 215));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        super.paintComponent(g);

        if (getText().isEmpty()) {
            g2.setColor(new Color(160, 160, 160));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, 16, textY);
        }
    }
}

// ---------- password field with rounded corners and placeholder ----------
class RoundedPasswordField extends JPasswordField {

    private final String placeholder;
    private final int radius = 12;

    public RoundedPasswordField(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setBorder(new EmptyBorder(0, 16, 0, 44));
        setFont(new Font("Arial", Font.PLAIN, 14));
        setForeground(new Color(40, 40, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(new Color(215, 215, 215));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        if (getPassword().length == 0) {
            g2.setColor(new Color(160, 160, 160));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, 16, textY);
        }

        super.paintComponent(g);
    }
}

// ---------- eye toggle button for showing/hiding password ----------
class EyeToggleButton extends JButton {

    private boolean showing = false;
    private final char realEchoChar;

    public EyeToggleButton(JPasswordField target) {
        this.realEchoChar = target.getEchoChar();

        setText("\u25CF");
        setFont(new Font("Arial", Font.PLAIN, 13));
        setForeground(new Color(140, 140, 140));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addActionListener(e -> {
            showing = !showing;
            target.setEchoChar(showing ? (char) 0 : realEchoChar);
            setText(showing ? "\u25CB" : "\u25CF");
        });
    }
}

// ---------- button with solid rounded background (maroon LOGIN button) ----------
class RoundedButton extends JButton {

    private final int radius = 12;
    private final Color baseColor;
    private final Color hoverColor;

    public RoundedButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = baseColor.darker();
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(baseColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g);
    }
}

// ---------- the login window itself ----------
public class MyFrame extends JFrame implements ActionListener {

    private RoundedTextField usernameField;
    private RoundedPasswordField passwordField;
    private RoundedButton loginButton;
    private JButton signUpButton;
    private JButton forgotPasswordButton;
    private EyeToggleButton eyeToggle;
    private JLabel heading;
    private JLabel roleLabel;
    private JLabel userLabel;
    private JLabel passLabel;
    private JCheckBox keepSignedIn;

    private JRadioButton studentRadio;
    private JRadioButton lecturerRadio;
    private JRadioButton adminRadio;
    private ButtonGroup roleGroup;

    private ImagePanel leftPanel;
    private RoundedPanel card;
    private JLabel capIcon;
    private JLabel systemTitle;
    private JLabel systemSubtitle;
    private JLabel campusName;

    private static final int MIN_WIDTH = 900;
    private static final int MIN_HEIGHT = 650;
    private static final int CARD_MAX_WIDTH = 470;
    private static final int SIDE_MARGIN = 40;
    private static final int CARD_PAD = 36;

    private static final Color MAROON = new Color(123, 17, 19);
    private static final Color GOLD = new Color(230, 180, 60);

    private static final Color PAGE_BG = new Color(255, 255, 255);
    private static final Color CARD_BG = new Color(240, 240, 240);
    private static final Color CARD_BORDER = new Color(225, 225, 225);
    private static final Color TEXT_DARK = new Color(35, 35, 40);
    private static final Color TEXT_GREY = new Color(110, 116, 122);

    public MyFrame() {
        setTitle("University of Kelaniya - Open Learning Portal");
        setSize(1000, 680);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(true);
        getContentPane().setBackground(PAGE_BG);

        java.io.File imageFile = new java.io.File("recources/images/campus.jpg");
        if (!imageFile.exists()) {
            System.out.println("campus.jpg NOT FOUND at: " + imageFile.getAbsolutePath());
        } else {
            System.out.println("campus.jpg found at: " + imageFile.getAbsolutePath());
        }
        Image campusImage = imageFile.exists() ? new ImageIcon(imageFile.getPath()).getImage() : null;
        leftPanel = new ImagePanel(campusImage);
        add(leftPanel);

        capIcon = new JLabel("\uD83C\uDF93");
        capIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        capIcon.setForeground(Color.WHITE);
        leftPanel.add(capIcon);

        systemTitle = new JLabel("UNIVERSITY MANAGEMENT SYSTEM");
        systemTitle.setFont(new Font("Arial", Font.BOLD, 19));
        systemTitle.setForeground(Color.WHITE);
        leftPanel.add(systemTitle);

        systemSubtitle = new JLabel("UNIVERSITY OF KELANIYA");
        systemSubtitle.setFont(new Font("Arial", Font.BOLD, 13));
        systemSubtitle.setForeground(GOLD);
        leftPanel.add(systemSubtitle);

        campusName = new JLabel("<html>University of<br>Kelaniya</html>");
        campusName.setFont(new Font("Arial", Font.BOLD, 32));
        campusName.setForeground(Color.WHITE);
        leftPanel.add(campusName);

        card = new RoundedPanel(30, CARD_BG, CARD_BORDER);
        add(card);

        heading = new JLabel("Account Log In");
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(TEXT_DARK);
        card.add(heading);

        roleLabel = new JLabel("LOGIN AS");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        roleLabel.setForeground(TEXT_GREY);
        card.add(roleLabel);

        studentRadio = new JRadioButton("Student");
        lecturerRadio = new JRadioButton("Lecturer");
        adminRadio = new JRadioButton("Admin");
        roleGroup = new ButtonGroup();
        for (JRadioButton radio : new JRadioButton[]{studentRadio, lecturerRadio, adminRadio}) {
            radio.setFont(new Font("Arial", Font.PLAIN, 15));
            radio.setBackground(CARD_BG);
            radio.setForeground(TEXT_DARK);
            radio.setFocusPainted(false);
            radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
            roleGroup.add(radio);
            card.add(radio);
        }
        studentRadio.setSelected(true);

        userLabel = new JLabel("USERNAME OR EMAIL");
        userLabel.setFont(new Font("Arial", Font.BOLD, 11));
        userLabel.setForeground(TEXT_GREY);
        card.add(userLabel);

        usernameField = new RoundedTextField("e.g., name@kln.ac.lk");
        card.add(usernameField);

        passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Arial", Font.BOLD, 11));
        passLabel.setForeground(TEXT_GREY);
        card.add(passLabel);

        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 11));
        forgotPasswordButton.setForeground(MAROON);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setHorizontalAlignment(SwingConstants.RIGHT);
        forgotPasswordButton.addActionListener(this);
        card.add(forgotPasswordButton);

        passwordField = new RoundedPasswordField("Enter your password");
        card.add(passwordField);

        eyeToggle = new EyeToggleButton(passwordField);
        card.add(eyeToggle);

        keepSignedIn = new JCheckBox("Keep me signed in");
        keepSignedIn.setFont(new Font("Arial", Font.PLAIN, 13));
        keepSignedIn.setBackground(CARD_BG);
        keepSignedIn.setForeground(TEXT_DARK);
        keepSignedIn.setFocusPainted(false);
        card.add(keepSignedIn);

        loginButton = new RoundedButton("LOGIN   \u2192", MAROON);
        loginButton.addActionListener(this);
        card.add(loginButton);

        signUpButton = new JButton("Don't have an account?  Sign Up Now");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 13));
        signUpButton.setForeground(MAROON);
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setHorizontalAlignment(SwingConstants.CENTER);
        signUpButton.addActionListener(this);
        add(signUpButton);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayout();
            }
        });

        relayout();
    }

    private void relayout() {
        Dimension size = getContentPane().getSize();
        int width = Math.max(size.width, MIN_WIDTH);
        int height = Math.max(size.height, MIN_HEIGHT);

        int leftWidth = Math.max(320, (int) (width * 0.42));
        leftPanel.setBounds(0, 0, leftWidth, height);

        capIcon.setBounds(40, 40, 50, 50);
        systemTitle.setBounds(95, 45, leftWidth - 120, 25);
        systemSubtitle.setBounds(95, 72, leftWidth - 120, 20);
        campusName.setBounds(40, height - 160, leftWidth - 60, 100);

        int availableRightWidth = width - leftWidth - (SIDE_MARGIN * 2);
        int cardWidth = Math.min(CARD_MAX_WIDTH, Math.max(500, availableRightWidth));
        int cardHeight = 452;

        int cardX = leftWidth + Math.max(SIDE_MARGIN, (availableRightWidth - cardWidth) / 2 + SIDE_MARGIN);
        int cardY = Math.max(20, (height - cardHeight - 60) / 2);
        card.setBounds(cardX, cardY, cardWidth, cardHeight);

        int fieldWidth = cardWidth - (CARD_PAD * 2);

        heading.setBounds(CARD_PAD, 28, fieldWidth, 34);

        roleLabel.setBounds(CARD_PAD, 76, fieldWidth, 16);
        int radioWidth = fieldWidth / 3;
        studentRadio.setBounds(CARD_PAD, 94, radioWidth, 24);
        lecturerRadio.setBounds(CARD_PAD + radioWidth, 94, radioWidth, 24);
        adminRadio.setBounds(CARD_PAD + radioWidth * 2, 94, radioWidth, 24);

        userLabel.setBounds(CARD_PAD, 132, fieldWidth, 16);
        usernameField.setBounds(CARD_PAD, 150, fieldWidth, 40);

        passLabel.setBounds(CARD_PAD, 210, 200, 16);
        forgotPasswordButton.setBounds(CARD_PAD + fieldWidth - 130, 208, 130, 18);
        passwordField.setBounds(CARD_PAD, 230, fieldWidth, 40);
        eyeToggle.setBounds(CARD_PAD + fieldWidth - 34, 230, 30, 40);

        keepSignedIn.setBounds(CARD_PAD - 4, 284, 250, 24);

        loginButton.setBounds(CARD_PAD, 328, fieldWidth, 46);

        signUpButton.setBounds(cardX, cardY + cardHeight + 14, cardWidth, 25);

        revalidate();
        repaint();
    }

    private String getSelectedRole() {
        if (studentRadio.isSelected()) return "student";
        if (lecturerRadio.isSelected()) return "lecturer";
        if (adminRadio.isSelected()) return "admin";
        return null;
    }

    // ---------- DATABASE-CONNECTED LOGIN CHECK ----------
    private String checkLogin(String usernameOrEmail, String password) {
        String sql = "SELECT password, role FROM users WHERE username = ? OR email = ?";

        try (java.sql.Connection conn = DB.DBConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null; // no matching user found
                }
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");

                if (!storedPassword.equals(password)) {
                    return null; // wrong password
                }
                return role.toLowerCase();
            }

        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == loginButton) {
            String selectedRole = getSelectedRole();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (selectedRole == null) {
                JOptionPane.showMessageDialog(this, "Please select a role to log in as.");
                return;
            }

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your username/email and password.");
                return;
            }

            String actualRole = checkLogin(username, password);

            if (actualRole == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            } else if (!actualRole.equals(selectedRole)) {
                JOptionPane.showMessageDialog(this,
                        "This account is not registered as a " + capitalize(selectedRole) + ".");
            } else {
                switch (actualRole) {

                    case "lecturer":
                        SwingUtilities.invokeLater(() -> new LecturerPortal().setVisible(true));
                        this.dispose();
                        break;
                    case "admin":
                        SwingUtilities.invokeLater(() -> new AdminDashboard(username).setVisible(true));
                        this.dispose();
                        break;
                    case "student":
                        SwingUtilities.invokeLater(() -> new DashboardApp(username).setVisible(true));
                        this.dispose();
                        break;

                }
            }
        } else if (event.getSource() == signUpButton) {
            SwingUtilities.invokeLater(() -> new SignUpForm().setVisible(true));
            this.dispose();
        } else if (event.getSource() == forgotPasswordButton) {
            JOptionPane.showMessageDialog(this, "Password reset flow goes here.");
        }
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}