package Gestori;

import Classi.Lezione;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class CreazioneOrarioDisposizioni extends JPanel
{
    ArrayList<Lezione> listaDisposizioni = new ArrayList<>();
    ArrayList<Lezione> listaDisposizioniGiornox = new ArrayList<>();
    JPanel panelloOrarioDisposizioni = new JPanel();
    JPanel panelloOrario = new JPanel();
    File letturaFile = new File("letturaFile.txt");
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};
    Border bordo = BorderFactory.createLineBorder(Color.BLACK);

    public CreazioneOrarioDisposizioni() {
        setLayout(new BorderLayout());
        add(panelloOrarioDisposizioni, BorderLayout.CENTER);
        caricaLezioni();
    }

    private void caricaLezioni() {
        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                String durata = parti[1];
                String materia;

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

                if(parti[2].contains("Disposizione")) {
                    materia = parti[2];

                    if (parti[4].contains("Cognome")) {
                        if (parti[5].contains("Cognome")) {
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
                    listaDisposizioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                    System.out.println(docente);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }

    public void tabellaDisposizioni(String giornoselezionato) {
        panelloOrario.removeAll();
        listaDisposizioniGiornox.clear();

        for (Lezione l : listaDisposizioni) {
            if (l.getGiorno().equalsIgnoreCase(giornoselezionato)) {
                listaDisposizioniGiornox.add(l);
            }
        }
        panelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        for (int i = 0; i < oreStampa.length; i++) {
            c.gridx = 0;
            c.gridy = i + 1;
            JLabel labelOra = new JLabel(oreStampa[i], SwingConstants.CENTER);
            labelOra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            labelOra.setOpaque(true);
            labelOra.setBackground(new Color(200, 200, 200));
            labelOra.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelloOrario.add(labelOra, c);
        }

        String[] oreNumeriche = {"8h", "9h", "10h", "11h", "12h", "13h"};
        for (int riga = 0; riga < oreNumeriche.length; riga++) {
            String oraCorrente = oreNumeriche[riga];
            int colonna = 1;
            for(int i = 0; i < listaDisposizioniGiornox.size(); i++)
            {
                if (listaDisposizioniGiornox.get(i).getOra().contains(oraCorrente)) {
                    int durata = calcolaDurataBlocchi(listaDisposizioniGiornox.get(i));
                    c.gridx = colonna;
                    c.gridy = riga + 1;
                    c.gridheight = durata;
                    panelloOrario.add(creaPannelloLezione(listaDisposizioniGiornox.get(i)), c);
                    colonna++;
                }
            }
        }

        for (Component comp : panelloOrario.getComponents()) {
            if (comp instanceof JPanel || comp instanceof JLabel) {
                comp.setPreferredSize(new Dimension(120, 60));
            }
        }

        panelloOrario.setPreferredSize(new Dimension(900, 400));
        panelloOrarioDisposizioni.removeAll();
        panelloOrarioDisposizioni.add(panelloOrario, BorderLayout.CENTER);
        panelloOrarioDisposizioni.revalidate();
        panelloOrarioDisposizioni.repaint();
    }

    private int calcolaDurataBlocchi(Lezione lezione) {
        try {
            return Integer.parseInt(lezione.getDurata().split("h")[0].trim());
        } catch (Exception e) {
            return 1;
        }
    }


    public JPanel creaPannelloLezione(Lezione l) {
        JPanel panel = new JPanel(new BorderLayout());
        String risultato = String.join(" ", l.getDocente());
        JLabel docente = new JLabel(risultato, SwingConstants.CENTER);
        JLabel materia = new JLabel(l.getMateria(), SwingConstants.CENTER);
        panel.add(docente, BorderLayout.NORTH);
        panel.add(materia, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(new Color(135, 206, 250));
        panel.setPreferredSize(new Dimension(120, 60));
        return panel;
    }

    public JPanel getPanelloOrarioDisposizioni() {
        return panelloOrarioDisposizioni;
    }



}


