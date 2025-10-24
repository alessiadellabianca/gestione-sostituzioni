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
    }

   /* public void creaOrarioAssenti()
    {

        panelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        for(String doc:docentiAssenti)
        {

        }

        for(Lezione l:lezioniDaSostituire)
        {
            if(l.codocenza)
            {
                for(String controlla: docentiAssenti)
                {

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

    }*/

    private void caricaLezioni() {

        ArrayList<Lezione> listaLezioni = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                String durata = parti[1];
                String materia = parti[2];
                ArrayList<String> docente = new ArrayList<>();
                docente.add(parti[3]);
                String classe;
                String giorno;
                String ora;
                boolean codocenza;

                if (parti[4].contains("Cognome")) {
                    if (parti[5].contains("Cognome")) {
                        docente.add(parti[4]);
                        docente.add(parti[5]);
                        classe = parti[6];
                        codocenza = parti[7].contains("S");
                        giorno = parti[8];
                        ora = parti[9];
                    } else {
                        docente.add(parti[4]);
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


    public JPanel creaPannelloLezione(Lezione l) {
        JPanel panel = new JPanel(new BorderLayout());
        ArrayList<String> docentiVari= new ArrayList<>();
        docentiVari.equals(l.getDocente());
        String robeDaMettere=null;
        for(String x: docentiVari) {
            robeDaMettere= robeDaMettere+" "+x;
        }
        JLabel docente = new JLabel(robeDaMettere, SwingConstants.CENTER);
        JLabel materia = new JLabel(l.getMateria(), SwingConstants.CENTER);
        panel.add(docente, BorderLayout.NORTH);
        panel.add(materia, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(120, 60));
        return panel;
    }

}
