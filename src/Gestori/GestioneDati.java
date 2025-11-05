package Gestori;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GestioneDati {

    private static ArrayList<String>  docenti = new ArrayList();
    private static ArrayList<String>  docentimodificati = new ArrayList();
    private static ArrayList<String> classi = new ArrayList();
    private static ArrayList<String> materie = new ArrayList();

    public void organizzazioneFile(String nomeFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeFile))) {
            String linea = null;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");

                if(!materie.contains(parti[2]))
                {
                    materie.add(parti[2]);
                }

                    if(!docenti.contains(parti[3]))
                    {
                        docenti.add(parti[3]);
                    }

                if(parti[4].contains("Cognome"))
                {
                    if (!docenti.contains(parti[4])) {
                        docenti.add(parti[4]);
                    }
                    if(parti[5].contains("Cognome"))
                    {
                            if (!docenti.contains(parti[5])) {
                                docenti.add(parti[5]);
                            }

                            if (!classi.contains(parti[6])) {
                                classi.add(parti[6]);
                            }

                    }
                    else{

                            if (!classi.contains(parti[5])) {
                                classi.add(parti[5]);
                            }

                    }
                }
                else if(!classi.contains(parti[4]))
                        {
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

        for (String docente : docenti) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {
                docentimodificati.add(senzaVirgolette);
            }
        }
    return docentimodificati;
    }


    public ArrayList<String> getClassi () {
            return classi;
        }

    /*public static void main(String[] args) {
        Gestori.GestioneDati gestore = new Gestori.GestioneDati();
        System.out.println(gestore.toString());
    }*/
}
