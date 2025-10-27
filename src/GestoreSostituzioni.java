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

    public GestoreSostituzioni(List<String> docenti,GestoreDocenti gestoreDocenti,String giorno) {
        this.docentiAssenti = docenti;
        this.gestoreDocenti = gestoreDocenti;
        this.giorno = giorno;
        this.lezioniDaSostituire = new ArrayList<>();
        trovaLezioniDaSostituire();
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
                else if(l.getClasse().contains("5"))
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

                }
            }


        }
    }

    public ArrayList<Lezione> getLezioniDaSostituire() {
        return lezioniDaSostituire;
    }

    public ArrayList<Lezione> getLezioniSostituite() {
        return lezioniSostituite;
    }

    public JPanel creaPannelloLezione(Lezione l) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel docente = new JLabel(l.getClasse(), SwingConstants.CENTER);
        JLabel materia = new JLabel(l.getMateria(), SwingConstants.CENTER);
        panel.add(docente, BorderLayout.NORTH);
        panel.add(materia, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //panel.setBackground(new Color(173, 216, 230));
        panel.setPreferredSize(new Dimension(120, 60));
        return panel;
    }

    }
