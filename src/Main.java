import ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point for Heritage Ledger — Personal Finance Manager.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Keep default look-and-feel if system L&F is unavailable.
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
