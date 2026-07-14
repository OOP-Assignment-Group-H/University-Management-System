package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.LocalTime;

public class WelcomePanel extends JPanel {

    private static final Color MAROON = new Color(123, 17, 19);
    private static final Color BG     = new Color(245, 247, 250);
    private static final Color WHITE  = Color.WHITE;


    private static final String IMAGE_PATH = "recources/images/fct full.jpeg";

    public WelcomePanel(String username) {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(buildBanner(username), BorderLayout.NORTH);
        add(buildImageArea(),      BorderLayout.CENTER);
    }

    // ── Top greeting banner (maroon card) ─────────────────────
    private JPanel buildBanner(String username) {
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MAROON);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        banner.setOpaque(false);
        banner.setBorder(new EmptyBorder(26, 30, 26, 30));
        banner.setPreferredSize(new Dimension(0, 130));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));

        JLabel greeting = new JLabel(greetingForTime() + ", " + username + "!");
        greeting.setFont(new Font("Arial", Font.BOLD, 24));
        greeting.setForeground(WHITE);

        JLabel subtitle = new JLabel("Here's a quick overview of your university management system.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        subtitle.setBorder(new EmptyBorder(6, 0, 0, 0));

        textCol.add(greeting);
        textCol.add(subtitle);

        banner.add(textCol, BorderLayout.WEST);
        return banner;
    }

    private String greetingForTime() {
        int hour = LocalTime.now().getHour();
        if (hour < 12) return "Good Morning";
        if (hour < 17) return "Good Afternoon";
        return "Good Evening";
    }

    // ── Center image area ──────────────────────────────────────
    private JPanel buildImageArea() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(30, 0, 0, 0));

        File file = new File(IMAGE_PATH);
        if (file.exists()) {
            Image original = new ImageIcon(IMAGE_PATH).getImage();
            wrap.add(new ScaledImagePanel(original), BorderLayout.CENTER);
        } else {
            JLabel placeholder = new JLabel("Place an image at: " + IMAGE_PATH);
            placeholder.setHorizontalAlignment(SwingConstants.CENTER);
            placeholder.setFont(new Font("Arial", Font.PLAIN, 13));
            placeholder.setForeground(new Color(150, 150, 160));
            wrap.add(placeholder, BorderLayout.CENTER);
        }

        return wrap;
    }

    // ── Panel that scales an image to fit itself, keeping aspect ratio ──
    private static class ScaledImagePanel extends JPanel {
        private final Image image;

        ScaledImagePanel(Image image) {
            this.image = image;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) return;

            int panelW = getWidth();
            int panelH = getHeight();
            int imgW   = image.getWidth(this);
            int imgH   = image.getHeight(this);
            if (imgW <= 0 || imgH <= 0 || panelW <= 0 || panelH <= 0) return;

            // Scale to fit inside the panel while preserving aspect ratio
            double scale = Math.min((double) panelW / imgW, (double) panelH / imgH);
            int drawW = (int) (imgW * scale);
            int drawH = (int) (imgH * scale);
            int x = (panelW - drawW) / 2;
            int y = (panelH - drawH) / 2;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, x, y, drawW, drawH, this);
            g2.dispose();
        }
    }
}