import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrameSostituzioni extends JPanel {

    private final List<JCheckBox> checkBoxList = new ArrayList<>();

    public FrameSostituzioni(GestioneDati gestore) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JButton conferma = new JButton("CONFERMA");
        conferma.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        conferma.setBackground(new Color(135, 206, 250));
        conferma.setForeground(Color.BLACK);
        conferma.setFocusPainted(false);

        JPanel pannelloConferma = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloConferma.add(conferma);

        add(pannelloConferma, BorderLayout.NORTH);

        JPanel panelCheckbox = new JPanel(new GridLayout(0, 1, 10, 10));

        List<String> cognomiAggiunti = new ArrayList<>();

        for (String docente : gestore.getDocenti()) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {

                String[] parti = senzaVirgolette.split("\\s+");
                String cognome = parti[parti.length - 1];

                boolean giaPresente = false;
                for (String c : cognomiAggiunti) {
                    if (c.equals(cognome)) {
                        giaPresente = true;
                        break;
                    }
                }

                if (!giaPresente) {
                    cognomiAggiunti.add(cognome);
                    JCheckBox checkBox = new JCheckBox(senzaVirgolette);

                    checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    panelCheckbox.add(checkBox);
                    checkBoxList.add(checkBox);
                }
            }
        }

        JScrollPane check = new JScrollPane(panelCheckbox);
        check.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        check.getVerticalScrollBar().setUnitIncrement(16);

        add(check, BorderLayout.CENTER);

        // Esempio ActionListener per il bottone conferma (da personalizzare)
        conferma.addActionListener(e -> {
            List<String> selezionati = new ArrayList<>();
            for (JCheckBox cb : checkBoxList) {
                if (cb.isSelected()) {
                    selezionati.add(cb.getText());
                }
            }
            JOptionPane.showMessageDialog(this, "Hai selezionato: " + selezionati);
        });
    }
}
