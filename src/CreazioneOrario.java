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

    String[] giorni = {"Ora", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};
    String[] giorniPerFile = {"lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"};
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};
    String[] orePerFile = {"08h00", "09h00", "10h00", "11h00", "12h00", "13h00"};

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

        for (Lezione l : listaLezioni) {
            if (l.getClasse().equals(classeSelezionata)) {
                orarioClassex.add(l);
            }
        }

        GridBagConstraints tabella = new GridBagConstraints();
        panelloOrario.setLayout(new GridBagLayout());
        Border bordo = BorderFactory.createLineBorder(Color.BLACK);

        for(int i=0; i< giorni.length;i++)
        {
            tabella.gridx=i;
            tabella.gridy=0;
            tabella.ipadx = 50;  // aumenta larghezza minima
            tabella.ipady = 30;  // aumenta altezza minima
            JLabel label = new JLabel(giorni[i],SwingConstants.CENTER);
            label.setBorder(bordo);
            panelloOrario.add(label,tabella);
        }


        panelloOrario.setPreferredSize(new Dimension(800, 500));
        revalidate();
        repaint();
    }


    public JPanel getPanelloOrario() {
        return panelloOrario;
    }
}
