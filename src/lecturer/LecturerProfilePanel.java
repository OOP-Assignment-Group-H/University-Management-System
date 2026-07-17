package lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

/**
 * Lecturer profile view: photo, personal details and an edit form.
 */
public class LecturerProfilePanel extends JPanel {

    private final Consumer<File> photoCallback;
    private JLabel photoLabel;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField departmentField;
    private JTextField officeField;

    public LecturerProfilePanel(Consumer<File> photoCallback) {
        this.photoCallback = photoCallback;
        setLayout(new BorderLayout(24, 0));
        setOpaque(false);

        add(buildPhotoCard(), BorderLayout.WEST);
        add(buildDetailsCard(), BorderLayout.CENTER);
    }

    private JPanel buildPhotoCard() {
        JPanel card = card(240);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        photoLabel = new JLabel(AvatarUtil.personIcon(110, Color.GRAY));
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton uploadBtn = new JButton("Change Photo");
        styleSecondaryButton(uploadBtn);
        uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png"));
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                photoLabel.setIcon(AvatarUtil.loadCircularIcon(file, 110));
                if (photoCallback != null) photoCallback.accept(file);
            }
        });

        card.add(Box.createVerticalStrut(10));
        card.add(photoLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(uploadBtn);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel buildDetailsCard() {
        JPanel card = card(0);
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridy = 0;

        JLabel heading = new JLabel("My Profile");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(LecturerPortal.TEXT_DARK);
        card.add(heading, gbc);
        gbc.gridy++;

        nameField = addField(card, gbc, "Full Name", "Dr. Alan Reed");
        emailField = addField(card, gbc, "Email", "a.reed@kln.ac.lk");
        phoneField = addField(card, gbc, "Phone", "+94 71 234 5678");
        departmentField = addField(card, gbc, "Department", "Computer Science");
        officeField = addField(card, gbc, "Office", "Room 204, ICT Building");

        JButton saveBtn = new JButton("Save Changes");
        stylePrimaryButton(saveBtn);
        saveBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Profile updated successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE));

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        card.add(saveBtn, gbc);

        return card;
    }

    private JTextField addField(JPanel card, GridBagConstraints gbc, String label, String value) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setForeground(LecturerPortal.TEXT_GREY);
        card.add(l, gbc);
        gbc.gridy++;

        JTextField field = new JTextField(value);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LecturerPortal.CARD_BORDER),
                new EmptyBorder(8, 10, 8, 10)));
        card.add(field, gbc);
        gbc.gridy++;

        return field;
    }

    private JPanel card(int fixedWidth) {
        JPanel panel = new JPanel();
        panel.setBackground(LecturerPortal.CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LecturerPortal.CARD_BORDER, 1, true),
                new EmptyBorder(24, 24, 24, 24)));
        if (fixedWidth > 0) {
            panel.setPreferredSize(new Dimension(fixedWidth, 0));
        }
        return panel;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setBackground(LecturerPortal.MAROON);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(LecturerPortal.MAROON);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LecturerPortal.MAROON),
                new EmptyBorder(8, 16, 8, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}