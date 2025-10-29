import javax.print.Doc;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreSostituzioni extends JFrame{
    List<String> docentiAssenti;
    String giorno;
    JPanel panelloOrario = new JPanel();
    File letturaFile = new File("letturaFile.txt");
    GestoreDocenti gestoreDocenti;
    ArrayList<Lezione> lezioniDaSostituire;
    ArrayList<Lezione> lezioniSostituite = new ArrayList<>();
    ArrayList<Lezione> listaDisposizioni = new ArrayList<>();
    File lettFile = new File("letturaFile.txt");
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};


    public GestoreSostituzioni(List<String> docenti,GestoreDocenti gestoreDocenti,String giorno) {
        this.docentiAssenti = docenti;
        this.gestoreDocenti = gestoreDocenti;
        this.giorno = giorno;
        this.lezioniDaSostituire = new ArrayList<>();
        creaListaOreDisponibili();
        trovaLezioniDaSostituire();
        tabellaDisposizioni(lezioniSostituite);

    }

    private void creaListaOreDisponibili() {
        try (BufferedReader br = new BufferedReader(new FileReader(lettFile))) {
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

    private void trovaLezioniDaSostituire() {
        lezioniDaSostituire.clear();
        boolean trovataSostituzione = false;

        for (String docenteAssente : docentiAssenti) {

            Docente docente = gestoreDocenti.trovaDocente(docenteAssente);
           for(Lezione l: docente.getLezioniDiQuestoDoc())
           {
               if(l.getGiorno().equalsIgnoreCase(giorno))
               {
                   lezioniDaSostituire.add(l);
               }
           }

            for(Lezione l: lezioniDaSostituire)
            {
                if(l.isCodocenza())
                {
                    lezioniSostituite.add(l);
                }
                else
                {

                    for(int i=0;i<listaDisposizioni.size();i++)
                    {
                        if(l.ora.equals(listaDisposizioni.get(i).getOra())
                                && l.getGiorno().equals(listaDisposizioni.get(i).getGiorno()))
                        {
                            lezioniSostituite.add(l);
                        }
                        /*else if(l.getClasse().contains("5"))
                        {
                            for(Docente nomeDocente : gestoreDocenti.trovaNomiDocentiPerClasse(l.getClasse()))
                            {
                                for(Lezione lezione : nomeDocente.getLezioniDiQuestoDoc())
                                {
                                    if(lezione.getGiorno().equals(giorno) && l.getOra().equals(lezione.getOra())&&trovataSostituzione == false)
                                    {
                                        lezioniSostituite.add(lezione);
                                        trovataSostituzione = true;
                                    }
                                }
                            }

                        }else if(!l.getClasse().contains("5"))
                        {
                            trovataSostituzione = true;
                        }*/
                    }

                }

            }


        }
    }

    public void tabellaDisposizioni(ArrayList<Lezione> lezioniSostituite) {
        panelloOrario.removeAll();


        panelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        // Intestazione vuota in alto a sinistra
        c.gridx = 0;
        c.gridy = 0;
        JLabel vuoto = new JLabel("", SwingConstants.CENTER);
        vuoto.setOpaque(true);
        vuoto.setBackground(new Color(220, 220, 220));
        vuoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelloOrario.add(vuoto, c);


        for (int i = 0; i < docentiAssenti.size(); i++) {
            c.gridx = i + 1;
            c.gridy = 0;
            JLabel labelDocente = new JLabel(docentiAssenti.get(i), SwingConstants.CENTER);
            labelDocente.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelDocente.setOpaque(true);
            labelDocente.setBackground(new Color(180, 180, 180));
            labelDocente.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelloOrario.add(labelDocente, c);
        }


        for (int i = 0; i < oreStampa.length; i++) {
            c.gridx = 0;
            c.gridy = i + 1;
            c.gridheight = 1;
            JLabel labelOra = new JLabel(oreStampa[i], SwingConstants.CENTER);
            labelOra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            labelOra.setOpaque(true);
            labelOra.setBackground(new Color(200, 200, 200));
            labelOra.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelloOrario.add(labelOra, c);
        }


        for (int col = 0; col < docentiAssenti.size(); col++) {
            String docenteCorrente = docentiAssenti.get(col);

            for (int riga = 0; riga < oreStampa.length; riga++) {
                String oraCorrente = oreStampa[riga].split("-")[0]; // Prende l'ora di inizio
                boolean lezioneTrovata = false;

                // Cerca le lezioni per questo docente e questa ora
                for (Lezione lezione : lezioniSostituite) {
                    if (lezione.getDocente().contains(docenteCorrente) &&
                        lezione.getOra().equals(oraCorrente)) {

                        int altezza = calcolaDurataBlocchi(lezione);
                        c.gridx = col + 1;
                        c.gridy = riga + 1;
                        c.gridheight = altezza;

                        panelloOrario.add(creaPannelloLezione(lezione), c);
                        lezioneTrovata = true;

                        // Salta le ore successive se la lezione dura più di un'ora
                        if (altezza > 1) {
                            riga += (altezza - 1);
                        }
                        break;
                    }
                }

                if (!lezioneTrovata) {
                    c.gridx = col + 1;
                    c.gridy = riga + 1;
                    c.gridheight = 1;
                    JPanel vuotoPanel = new JPanel();
                    vuotoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                    vuotoPanel.setBackground(Color.WHITE);
                    panelloOrario.add(vuotoPanel, c);
                }
            }
        }

        // Imposta le dimensioni preferite per i componenti
        for (Component comp : panelloOrario.getComponents()) {
            if (comp instanceof JPanel || comp instanceof JLabel) {
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




    }
