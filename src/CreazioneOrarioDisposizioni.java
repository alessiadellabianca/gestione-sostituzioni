/*import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class CreazioneOrarioDisposizioni extends JPanel
{
    ArrayList<Lezione> listaDisposizioni = new ArrayList<>();
    JPanel panelloOrarioDisposizioni = new JPanel();
    File letturaFile = new File("letturaFile.txt");
    GestioneDati gestore = new GestioneDati();
    Border bordo = BorderFactory.createLineBorder(Color.BLACK);

    public CreazioneOrarioDisposizioni() {
        setLayout(new BorderLayout());
        this.gestore=gestore;
        add(panelloOrarioDisposizioni, BorderLayout.CENTER);
        caricaLezioni();
    }

    private void caricaLezioni(){

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

                if(parti[2].contains("Disposizione"))
                {
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

                    listaDisposizioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                }

            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }

    }

    public ArrayList<Lezione> getListaDisposizioni() {
        return listaDisposizioni;
    }



    public static void main(String[] args) {
        CreazioneOrarioDisposizioni g = new CreazioneOrarioDisposizioni();
        g.caricaLezioni();
        System.out.println(g.toString());
    }
}
*/

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class CreazioneOrarioDisposizioni extends JPanel
{
    ArrayList<Lezione> listaDisposizioni = new ArrayList<>();
    JPanel panelloOrarioDisposizioni = new JPanel();
    File letturaFile = new File("letturaFile.txt");
    GestioneDati gestore = new GestioneDati();
    Border bordo = BorderFactory.createLineBorder(Color.BLACK);

    public CreazioneOrarioDisposizioni() {
        setLayout(new BorderLayout());
        this.gestore = gestore;
        add(panelloOrarioDisposizioni, BorderLayout.CENTER);
        caricaLezioni();
    }

    private void caricaLezioni() {

        String durata;
        String giorno = null;
        String ora = null;
        boolean codocenza = true;
        String materia = null;
        String classe = null;
        String docente = null;
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

                    // Estraggo solo il cognome (ultima parola in docente)
                    String[] partiDocente = docente.trim().split("\\s+");
                    String cognome = partiDocente[partiDocente.length - 1];

                    // Aggiungo alla lista usando solo cognome e ora (e gli altri dati come prima)
                    listaDisposizioni.add(new Lezione(cognome, codocenza, classe, materia, durata, ora, giorno));
                }

            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }

    }

    public ArrayList<Lezione> getListaDisposizioni() {
        return listaDisposizioni;
    }

    @Override
    public String toString() {
        return "CreazioneOrarioDisposizioni{" +
                "listaDisposizioni=" + listaDisposizioni +
                '}';
    }

    public static void main(String[] args) {
        CreazioneOrarioDisposizioni g = new CreazioneOrarioDisposizioni();
        g.caricaLezioni();
        System.out.println(g.toString());
    }
}
