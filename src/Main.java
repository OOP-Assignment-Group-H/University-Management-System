import javax.swing.SwingUtilities;
import view.MyFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { new MyFrame().setVisible(true);});
    }
}