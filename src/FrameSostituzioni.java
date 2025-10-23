import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FrameSostituzioni extends JPanel {

    private final List<JCheckBox> checkBoxList = new ArrayList<>();

    public FrameSostituzioni(GestioneDati gestore) {
        List<String> cognomiAggiunti = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        String data=new String();
        data= LocalDate.now().toString();
        JLabel descrizioni = new JLabel("Seleziona i docenti assenti nella giornata di: "+data+"  ");

        JButton conferma = new JButton("CONFERMA");
        conferma.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        conferma.setBackground(new Color(135, 206, 250));
        conferma.setForeground(Color.BLACK);
        conferma.setFocusPainted(false);
        panel.add(descrizioni);
        panel.add(conferma);
        panel.add(descrizioni);
        panel.add(conferma);

        JPanel pannelloConferma = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloConferma.add(panel);

        add(pannelloConferma, BorderLayout.NORTH);

        JPanel panelCheckbox = new JPanel(new GridLayout(0, 1, 10, 10));



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

        conferma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

              //  new GestoreSostituzioni();
            }
        });



    }
}
