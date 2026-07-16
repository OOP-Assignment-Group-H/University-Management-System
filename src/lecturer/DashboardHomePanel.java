package lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class DashboardHomePanel extends JPanel {

    private static final String IMAGE_PATH = "recources/images/images.jpg";
    private static final Color BG = LecturerPortal.PAGE_BG;

    public DashboardHomePanel(String lecturerName) {
        setLayout(new BorderLayout(0, 20));
        setBackground(BG);
        setOpaque(false);

        add(buildBanner(lecturerName), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setOpaque(false);
        center.add(buildStatsRow(), BorderLayout.NORTH);
        center.add(buildImageArea(), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildBanner(String lecturerName) {
        JPanel banner = new JPanel();
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBackground(LecturerPortal.MAROON);
        banner.setBorder(new EmptyBorder(22, 26, 22, 26));

        JLabel greeting = new JLabel("Welcome back, " + lecturerName + "!");
        greeting.setFont(new Font("Arial", Font.BOLD, 22));
        greeting.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Here's a quick overview of your courses and students.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(235, 210, 210));

        banner.add(greeting);
        banner.add(Box.createVerticalStrut(6));
        banner.add(sub);
        return banner;
    }

    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.add(statCard("Courses Teaching", "4"));
        row.add(statCard("Total Students", "212"));
        row.add(statCard("Pending Assignments", "6"));
        row.add(statCard("Upcoming Classes", "3 Today"));
        return row;
    }

    private JPanel statCard(String label, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(LecturerPortal.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LecturerPortal.CARD_BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));
        valueLabel.setForeground(LecturerPortal.MAROON);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        textLabel.setForeground(LecturerPortal.TEXT_GREY);

        card.add(valueLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(textLabel);
        return card;
    }

    private JPanel buildImageArea() {
        java.io.File imageFile = new java.io.File(IMAGE_PATH);
        Image campusImage = null;

        if (!imageFile.exists()) {
            System.out.println("Dashboard image NOT FOUND at: " + imageFile.getAbsolutePath());
        } else {
            campusImage = new ImageIcon(imageFile.getPath()).getImage();
        }

        return new RoundedImagePanel(campusImage, 18);
    }

    /** Rounded-corner panel that scales and clips an image to fill its bounds. */
    private static class RoundedImagePanel extends JPanel {
        private final Image image;
        private final int radius;

        RoundedImagePanel(Image image, int radius) {
            this.image = image;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

            if (image != null) {
                g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            } else {
                g2.setColor(LecturerPortal.CARD_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(LecturerPortal.TEXT_GREY);
                g2.setFont(new Font("Arial", Font.PLAIN, 13));
                String msg = "Image not found: " + IMAGE_PATH;
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            }
            g2.dispose();
        }
    }
}