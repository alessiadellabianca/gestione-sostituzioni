package Interfaccia;

import Classi.Lezione;
import Gestori.GestioneDati;
import Gestori.GestoreDocenti;
import Gestori.GestoreSostituzioni;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FrameSostituzioni extends JPanel {

    private final List<JCheckBox> checkBoxList = new ArrayList<>();
    ArrayList<Lezione> listaLezioni = new ArrayList<>();
    File letturaFile = new File("letturaFile.txt");
    GestoreDocenti gestoreDocenti = new GestoreDocenti();
    JComboBox comboBox = new JComboBox();

    public FrameSostituzioni(GestioneDati gestore) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {}

        List<String> cognomiAggiunti = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(135, 206, 250));
        panelTop.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String data = LocalDate.now().toString();
        comboBox.addItem("Lunedì");
        comboBox.addItem("Martedì");
        comboBox.addItem("Mercoledì");
        comboBox.addItem("Giovedì");
        comboBox.addItem("Venerdì");
        comboBox.addItem("Sabato");

        JLabel descrizione = new JLabel("Seleziona i docenti assenti nella giornata di ", SwingConstants.CENTER);
        descrizione.setFont(new Font("Segoe UI", Font.BOLD, 16));
        descrizione.setForeground(Color.BLACK);
        panelTop.add(descrizione, BorderLayout.WEST);
        panelTop.add(comboBox, BorderLayout.CENTER);
        add(panelTop, BorderLayout.NORTH);

        int giorno = LocalDate.now().getDayOfWeek().getValue();
        caricaLezioni();
        gestoreDocenti.creaDocentiDaLezioni();

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBackground(Color.WHITE);

        JPanel panelCheckbox = new JPanel(new GridLayout(0, 2, 12, 12));
        panelCheckbox.setBackground(Color.WHITE);
        panelCheckbox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ArrayList<String> nomiDocenti = gestoreDocenti.getNomiDocenti();
        for (String docente : nomiDocenti) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {
                String[] parti = senzaVirgolette.split("\\s+");
                String cognome = parti[parti.length - 1];
                boolean giaPresente = cognomiAggiunti.contains(cognome);
                if (!giaPresente) {
                    cognomiAggiunti.add(cognome);
                    JCheckBox checkBox = new JCheckBox(senzaVirgolette);
                    checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    checkBox.setBackground(Color.WHITE);
                    checkBox.setFocusPainted(false);
                    checkBoxList.add(checkBox);
                    panelCheckbox.add(checkBox);
                }
            }
        }

        JScrollPane scroll = new JScrollPane(panelCheckbox);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panelCentro.add(scroll, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBottoni.setBackground(Color.WHITE);

        JButton conferma = new JButton("CONFERMA");
        conferma.setFont(new Font("Segoe UI", Font.BOLD, 14));
        conferma.setBackground(new Color(70, 130, 180));
        conferma.setForeground(Color.WHITE);
        conferma.setFocusPainted(false);
        conferma.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        conferma.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        JButton annulla = new JButton("ANNULLA");
        annulla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        annulla.setBackground(new Color(220, 220, 220));
        annulla.setForeground(Color.BLACK);
        annulla.setFocusPainted(false);
        annulla.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        annulla.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        panelBottoni.add(conferma);
        panelBottoni.add(annulla);
        add(panelBottoni, BorderLayout.SOUTH);

        conferma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> docentiSelezionati = new ArrayList<>();
                for (JCheckBox checkBox : checkBoxList) {
                    if (checkBox.isSelected()) {
                        docentiSelezionati.add(checkBox.getText());
                    }
                }
                if (!docentiSelezionati.isEmpty()) {
                    new GestoreSostituzioni(docentiSelezionati, gestoreDocenti, comboBox.getSelectedItem().toString());
                } else {
                    JOptionPane.showMessageDialog(FrameSostituzioni.this,
                            "Seleziona almeno un docente assente!",
                            "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        annulla.addActionListener(e -> {
            for (JCheckBox checkBox : checkBoxList) checkBox.setSelected(false);
        });
    }

    public void caricaLezioni() {
        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                if (parti.length < 8) continue;
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
                        } else continue;
                    } else {
                        String pulito = parti[4].replace("\"", "").trim();
                        if (!pulito.isEmpty()) docente.add(pulito);
                        if (parti.length > 8) {
                            codocenza = parti[6].contains("S");
                            giorno = parti[7];
                            ora = parti[8];
                            classe = parti[5];
                        } else continue;
                    }
                } else {
                    if (parti.length > 7) {
                        classe = parti[4];
                        codocenza = parti[5].contains("S");
                        giorno = parti[6];
                        ora = parti[7];
                    } else continue;
                }
                classe = classe.replace("\"", "").trim();
                giorno = giorno.replace("\"", "").trim();
                ora = ora.replace("\"", "").trim();
                materia = materia.replace("\"", "").trim();
                durata = durata.replace("\"", "").trim();
                if (!classe.isEmpty() && !giorno.isEmpty() && !ora.isEmpty() && !materia.isEmpty() && !docente.isEmpty()) {
                    listaLezioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
        gestoreDocenti.setTutteLezioni(listaLezioni);
    }

    public String controllaGiorno(int giorno) {
        return switch (giorno) {
            case 1 -> "Lunedì";
            case 2 -> "Martedì";
            case 3 -> "Mercoledì";
            case 4 -> "Giovedì";
            case 5 -> "Venerdì";
            case 6 -> "Sabato";
            default -> "nulla";
        };
    }
}
