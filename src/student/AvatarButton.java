
package student;

import javax.swing.*;
import java.awt.*;

public class AvatarButton extends JButton {

    private final Color circleColor;

    public AvatarButton(String initials, Color circleColor) {
        super(initials);
        this.circleColor = circleColor;
        setFont(new Font("SansSerif", Font.BOLD, 13));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setPreferredSize(new Dimension(38, 38));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setInitials(String initials) {
        setText(initials);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(circleColor);
        g2.fillOval(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public boolean contains(int x, int y) {
        int r = getWidth() / 2;
        return (x - r) * (x - r) + (y - r) * (y - r) <= r * r;
    }
}