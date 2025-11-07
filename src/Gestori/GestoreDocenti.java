package Gestori;

import Classi.Docente;
import Classi.Lezione;

import java.util.ArrayList;

public class GestoreDocenti {
    private ArrayList<Docente> docenti;
    private ArrayList<Lezione> tutteLezioni;

    public GestoreDocenti() {
        this.docenti = new ArrayList<>();
        this.tutteLezioni = new ArrayList<>();
    }

    public void creaDocentiDaLezioni() {
        ArrayList<String> nomiDocenti = new ArrayList<>();

        for (Lezione lezione : tutteLezioni) {
            for (String nomeDocente : lezione.getDocente()) {
                String nomePulito = nomeDocente.replace("\"", "").trim();
                if (!nomePulito.isEmpty() && !nomiDocenti.contains(nomePulito)) {
                    nomiDocenti.add(nomePulito);
                }
            }
        }

        for (String nomeDocente : nomiDocenti) {
            Docente docente = new Docente(nomeDocente, tutteLezioni);
            docenti.add(docente);
        }
    }

    public Docente trovaDocente(String nome) {
        for (Docente docente : docenti) {
            if (docente.getNome().trim().equals(nome.trim())) {
                return docente;
            }
        }
        return null;
    }

    public ArrayList<Docente> getDocenti() {
        return docenti;
    }

    public ArrayList<String> getNomiDocenti() {
        ArrayList<String> nomi = new ArrayList<>();
        for (Docente docente : docenti) {
            nomi.add(docente.getNome());
        }
        return nomi;
    }

    public ArrayList<Lezione> getTutteLezioni() {
        return tutteLezioni;
    }

    public void setTutteLezioni(ArrayList<Lezione> tutteLezioni) {
        this.tutteLezioni = tutteLezioni;
        docenti.clear();
        creaDocentiDaLezioni();
    }

}