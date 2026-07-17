package lecturer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Sidebar navigation item styled like the admin dashboard's left menu:
 * transparent by default, maroon highlight when active/hovered.
 */
class SidebarButton extends JButton {

    private boolean active = false;
    private boolean logoutStyle = false;

    public SidebarButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(LecturerPortal.SIDEBAR_TEXT);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 16));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        setForeground(active ? Color.WHITE : LecturerPortal.SIDEBAR_TEXT);
        repaint();
    }

    public void setLogoutStyle(boolean logoutStyle) {
        this.logoutStyle = logoutStyle;
        setForeground(new Color(230, 160, 160));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (active) {
            g2.setColor(LecturerPortal.MAROON);
            g2.fillRoundRect(10, 2, getWidth() - 20, getHeight() - 4, 10, 10);
        } else if (getModel().isRollover() && !logoutStyle) {
            g2.setColor(new Color(60, 60, 66));
            g2.fillRoundRect(10, 2, getWidth() - 20, getHeight() - 4, 10, 10);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}