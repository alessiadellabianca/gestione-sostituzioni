import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrameDocenti extends JPanel {

    JComboBox<String> orarioDocente = new JComboBox<>();

    public FrameDocenti(GestioneDati gestore) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        for (String docente : gestore.getDocenti()) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {
                orarioDocente.addItem(senzaVirgolette);
            }
        }

        orarioDocente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orarioDocente.setBackground(new Color(135, 206, 250));
        orarioDocente.setForeground(Color.BLACK);

        add(orarioDocente, BorderLayout.NORTH);
    }
}
