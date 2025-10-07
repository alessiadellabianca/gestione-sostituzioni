import java.io.*;
import java.util.ArrayList;

public class CreazioneOrario {

    GestioneDati datiGenerali=new GestioneDati();
    ArrayList<String> docenti = new ArrayList();
    ArrayList<String> classi = new ArrayList();
    ArrayList<String> materie = new ArrayList();
    String docente;
    String materia;
    String ora;
    String durata;
    String classe;
    boolean codocenza;
    String[] giorni = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
    ArrayList<Lezione> listaLezioni = new ArrayList();


    File letturaFile = new File("letturaFile.txt");



    public CreazioneOrario() {

        docenti=datiGenerali.getDocenti();
        classi=datiGenerali.getClassi();
        materie=datiGenerali.getMaterie();
    }

     public void crazioneOrarioClasse(String classe) throws FileNotFoundException {

         try (BufferedReader br = new BufferedReader(new FileReader(letturaFile))) {
             String linea = null;
             while ((linea = br.readLine()) != null) {


                 String[][] dati=new String[6][6];

                 String[] parti = linea.split(";");

                 materia=parti[2];
                 durata=parti[1];

                 if(parti[4].contains("Cognome"))
                 {
                     docenti.add(parti[4]);
                     classi.add(parti[5]);
                 } else {
                     classi.add(parti[4]);
                 }


             }
         } catch (IOException e) {
             System.out.println("Errore nella lettura del file: " + e.getMessage());
         }
     }



     }



