package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;


public class ProfileDialog extends JDialog {


    private static final Color MAROON       = new Color(123, 17, 19);
    private static final Color MAROON_DARK  = new Color(95,  12, 14);
    private static final Color BG_LIGHT     = new Color(245, 247, 250);
    private static final Color CARD_WHITE   = Color.WHITE;
    private static final Color TEXT_DARK    = new Color(35, 35, 40);
    private static final Color TEXT_MUTED   = new Color(120, 120, 130);
    private static final Color BORDER_COLOR = new Color(220, 220, 228);
    private static final Color FIELD_BG     = new Color(250, 250, 252);
    private static final Color SUCCESS_GREEN = new Color(34, 139, 34);


    private String fullName    = "Admin User";
    private String username    = "admin";
    private String email       = "admin@uok.ac.lk";
    private String phone       = "0711234567";
    private String role        = "System Administrator";
    private String department  = "Faculty of Computing & Technology";

    // ── UI fields ──────────────────────────────────────────────────────────────
    private JTextField tfFullName, tfUsername, tfEmail, tfPhone, tfRole, tfDepartment;
    private JButton    btnEdit, btnSave, btnCancel;
    private JPanel     avatarPanel;
    private JLabel     avatarInitialsLabel;

    // ── Constructor ────────────────────────────────────────────────────────────
    public ProfileDialog(JFrame owner) {
        super(owner, "Admin Profile", true);          // modal dialog
        setSize(480, 620);
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUI();
        setFieldsEditable(false);                     // start in VIEW mode
    }

    // ── Build all UI ───────────────────────────────────────────────────────────
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_LIGHT);
        setContentPane(root);

        // ── 1. Top banner (maroon) + avatar ────────────────────────────────────
        JPanel banner = new JPanel(null);
        banner.setPreferredSize(new Dimension(480, 140));
        banner.setBackground(MAROON);

        // Title label inside banner
        JLabel titleLbl = new JLabel("My Profile");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 20));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBounds(20, 16, 200, 30);
        banner.add(titleLbl);

        JLabel subLbl = new JLabel("View and manage your account details");
        subLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        subLbl.setForeground(new Color(255, 200, 200));
        subLbl.setBounds(20, 44, 340, 20);
        banner.add(subLbl);

        // Circular avatar – placed so it overlaps the banner bottom edge
        avatarPanel = createCircularAvatar(72);
        avatarPanel.setBounds(190, 80, 90, 90);   // overlaps banner by ~50 px
        banner.add(avatarPanel);

        root.add(banner, BorderLayout.NORTH);

        // ── 2. Centre card ─────────────────────────────────────────────────────
        JPanel card = new JPanel();
        card.setBackground(CARD_WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(55, 30, 20, 30)); // top gap = avatar overlap

        // Name + role labels (read-only display, updated on save)
        JLabel lblName = new JLabel(fullName);
        lblName.setFont(new Font("Arial", Font.BOLD, 18));
        lblName.setForeground(TEXT_DARK);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRole = new JLabel(role);
        lblRole.setFont(new Font("Arial", Font.PLAIN, 13));
        lblRole.setForeground(TEXT_MUTED);
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblName);
        card.add(Box.createVerticalStrut(4));
        card.add(lblRole);
        card.add(Box.createVerticalStrut(20));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER_COLOR);
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        // ── Form fields ────────────────────────────────────────────────────────
        tfFullName   = addFormRow(card, "Full Name",   fullName);
        tfUsername   = addFormRow(card, "Username",    username);
        tfEmail      = addFormRow(card, "Email",       email);
        tfPhone      = addFormRow(card, "Phone",       phone);
        tfRole       = addFormRow(card, "Role",        role);
        tfDepartment = addFormRow(card, "Department",  department);

        card.add(Box.createVerticalStrut(12));

        // ── Action buttons ─────────────────────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setBackground(CARD_WHITE);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        btnEdit   = createButton("Edit Profile", MAROON,   Color.WHITE);
        btnSave   = createButton("Save Changes", SUCCESS_GREEN, Color.WHITE);
        btnCancel = createButton("Cancel",        new Color(180,180,180), Color.WHITE);

        btnSave.setVisible(false);
        btnCancel.setVisible(false);

        btnRow.add(btnEdit);
        btnRow.add(btnSave);
        btnRow.add(btnCancel);
        card.add(btnRow);

        // Wrap card in a scroll pane in case window is too small
        JScrollPane scroll = new JScrollPane(card,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(CARD_WHITE);

        root.add(scroll, BorderLayout.CENTER);

        // ── Wire listeners ─────────────────────────────────────────────────────
        btnEdit.addActionListener(e -> enterEditMode(lblName, lblRole));
        btnSave.addActionListener(e -> saveChanges(lblName, lblRole));
        btnCancel.addActionListener(e -> cancelEdit(lblName, lblRole));
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    /** Creates a labelled text field row and returns the JTextField. */
    private JTextField addFormRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        row.setBackground(CARD_WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        row.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(TEXT_MUTED);
        row.add(lbl, BorderLayout.NORTH);

        JTextField tf = new JTextField(value);
        tf.setFont(new Font("Arial", Font.PLAIN, 14));
        tf.setForeground(TEXT_DARK);
        tf.setBackground(FIELD_BG);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        tf.setPreferredSize(new Dimension(0, 38));
        row.add(tf, BorderLayout.CENTER);

        parent.add(row);
        return tf;
    }

    /** Creates a styled action button. */
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 38));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            Color original = bg;
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }

    /** Creates a circular avatar panel with initials painted on it. */
    private JPanel createCircularAvatar(int size) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // White ring (border effect)
                g2.setColor(Color.WHITE);
                g2.fillOval(1, 1, size - 2, size - 2);

                // Maroon circle
                g2.setColor(MAROON);
                g2.fillOval(4, 4, size - 8, size - 8);

                // Initials
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, size / 3));
                String initials = getInitials(fullName);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (size - fm.stringWidth(initials)) / 2;
                int ty = (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, tx, ty);
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(size + 10, size + 10));
        return panel;
    }

    /** Returns up to 2 uppercase initials from a name string. */
    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) return String.valueOf(parts[0].charAt(0)).toUpperCase();
        return (String.valueOf(parts[0].charAt(0)) + String.valueOf(parts[parts.length - 1].charAt(0))).toUpperCase();
    }

    // ── Mode transitions ───────────────────────────────────────────────────────

    private void enterEditMode(JLabel lblName, JLabel lblRole) {
        setFieldsEditable(true);
        btnEdit.setVisible(false);
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
        tfFullName.requestFocusInWindow();
    }

    private void saveChanges(JLabel lblName, JLabel lblRole) {
        // Basic validation
        if (tfFullName.getText().trim().isEmpty() || tfEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Full Name and Email cannot be empty.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Persist to local fields
        fullName   = tfFullName.getText().trim();
        username   = tfUsername.getText().trim();
        email      = tfEmail.getText().trim();
        phone      = tfPhone.getText().trim();
        role       = tfRole.getText().trim();
        department = tfDepartment.getText().trim();

        // Refresh display labels
        lblName.setText(fullName);
        lblRole.setText(role);

        // Repaint avatar with possibly new initials
        avatarPanel.repaint();

        setFieldsEditable(false);
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        btnEdit.setVisible(true);

        JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelEdit(JLabel lblName, JLabel lblRole) {
        // Restore original values
        tfFullName.setText(fullName);
        tfUsername.setText(username);
        tfEmail.setText(email);
        tfPhone.setText(phone);
        tfRole.setText(role);
        tfDepartment.setText(department);

        setFieldsEditable(false);
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
        btnEdit.setVisible(true);
    }

    private void setFieldsEditable(boolean editable) {
        Color bg = editable ? Color.WHITE : FIELD_BG;
        for (JTextField tf : new JTextField[]{tfFullName, tfUsername, tfEmail, tfPhone, tfRole, tfDepartment}) {
            tf.setEditable(editable);
            tf.setBackground(bg);
        }
    }
}
