import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreazioneOrarioDocenti extends JPanel{

    ArrayList<Lezione> listaLezioni = new ArrayList<>();
    ArrayList<Lezione> orarioClassex = new ArrayList<>();
    JPanel panelloOrario = new JPanel();
    File letturaFile = new File("letturaFile.txt");
    GestioneDati gestore = new GestioneDati();
    Border bordo = BorderFactory.createLineBorder(Color.BLACK);

    String[] giorni = {"Ora", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
    String[] giorniPerFile = {"lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"};
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};
    String[] oreControllo = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00"};

    public CreazioneOrarioDocenti(GestioneDati gestore) {
        setLayout(new BorderLayout());
        this.gestore = gestore;
        JScrollPane scrollPane = new JScrollPane(panelloOrario);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        caricaLezioni();
    }

    private void caricaLezioni() {
        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                String durata = parti[1];
                String materia = parti[2];
                String docente = parti[3];
                String classe;
                String giorno;
                String ora;
                boolean codocenza;
                if (parti[4].contains("Cognome")) {
                    if (parti[5].contains("Cognome")) {
                        docente += " " + parti[4] + " " + parti[5];
                        classe = parti[6];
                        codocenza = parti[7].contains("S");
                        giorno = parti[8];
                        ora = parti[9];
                    } else {
                        docente += " " + parti[4];
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
    }

    public void creazioneOrarioTabella(String docenteSelezionato) {
        panelloOrario.removeAll();
        orarioClassex.clear();
        String doc=new String();
        doc=docenteSelezionato+" ";

        for (Lezione l : listaLezioni) {
            if (l.getDocente().contains(doc)) {
                orarioClassex.add(l);
            }
            else if(l.getDocente().equals(docenteSelezionato)) {
                orarioClassex.add(l);
            }
        }

        panelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        for (int i = 0; i < giorni.length; i++) {
            c.gridx = i;
            c.gridy = 0;
            c.gridheight = 1;
            JLabel label = new JLabel(giorni[i], SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setOpaque(true);
            label.setBackground(new Color(70, 130, 180));
            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            panelloOrario.add(label, c);
        }

        for (int i = 0; i < oreStampa.length; i++) {
            c.gridx = 0;
            c.gridy = i + 1;
            c.gridheight = 1;
            JLabel labelOra = new JLabel(oreStampa[i], SwingConstants.CENTER);
            labelOra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            labelOra.setOpaque(true);
            labelOra.setBackground(new Color(200, 200, 200));
            labelOra.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            panelloOrario.add(labelOra, c);
        }

        String[] oreNumeriche = {"8h", "9h", "10h", "11h", "12h", "13h"};

        for (int col = 1; col < giorni.length; col++) {
            String giornoAttuale = giorniPerFile[col - 1];
            int riga = 1;


            for (int cont = 0; cont < oreNumeriche.length; cont++) {
                boolean lezioneTrovata = false;

                for (Lezione lezione : orarioClassex) {
                    if (!lezione.getGiorno().equalsIgnoreCase(giornoAttuale)) continue;

                    if (lezione.getOra().contains(oreNumeriche[cont])) {
                        int altezza = calcolaDurataBlocchi(lezione);
                        c.gridx = col;
                        c.gridy = riga;
                        c.gridheight = altezza;
                        panelloOrario.add(creaPannelloLezione(lezione), c);
                        riga += altezza;
                        lezioneTrovata = true;
                        break;
                    }
                }
                if (lezioneTrovata==false) {
                    if(riga<=6)
                    {
                        c.gridx = col;
                        c.gridy = riga;
                        c.gridheight = 1;
                        JPanel vuoto = new JPanel();
                        vuoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                        panelloOrario.add(vuoto, c);
                        riga++;
                    }
                }
            }

        }

        for (Component comp : panelloOrario.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setPreferredSize(new Dimension(120, 60));
            }
        }

        panelloOrario.setPreferredSize(new Dimension(800, 400));
        revalidate();
        repaint();
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
        JLabel docente = new JLabel(l.getClasse(), SwingConstants.CENTER);
        JLabel materia = new JLabel(l.getMateria(), SwingConstants.CENTER);
        panel.add(docente, BorderLayout.NORTH);
        panel.add(materia, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.mettiColori(l, panel);
        //panel.setBackground(new Color(173, 216, 230));
        panel.setPreferredSize(new Dimension(120, 60));
        return panel;
    }

    public void mettiColori(Lezione l, JPanel panel) {  //rivedere bene che colori mettere
        String classe = l.getClasse();

        if (classe.equals("4^ B TUR")) {
            panel.setBackground(new Color(244, 67, 54));
        }
        else if (classe.equals("2^ D INF")) {
            panel.setBackground(new Color(33, 150, 243));
        }
        else if (classe.equals("2^ C IeFP")) {
            panel.setBackground(new Color(76, 175, 80));
        }
        else if (classe.equals("4^ D INF")) {
            panel.setBackground(new Color(255, 152, 0));
        }
        else if (classe.equals("3^ D INF")) {
            panel.setBackground(new Color(156, 39, 176));
        }
        else if (classe.equals("5^ D INF")) {
            panel.setBackground(new Color(0, 188, 212));
        }
        else if (classe.equals("4^ F MEC")) {
            panel.setBackground(new Color(121, 85, 72));
        }
        else if (classe.equals("3^ C IeFP")) {
            panel.setBackground(new Color(139, 195, 74));
        }
        else if (classe.equals("5^ B TUR")) {
            panel.setBackground(new Color(103, 58, 183));
        }
        else if (classe.equals("1^ G AGR")) {
            panel.setBackground(new Color(255, 193, 7));
        }
        else if (classe.equals("3^ F MEC")) {
            panel.setBackground(new Color(63, 81, 181));
        }
        else if (classe.equals("5^ F MEC")) {
            panel.setBackground(new Color(205, 220, 57));
        }
        else if (classe.equals("1^ D INF")) {
            panel.setBackground(new Color(0, 150, 136));
        }
        else if (classe.equals("1^ F MEC")) {
            panel.setBackground(new Color(255, 87, 34));
        }
        else if (classe.equals("2^G AGR")) {
            panel.setBackground(new Color(96, 125, 139));
        }
        else if (classe.equals("2^ F MEC")) {
            panel.setBackground(new Color(233, 30, 99));
        }
        else if (classe.equals("2^ A MAT")) {
            panel.setBackground(new Color(3, 169, 244));
        }
        else if (classe.equals("1^ A MAT")) {
            panel.setBackground(new Color(158, 158, 158));
        }
        else if (classe.equals("3^ B TUR")) {
            panel.setBackground(new Color(0, 188, 212));
        }
        else if (classe.equals("1^ C IeFP")) {
            panel.setBackground(new Color(255, 235, 59));
        }
        else if (classe.equals("5^ A MAT")) {
            panel.setBackground(new Color(255, 111, 0));
        }
        else if (classe.equals("3^ A MAT")) {
            panel.setBackground(new Color(0, 121, 107));
        }
        else if (classe.equals("4^ A MAT")) {
            panel.setBackground(new Color(186, 104, 200));
        }
        else if (classe.equals("3^G AGR")) {
            panel.setBackground(new Color(124, 179, 66));
        }
        else if (classe.equals("Disposizione")) {
            panel.setBackground(new Color(189, 189, 189));
        }
        else {
            panel.setBackground(new Color(255, 255, 255));
        }
    }


    public JPanel getPanelloOrario() {
        return panelloOrario;
    }
}