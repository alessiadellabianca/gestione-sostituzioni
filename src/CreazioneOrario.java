import javax.xml.validation.SchemaFactoryConfigurationError;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class CreazioneOrario {

    GestioneDati datiGenerali=new GestioneDati();
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
    String[] giorni = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
    ArrayList<Lezione> listaLezioni = new ArrayList();


    File letturaFile = new File("letturaFile.txt");



    public CreazioneOrario() {

        docenti=datiGenerali.getDocenti();
        classi=datiGenerali.getClassi();
        materie=datiGenerali.getMaterie();
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
                 } else {
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

     public void creazioneOrarioClasse(String classeDiCuiFareOrario)
     {
         orarioClassex.clear();
         for (int i = 0; i < listaLezioni.size(); i++) {
             if (listaLezioni.get(i).getClasse().equals(classeDiCuiFareOrario)) {
                 orarioClassex.add(listaLezioni.get(i));
             }
         }



     }

    public String toStringclasse() {
        return "CreazioneOrario{" +
                "listaLezioni=" + orarioClassex.toString() +
                '}';
    }
    public static void main(String[] args) {
        CreazioneOrario x = new CreazioneOrario();
        x.crazioneLezione();
        x.creazioneOrarioClasse("3^ F MEC");
        System.out.println(x.toStringclasse());

    }
     }







