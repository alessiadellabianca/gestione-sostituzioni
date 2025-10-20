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
    ArrayList<Lezione> lezioniDaSostituire;
    ArrayList<Lezione> listaDisposizioni = new ArrayList<>();

    public GestoreSostituzioni(List<String> docenti,String giorno) {
        this.docentiAssenti = docenti;
        this.giorno = giorno;
        creaOrarioAssenti();
    }

    public void creaOrarioAssenti()
    {

        panelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        for(String doc:docentiAssenti)
        {
            lezioniDaSostituire=caricaLezioni(doc,giorno);
        }

        for(Lezione l:lezioniDaSostituire)
        {
            if(l.codocenza)
            {
                for(String controlla: docentiAssenti)
                {
                    //controllare se è assente quello in codocenza
                }
            }
            else
            {
                if(l.classe.contains("5"))
                {
                    for(Lezione cerca:listaDisposizioni)
                    {
                        if(l.ora.equals(cerca.ora))
                        {
                            panelloOrario.add(creaPannelloLezione(l),c);
                        }
                    }
                }
            }
        }

        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private ArrayList<Lezione> caricaLezioni(String docAssente,String g) {

        ArrayList<Lezione> listaLezioni = new ArrayList<>();

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

                String prova=docAssente+" ";

                if (docente.contains(prova) && giorno.contains(g)) {
                    listaLezioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                }
                else if(docente.equals(prova) && giorno.contains(g))
                {
                    listaLezioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
        return listaLezioni;
    }

    private void creaListaDisposizioni()
    {
        String durata;
        String giorno = null;
        String ora = null;
        boolean codocenza = true;
        String materia = null;
        String classe = null;
        String docente = null;
        String num = null;
        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");

                if (parti[2].contains("Disposizione")) {
                    durata = parti[1];
                    materia = parti[2];
                    docente = parti[3];

                    if (parti[4].contains("Cognome")) {
                        if (parti[5].contains("Cognome")) {
                            docente = docente + " " + parti[4] + " " + parti[5];
                            classe = parti[6];
                            codocenza = parti[7].contains("S");
                            giorno = parti[8];
                            ora = parti[9];
                        } else {
                            docente = docente + " " + parti[4];
                            codocenza = parti[6].contains("S");
                            giorno = parti[7];
                            ora = parti[8];
                        }
                    } else {
                        classe = parti[4];
                        codocenza = parti[5].contains("S");
                        giorno = parti[6];
                        ora = parti[7];
                    }
                    listaDisposizioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                }

            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }

    public JPanel creaPannelloLezione(Lezione l) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel docente = new JLabel(l.getDocente(), SwingConstants.CENTER);
        panel.add(docente, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setBackground(new Color(173, 216, 230));
        panel.setPreferredSize(new Dimension(120, 60));
        return panel;
    }

}
