import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FrameDocenti extends JPanel {

    JComboBox<String> orarioDocente = new JComboBox<>();

    public FrameDocenti(GestioneDati gestore) {
        setLayout(new BorderLayout());

        for (String docente : gestore.getDocenti()) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {
                orarioDocente.addItem(senzaVirgolette);
            }
        }

        add(orarioDocente, BorderLayout.CENTER);
    }
}
