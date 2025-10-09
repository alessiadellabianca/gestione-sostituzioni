import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GestioneDati {

    private static ArrayList<String>  docenti = new ArrayList();
    private static ArrayList<String> classi = new ArrayList();
    private static ArrayList<String> materie = new ArrayList();


    public static void organizzazioneFile(String nomeFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeFile))) {
            String linea = null;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");

                materie.add(parti[2]);

                if (parti.length > 3) {
                    docenti.add(parti[3]);
                }

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

    public ArrayList<String> getMaterie() {
        return materie;
    }

    public ArrayList<String> getDocenti() {
        return docenti;
    }

    public ArrayList<String> getClassi() {
        return classi;
    }

    @Override
    public String toString() {
        return  "GestioneDati{" +
                "docenti=" + classi +
                '}';
    }
}
