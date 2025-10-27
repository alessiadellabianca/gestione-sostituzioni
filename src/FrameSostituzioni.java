import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FrameSostituzioni extends JPanel {

    private final List<JCheckBox> checkBoxList = new ArrayList<>();
    ArrayList<Lezione> listaLezioni = new ArrayList<>();
    File letturaFile = new File("letturaFile.txt");
    GestoreDocenti gestoreDocenti = new GestoreDocenti();

    public FrameSostituzioni(GestioneDati gestore) {


        List<String> cognomiAggiunti = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        String data;
        data = LocalDate.now().toString();
        JLabel descrizioni = new JLabel("Seleziona i docenti assenti nella giornata di: " + data + "  ");

        int giorno;
        giorno = LocalDate.now().getDayOfWeek().getValue();
        caricaLezioni();
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


                new GestoreSostituzioni(cognomiAggiunti, gestoreDocenti, controllaGiorno(giorno));
            }
        });


    }

    public void caricaLezioni() {
        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                String durata = parti[1];
                String materia = parti[2];

                ArrayList<String> docente = new ArrayList<>();

                String[] docentiSplit = parti[3].split(";");
                for (String d : docentiSplit) {
                    String pulito = d.replace("\"", "").trim();
                    if (!pulito.isEmpty()) docente.add(pulito);
                }

                String classe;
                String giorno;
                String ora;
                boolean codocenza;

                if (parti[4].contains("Cognome")) {
                    if (parti[5].contains("Cognome")) {
                        // aggiungi anche questi eventuali docenti
                        String[] altriDoc = {parti[4], parti[5]};
                        for (String d : altriDoc) {
                            String pulito = d.replace("\"", "").trim();
                            if (!pulito.isEmpty()) docente.add(pulito);
                        }
                        classe = parti[6];
                        codocenza = parti[7].contains("S");
                        giorno = parti[8];
                        ora = parti[9];
                    } else {
                        String pulito = parti[4].replace("\"", "").trim();
                        if (!pulito.isEmpty()) docente.add(pulito);
                        codocenza = parti[6].contains("S");
                        giorno = parti[7];
                        ora = parti[8];
                        classe = parti[5];
                    }
                } else {
                    classe = parti[4];
                    codocenza = parti[5].contains("S");
                    giorno = parti[6];
                    ora = parti[7];
                }

                listaLezioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                System.out.println(docente);
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
        gestoreDocenti.setTutteLezioni(listaLezioni);
    }

    public String controllaGiorno(int giorno) {
        if (giorno == 1) {
            return "Lunedì";
        } else if (giorno == 2) {
            return "Martedì";
        } else if (giorno == 3) {
            return "Mercoledì";
        } else if (giorno == 4) {
            return "Giovedì";
        } else if (giorno == 5) {
            return "Venerdì";
        } else if (giorno == 6) {
            return "Sabato";
        }
        return "nulla";
    }
}
