import javax.swing.SwingUtilities;
import view.SignUpForm;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new SignUpForm().setVisible(true);
        });

    }
}
