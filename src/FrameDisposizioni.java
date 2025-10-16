import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrameDisposizioni extends JPanel {

    JComboBox<String> orario = new JComboBox<>();
    String[] sceltaGiorno = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};

    public FrameDisposizioni(GestioneDati gestore) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        for (String disp : sceltaGiorno) {
            orario.addItem(disp);
        }

        orario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orario.setBackground(new Color(135, 206, 250));
        orario.setForeground(Color.BLACK);

        add(orario, BorderLayout.NORTH);
    }
}
