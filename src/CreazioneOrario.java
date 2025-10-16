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
    GestioneDati gestore=new GestioneDati();

    String[] giorni = {"Ora", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
    String[] giorniPerFile = {"lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"};
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};
    String[] orePerFile = {"8", "9", "10", "11", "12", "13"};

    public CreazioneOrario(GestioneDati gestore) {
        setLayout(new BorderLayout());
        this.gestore=gestore;
        add(panelloOrario, BorderLayout.CENTER);
        caricaLezioni();
    }

    private void caricaLezioni() {
        String durata;
        String giorno=null;
        String ora=null;
        boolean codocenza=true;
        String materia=null;
        String classe=null;
        String docente=null;
        //ciao
        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                 durata=parti[1];
                 materia=parti[2];
                 docente=parti[3];
                if(parti[4].contains("Cognome"))
                {
                    if(parti[5].contains("Cognome"))
                    {
                        docente=docente+" "+parti[4]+" "+parti[5];
                        classe=parti[6];
                        if(parti[7].contains("S"))
                        {
                            codocenza=true;
                        }
                        else {
                            codocenza=false;
                        }
                        giorno=parti[8];
                        ora=parti[9];

                    }
                    else{
                        docente=docente+" "+parti[4];
                        if(parti[6].contains("S"))
                        {
                            codocenza=true;
                        }
                        else {
                            codocenza=false;
                        }
                        giorno=parti[7];
                        ora=parti[8];

                    }
                }
                else
                {
                    classe=parti[4];
                    if(parti[5].contains("S"))
                    {
                        codocenza=true;
                    }
                    else {
                        codocenza=false;
                    }
                    giorno=parti[6];
                    ora=parti[7];
                }
                listaLezioni.add(new Lezione(docente, codocenza, classe, materia,durata, ora, giorno));

            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }



    public void creazioneOrarioClasse(String classeSelezionata) {
        panelloOrario.removeAll();
        orarioClassex.clear();

        // Filtra le lezioni della classe selezionata
        for (Lezione l : listaLezioni) {
            if (l.getClasse().equals(classeSelezionata)) {
                orarioClassex.add(l);
            }
        }

        // Trova tutte le ore presenti nella classe (normalizzate)
        ArrayList<String> oreClasse = new ArrayList<>();
        for (Lezione l : orarioClassex) {
            String oraNorm = l.getOra().split("h")[0]; // "08h00" -> "08"
            oraNorm = String.valueOf(Integer.parseInt(oraNorm)); // "08" -> "8"
            if (!oreClasse.contains(oraNorm)) {
                oreClasse.add(oraNorm);
            }
        }
        oreClasse.sort((a, b) -> Integer.parseInt(a) - Integer.parseInt(b)); // ordina numericamente

        panelloOrario.setLayout(new GridLayout(oreClasse.size() + 1, giorni.length));
        Border bordo = BorderFactory.createLineBorder(Color.BLACK);

        // Intestazione giorni
        for (String g : giorni) {
            JLabel label = new JLabel(g, SwingConstants.CENTER);
            label.setBorder(bordo);
            panelloOrario.add(label);
        }

        // Popola la tabella
        for (String ora : oreClasse) {
            // Label ora
            JLabel labelOra = new JLabel(ora + ":00", SwingConstants.CENTER);
            labelOra.setBorder(bordo);
            panelloOrario.add(labelOra);

            // Celle per ogni giorno
            for (String giornoFile : giorniPerFile) {
                JPanel cella = new JPanel(new BorderLayout());
                cella.setBorder(bordo);
                String testoCella = "";

                for (Lezione l : orarioClassex) {
                    String oraNorm = l.getOra().split("h")[0];
                    oraNorm = String.valueOf(Integer.parseInt(oraNorm));

                    if (l.getGiorno().equalsIgnoreCase(giornoFile) && oraNorm.equals(ora)) {
                        testoCella = l.getMateria() + " - " + l.getDocente();
                        break;
                    }
                }

                JLabel labelLezione = new JLabel(testoCella, SwingConstants.CENTER);
                cella.add(labelLezione, BorderLayout.CENTER);
                panelloOrario.add(cella);
            }
        }

        panelloOrario.setPreferredSize(new Dimension(700, 300));
        revalidate();
        repaint();
    }


    public JPanel getPanelloOrario() {
        return panelloOrario;
    }
}
