import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrameDisposizioni extends JPanel {

    JComboBox<String> orario = new JComboBox<>();
    String[] sceltaOrari = {"disp1", "disp33", "disp35"};

    public FrameDisposizioni(GestioneDati gestore) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        for (String disp : sceltaOrari) {
            orario.addItem(disp);
        }

        orario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orario.setBackground(new Color(135, 206, 250));
        orario.setForeground(Color.BLACK);

        add(orario, BorderLayout.NORTH);
    }
}
