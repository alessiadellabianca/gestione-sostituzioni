import javax.xml.validation.SchemaFactoryConfigurationError;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class CreazioneOrario extends JPanel{
    ArrayList<String> docenti = new ArrayList();
    ArrayList<String> classi = new ArrayList();
    ArrayList<String> materie = new ArrayList();
    ArrayList<Lezione> orarioClassex=new ArrayList();
    String docente;
    String materia;
    String ora;
    String giorno;
    String durata;
    String classe;
    boolean codocenza;
    String[] giorni = {"Ora", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};
    String[] ore = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00"};

    ArrayList<Lezione> listaLezioni = new ArrayList();
    JPanel panelloOrario = new JPanel();



    File letturaFile = new File("letturaFile.txt");



    public CreazioneOrario(GestioneDati gestore) {

        setLayout(new BorderLayout());
        docenti=gestore.getDocenti();
        classi=gestore.getClassi();
        materie=gestore.getMaterie();
        crazioneLezione();
    }

    @Override
    public String toString() {
        return "CreazioneOrario{" +
                "listaLezioni=" + listaLezioni.toString() +
                '}';
    }

    public void crazioneLezione(){

         try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
             String linea = null;
             while ((linea = br.readLine()) != null) {

                 String[][] dati=new String[6][6];
                 String[] parti = linea.split(";");


                 materia=parti[2];
                 durata=parti[1];
                 docente=parti[3];

                 if(parti[4].contains("Cognome"))
                 {
                     if(parti[5].contains("Cognome"))
                     {
                         docente=docente+parti[4]+parti[5];
                         classe=parti[6];
                         if(parti[7].equals("S"))
                         {
                             codocenza=true;
                         }
                         else{
                             codocenza=false;
                         }
                         giorno=parti[8];
                         ora=parti[9];
                     }
                     else{
                         docente=docente+parti[4];
                         classe=parti[5];
                         if(parti[6].equals("S"))
                         {
                             codocenza=true;
                         }
                         else{
                             codocenza=false;
                         }
                         giorno=parti[7];
                         ora=parti[8];
                     }
                 }
                 {
                     classe=parti[4];
                     if(parti[5].equals("S"))
                     {
                         codocenza=true;
                     }
                     else{
                         codocenza=false;
                     }
                     giorno=parti[6];
                     ora=parti[7];
                 }

                 listaLezioni.add(new Lezione(docente,codocenza,classe,materia,durata,ora,giorno));


             }
         } catch (IOException e) {
             System.out.println("Errore nella lettura del file: " + e.getMessage());
         }

     }

    /*public void creazioneOrarioClasse(String classeDiCuiFareOrario)
    {

        orarioClassex.clear();
        for (int i = 0; i < listaLezioni.size(); i++) {
            if (listaLezioni.get(i).getClasse().equals(classeDiCuiFareOrario)) {
                orarioClassex.add(listaLezioni.get(i));  //Lezioni di ogni classe
            }
        }
        panelloOrario.removeAll();
        panelloOrario.setLayout(new GridLayout(7, 7));

        Border bordo = BorderFactory.createLineBorder(Color.BLACK);


        for (int i = 0; i < giorni.length; i++) { //imposta prima riga
            JLabel label = new JLabel(giorni[i], SwingConstants.CENTER);
            label.setBorder(bordo);
            panelloOrario.add(label);
        }
        for (int i = 0; i < ore.length; i++) { //imposta all'inizio di ogni riga l'ora
            JLabel labelOra = new JLabel(oreStampa[i], SwingConstants.CENTER);
            labelOra.setBorder(bordo);
            panelloOrario.add(labelOra);

            for (Lezione lezione : orarioClassex) {
                if (lezione.getGiorno().equals(giorni[j]) && lezione.getOra().equals(ore[i])) {
                    JLabel lezioneLabel = new JLabel(lezione.getMateria() + " - " + lezione.getDocente(), SwingConstants.CENTER);
                    panelloCella.add(lezioneLabel);
                }
            }
        }
        add(panelloOrario, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }*/

    public void creazioneOrarioClasse(String classeDiCuiFareOrario) {
        // Pulisce il pannello principale prima di ricreare il layout
        remove(panelloOrario);

        // Reset dati
        orarioClassex.clear();
        for (Lezione lezione : listaLezioni) {
            if (lezione.getClasse().equals(classeDiCuiFareOrario)) {
                orarioClassex.add(lezione);
            }
        }

        // Ricostruisce il panello dell’orario
        panelloOrario = new JPanel(); // Ricreazione per forzare il layout nuovo
        panelloOrario.setLayout(new GridLayout(7, 7));
        Border bordo = BorderFactory.createLineBorder(Color.BLACK);

        // Prima riga: Giorni
        for (String giorno : giorni) {
            JLabel label = new JLabel(giorno, SwingConstants.CENTER);
            label.setBorder(bordo);
            panelloOrario.add(label);
        }

        // Riga per ciascuna ora
        for (int i = 0; i < ore.length; i++) {
            // Etichetta ora (prima colonna)
            JLabel labelOra = new JLabel(oreStampa[i], SwingConstants.CENTER);
            labelOra.setBorder(bordo);
            panelloOrario.add(labelOra);

            // Crea celle vuote per ogni giorno della settimana
            for (int j = 1; j < giorni.length; j++) {
                JPanel panelloCella = new JPanel(new BorderLayout());
                panelloCella.setBorder(bordo);

                // Cerca se c'è una lezione in questo slot
                for (Lezione lezione : orarioClassex) {
                    if (lezione.getGiorno().equals(giorni[j]) && lezione.getOra().equals(ore[i])) {
                        JLabel lezioneLabel = new JLabel("<html>" + lezione.getMateria() + "<br>" + lezione.getDocente() + "</html>", SwingConstants.CENTER);
                        panelloCella.add(lezioneLabel, BorderLayout.CENTER);
                        break;
                    }
                }

                panelloOrario.add(panelloCella);
            }
        }

        // Aggiungi il pannello in basso (SOUTH)
        add(panelloOrario, BorderLayout.SOUTH);

        // Rinfresca l’interfaccia
        revalidate();
        repaint();
    }


    public JPanel getPanelloOrario() {
        return panelloOrario;
    }

    public String toStringclasse() {
        return "CreazioneOrario{" +
                "listaLezioni=" + orarioClassex.toString() +
                '}';
    }
}
