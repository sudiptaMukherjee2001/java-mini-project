import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class ToastMessage extends JDialog {
    public ToastMessage(String message, int duration) {
        super();
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout());

        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 170));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(label, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center screen

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dispose();
            }
        }, duration);

        setVisible(true);
    }
}
