import javax.swing.*;
import java.awt.*;

public class FrameDocenti extends JPanel {

    JComboBox<String> orario = new JComboBox<>();

    public FrameDocenti() {
        setLayout(new BorderLayout());

        // Prendo la lista docenti statica da GestioneDati
        for (String docente : GestioneDati.getDocenti()) {
            orario.addItem(docente);
        }

        add(orario, BorderLayout.CENTER);
    }
}
