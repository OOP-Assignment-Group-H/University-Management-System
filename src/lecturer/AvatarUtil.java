package lecturer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class AvatarUtil {

    public static ImageIcon loadCircularIcon(File file, int size) {
        try {
            BufferedImage img = ImageIO.read(file);
            BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = circle.createGraphics();

            g.setClip(new Ellipse2D.Float(0, 0, size, size));
            g.drawImage(img, 0, 0, size, size, null);
            g.dispose();

            return new ImageIcon(circle);
        } catch (Exception e) {
            return null;
        }
    }

    public static ImageIcon personIcon(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setColor(color);
        g.fillOval(0, 0, size, size);

        g.setColor(Color.WHITE);
        g.fillOval(size/4, size/4, size/2, size/2);

        g.dispose();
        return new ImageIcon(img);
    }
}