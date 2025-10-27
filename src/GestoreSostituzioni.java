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

    public GestoreSostituzioni(List<String> docenti,GestoreDocenti gestoreDocenti,String giorno) {
        this.docentiAssenti = docenti;
        this.gestoreDocenti = gestoreDocenti;
        this.giorno = giorno;
        this.lezioniDaSostituire = new ArrayList<>();
        trovaLezioniDaSostituire();
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
