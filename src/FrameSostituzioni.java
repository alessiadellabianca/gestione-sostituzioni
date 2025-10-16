/*import javax.swing.*;
import java.awt.*;

public class FrameSostituzioni extends JPanel
{
    JComboBox<String> sceltaDocente = new JComboBox<>();

    public FrameSostituzioni(GestioneDati gestore)
    {
        setLayout(new BorderLayout());

        for (String docente : gestore.getDocenti()) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {
                sceltaDocente.addItem(senzaVirgolette);
            }
        }

        add(sceltaDocente, BorderLayout.CENTER);




    }
}*/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrameSostituzioni extends JPanel {

    private final List<JCheckBox> checkBoxList = new ArrayList<>();

    public FrameSostituzioni(GestioneDati gestore) {
        setLayout(new BorderLayout());

        JPanel panelCheckbox = new JPanel(new GridLayout(1, 2));

        for (String docente : gestore.getDocenti()) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {
                JCheckBox checkBox = new JCheckBox(senzaVirgolette);
                checkBoxList.add(checkBox);
                panelCheckbox.add(checkBox);
            }
        }

        JScrollPane scrollPane = new JScrollPane(panelCheckbox);
        add(scrollPane, BorderLayout.CENTER);
    }

    public List<String> getDocentiSelezionati() {
        List<String> selezionati = new ArrayList<>();
        for (JCheckBox cb : checkBoxList) {
            if (cb.isSelected()) {
                selezionati.add(cb.getText());
            }
        }
        return selezionati;
    }
}

