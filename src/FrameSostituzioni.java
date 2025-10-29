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

        // Inizializza i docenti dopo aver caricato le lezioni
        gestoreDocenti.creaDocentiDaLezioni();

        JButton conferma = new JButton("CONFERMA");
        conferma.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        conferma.setBackground(new Color(135, 206, 250));
        conferma.setForeground(Color.BLACK);
        conferma.setFocusPainted(false);
        panel.add(descrizioni);
        panel.add(conferma);

        JPanel pannelloConferma = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pannelloConferma.add(panel);

        add(pannelloConferma, BorderLayout.NORTH);

        JPanel panelCheckbox = new JPanel(new GridLayout(0, 1, 10, 10));

        // Ottieni la lista dei docenti dal GestoreDocenti invece che da GestioneDati
        ArrayList<String> nomiDocenti = gestoreDocenti.getNomiDocenti();
        for (String docente : nomiDocenti) {
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
                List<String> docentiSelezionati = new ArrayList<>();
                for (JCheckBox checkBox : checkBoxList) {
                    if (checkBox.isSelected()) {
                        docentiSelezionati.add(checkBox.getText());
                    }
                }

                if (!docentiSelezionati.isEmpty()) {
                    new GestoreSostituzioni(docentiSelezionati, gestoreDocenti, controllaGiorno(giorno));
                } else {
                    JOptionPane.showMessageDialog(FrameSostituzioni.this,
                        "Seleziona almeno un docente assente!",
                        "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


    }

    public void caricaLezioni() {
        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                if (parti.length < 8) continue; // Salta linee incomplete

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
                    if (parti.length > 5 && parti[5].contains("Cognome")) {
                        // aggiungi anche questi eventuali docenti
                        String[] altriDoc = {parti[4], parti[5]};
                        for (String d : altriDoc) {
                            String pulito = d.replace("\"", "").trim();
                            if (!pulito.isEmpty()) docente.add(pulito);
                        }
                        if (parti.length > 9) {
                            classe = parti[6];
                            codocenza = parti[7].contains("S");
                            giorno = parti[8];
                            ora = parti[9];
                        } else {
                            continue; // Salta linee incomplete
                        }
                    } else {
                        String pulito = parti[4].replace("\"", "").trim();
                        if (!pulito.isEmpty()) docente.add(pulito);
                        if (parti.length > 8) {
                            codocenza = parti[6].contains("S");
                            giorno = parti[7];
                            ora = parti[8];
                            classe = parti[5];
                        } else {
                            continue; // Salta linee incomplete
                        }
                    }
                } else {
                    if (parti.length > 7) {
                        classe = parti[4];
                        codocenza = parti[5].contains("S");
                        giorno = parti[6];
                        ora = parti[7];
                    } else {
                        continue; // Salta linee incomplete
                    }
                }

                // Pulisci i dati
                classe = classe.replace("\"", "").trim();
                giorno = giorno.replace("\"", "").trim();
                ora = ora.replace("\"", "").trim();
                materia = materia.replace("\"", "").trim();
                durata = durata.replace("\"", "").trim();

                if (!classe.isEmpty() && !giorno.isEmpty() && !ora.isEmpty() && !materia.isEmpty() && !docente.isEmpty()) {
                    listaLezioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                    System.out.println("Lezione caricata: " + docente + " - " + materia + " - " + classe + " - " + giorno + " " + ora);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
        gestoreDocenti.setTutteLezioni(listaLezioni);
        System.out.println("Totale lezioni caricate: " + listaLezioni.size());
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
