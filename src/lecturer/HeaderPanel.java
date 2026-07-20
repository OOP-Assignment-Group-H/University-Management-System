package lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;


class HeaderPanel extends JPanel {

    private final JLabel titleLabel;
    private final JLabel subtitleLabel;
    private final JLabel avatarLabel;
    private final String lecturerName;

    public HeaderPanel(String title, String subtitle, String lecturerName) {
        this.lecturerName = lecturerName;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(18, 30, 18, 24));

        JPanel textBox = new JPanel();
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
        textBox.setOpaque(false);

        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(LecturerPortal.TEXT_DARK);

        subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(LecturerPortal.TEXT_GREY);

        textBox.add(titleLabel);
        textBox.add(Box.createVerticalStrut(4));
        textBox.add(subtitleLabel);
        add(textBox, BorderLayout.WEST);

        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        rightBox.setOpaque(false);

        JLabel bell = new JLabel("\uD83D\uDD14");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        rightBox.add(bell);

        avatarLabel = new JLabel(initialIcon(lecturerName));
        rightBox.add(avatarLabel);

        add(rightBox, BorderLayout.EAST);
    }

    public void setSectionTitle(String section) {
        titleLabel.setText(section);
    }

    public void updateAvatarPhoto(File file) {
        avatarLabel.setIcon(AvatarUtil.loadCircularIcon(file, 44));
    }

    private Icon initialIcon(String name) {
        String initial = (name == null || name.isEmpty()) ? "L" : name.trim().substring(0, 1).toUpperCase();
        int size = 44;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(LecturerPortal.MAROON);
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(initial)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(initial, x, y);
        g2.dispose();
        return new ImageIcon(img);
    }
}