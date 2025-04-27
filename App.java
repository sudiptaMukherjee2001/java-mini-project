import javax.swing.SwingUtilities;
import ui.MainUI;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI());
    }
}
