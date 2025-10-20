import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class CreazioneOrario extends JPanel {

    ArrayList<Lezione> listaLezioni = new ArrayList<>();
    ArrayList<Lezione> orarioClassex = new ArrayList<>();
    JPanel panelloOrario = new JPanel();
    File letturaFile = new File("letturaFile.txt");
    GestioneDati gestore = new GestioneDati();
    Border bordo = BorderFactory.createLineBorder(Color.BLACK);

    String[] giorni = {"Ora", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
    String[] giorniPerFile = {"lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"};
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};

    public CreazioneOrario(GestioneDati gestore) {
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
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }

    public void creazioneOrarioClasse(String classeSelezionata) {
        panelloOrario.removeAll();
        orarioClassex.clear();

        for (Lezione l : listaLezioni) {
            if (l.getClasse().equalsIgnoreCase(classeSelezionata)) {
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

        for (int col = 1; col < giorni.length; col++) {
            String giornoAttuale = giorniPerFile[col - 1];
            int riga = 1;

            for (Lezione lezione : orarioClassex) {
                if (!lezione.getGiorno().equalsIgnoreCase(giornoAttuale)) continue;

                int altezza = calcolaDurataBlocchi(lezione);
                c.gridx = col;
                c.gridy = riga;
                c.gridheight = altezza;
                panelloOrario.add(creaPannelloLezione(lezione), c);
                riga += altezza;
            }

            while (riga <= 6) {
                c.gridx = col;
                c.gridy = riga;
                c.gridheight = 1;
                JPanel vuoto = new JPanel();
                vuoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                panelloOrario.add(vuoto, c);
                riga++;
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
        JLabel docente = new JLabel(l.getDocente(), SwingConstants.CENTER);
        JLabel materia = new JLabel(l.getMateria(), SwingConstants.CENTER);
        panel.add(docente, BorderLayout.NORTH);
        panel.add(materia, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.sfonfoMaterie(l, panel);
        //panel.setBackground(new Color(173, 216, 230));
        panel.setPreferredSize(new Dimension(120, 60));
        return panel;
    }

    public void sfonfoMaterie(Lezione l, JPanel panel) {
        String materia = l.getMateria();

        if (materia.equals("Lettere")) {
            panel.setBackground(new Color(255, 204, 204));
        }
        else if (materia.equals("TTRG")) {
            panel.setBackground(new Color(204, 255, 255));
        }
        else if (materia.equals("A TE/PRO .")) {
            panel.setBackground(new Color(255, 255, 204));
        }
        else if (materia.equals("TPSIT")) {
            panel.setBackground(new Color(204, 255, 204));
        }
        else if (materia.equals("Informatica")) {
            panel.setBackground(new Color(153, 204, 255));
        }
        else if (materia.equals("GPOI")) {
            panel.setBackground(new Color(255, 153, 204));
        }
        else if (materia.equals("Tec mec")) {
            panel.setBackground(new Color(192, 192, 192));
        }
        else if (materia.equals("A LING ED. FIS")) {
            panel.setBackground(new Color(255, 204, 153));
        }
        else if (materia.equals("Religione")) {
            panel.setBackground(new Color(204, 153, 255));
        }
        else if (materia.equals("Meccanica")) {
            panel.setBackground(new Color(153, 153, 255));
        }
        else if (materia.equals("Inglese")) {
            panel.setBackground(new Color(250, 86, 86));
        }
        else if (materia.equals("Storia")) {
            panel.setBackground(new Color(255, 255, 153));
        }
        else if (materia.equals("Dir ec pol")) {
            panel.setBackground(new Color(255, 153, 153));
        }
        else if (materia.equals("Chimica")) {
            panel.setBackground(new Color(204, 255, 153));
        }
        else if (materia.equals("Scienze")) {
            panel.setBackground(new Color(153, 255, 204));
        }
        else if (materia.equals("LTE")) {
            panel.setBackground(new Color(255, 204, 255));
        }
        else if (materia.equals("Dir leg tur")) {
            panel.setBackground(new Color(255, 230, 204));
        }
        else if (materia.equals("A TE/PRO FIS")) {
            panel.setBackground(new Color(204, 255, 230));
        }
        else if (materia.equals("TTIM")) {
            panel.setBackground(new Color(204, 204, 255));
        }
        else if (materia.equals("Matematica")) {
            panel.setBackground(new Color(255, 204, 102));
        }
        else if (materia.equals("Estimo")) {
            panel.setBackground(new Color(255, 255, 102));
        }
        else if (materia.equals("Disposizione")) {
            panel.setBackground(new Color(240, 240, 240));
        }
        else if (materia.equals("Fisica")) {
            panel.setBackground(new Color(67, 170, 170));
        }
        else if (materia.equals("Sistemi")) {
            panel.setBackground(new Color(204, 255, 204));
        }
        else if (materia.equals("A MA/SCI MAT")) {
            panel.setBackground(new Color(255, 230, 230));
        }
        else if (materia.equals("Tec inf")) {
            panel.setBackground(new Color(204, 255, 153));
        }
        else if (materia.equals("Pr. Vegetali")) {
            panel.setBackground(new Color(204, 255, 102));
        }
        else if (materia.equals("Tedesco")) {
            panel.setBackground(new Color(255, 153, 102));
        }
        else if (materia.equals("Sc motorie")) {
            panel.setBackground(new Color(153, 255, 153));
        }
        else if (materia.equals("A MA/SCI SCI")) {
            panel.setBackground(new Color(255, 204, 255));
        }
        else if (materia.equals("TEEA")) {
            panel.setBackground(new Color(204, 204, 204));
        }
        else if (materia.equals("A TE/PRO.")) {
            panel.setBackground(new Color(255, 229, 204));
        }
        else if (materia.equals("A TE/PRO..")) {
            panel.setBackground(new Color(255, 229, 255));
        }
        else if (materia.equals("STA")) {
            panel.setBackground(new Color(204, 229, 255));
        }
        else if (materia.equals("A LING REL")) {
            panel.setBackground(new Color(229, 204, 255));
        }
        else if (materia.equals("Dis prog")) {
            panel.setBackground(new Color(255, 255, 229));
        }
        else if (materia.equals("Comp. mat.")) {
            panel.setBackground(new Color(229, 255, 204));
        }
        else if (materia.equals("Spagnolo")) {
            panel.setBackground(new Color(255, 178, 102));
        }
        else if (materia.equals("Geografia")) {
            panel.setBackground(new Color(204, 229, 204));
        }
        else if (materia.equals("TMA")) {
            panel.setBackground(new Color(255, 204, 204));
        }
        else if (materia.equals("A LING ITA")) {
            panel.setBackground(new Color(255, 229, 229));
        }
        else if (materia.equals("Ec. aziendale")) {
            panel.setBackground(new Color(255, 255, 204));
        }
        else if (materia.equals("A ST/SO DIR")) {
            panel.setBackground(new Color(229, 229, 255));
        }
        else if (materia.equals("A LING ING")) {
            panel.setBackground(new Color(36, 255, 255));
        }
        else if (materia.equals("A  ST/SO STO")) {
            panel.setBackground(new Color(255, 204, 229));
        }
        else if (materia.equals("Pr. Animali")) {
            panel.setBackground(new Color(204, 255, 153));
        }
        else if (materia.equals("Arte")) {
            panel.setBackground(new Color(255, 204, 255));
        }
        else if (materia.equals("Trasformazioni")) {
            panel.setBackground(new Color(204, 255, 204));
        }
        else if (materia.equals("Telecom")) {
            panel.setBackground(new Color(153, 204, 255));
        }
        else if (materia.equals("Biot. Agrarie")) {
            panel.setBackground(new Color(204, 255, 204));
        }
        else if (materia.equals("Genio Rurale")) {
            panel.setBackground(new Color(229, 255, 204));
        }
        else {
            panel.setBackground(new Color(255, 255, 255));
        }
    }




    public JPanel getPanelloOrario() {
        return panelloOrario;
    }

}
