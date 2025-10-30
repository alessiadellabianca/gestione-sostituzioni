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

    public GestoreDocenti(ArrayList<Lezione> tutteLezioni) {
        this.tutteLezioni = tutteLezioni;
        this.docenti = new ArrayList<>();
    }

    public void aggiungiLezione(Lezione lezione) {
        if (lezione != null && !tutteLezioni.contains(lezione)) {
            tutteLezioni.add(lezione);
            aggiornaDocentiConNuovaLezione(lezione);
        }
    }
    private void aggiornaDocentiConNuovaLezione(Lezione lezione) {
        for (String nomeDocente : lezione.getDocente()) {
            Docente docente = trovaDocente(nomeDocente);
            if (docente != null) {
                docente.aggiungiLezione(lezione);
            } else {
                Docente nuovoDocente = new Docente(nomeDocente);
                nuovoDocente.aggiungiLezione(lezione);
                docenti.add(nuovoDocente);
            }
        }
    }

    public void creaDocentiDaLezioni() {
        ArrayList<String> nomiDocenti = new ArrayList<>();

        // Raccoglie tutti i nomi dei docenti
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

    public void setTutteLezioni(ArrayList<Lezione> tutteLezioni) {
        this.tutteLezioni = tutteLezioni;
        docenti.clear();
        creaDocentiDaLezioni();
    }

    public ArrayList<Lezione> getTutteLezioni() {
        return tutteLezioni;
    }


    public ArrayList<Docente> trovaDocentiPerClasse(String classe) {
        ArrayList<Docente> docentiPerClasse = new ArrayList<>();

        for (Docente docente : docenti) {
            if (docente.getClassiDiQuestoDoc().contains(classe)) {
                docentiPerClasse.add(docente);
            }
        }

        return docentiPerClasse;
    }

    public ArrayList<Docente> trovaDocentiPerMateria(String materia) {
        ArrayList<Docente> docentiPerMateria = new ArrayList<>();

        for (Docente docente : docenti) {
            if (docente.getMaterieDiQuestoDoc().contains(materia)) {
                docentiPerMateria.add(docente);
            }
        }

        return docentiPerMateria;
    }

    public ArrayList<Docente> trovaNomiDocentiPerClasse(String classe) {
        ArrayList<Docente> nomiDocenti = new ArrayList<>();
        ArrayList<Docente> docentiPerClasse = trovaDocentiPerClasse(classe);

        for (Docente docente : docentiPerClasse) {
            nomiDocenti.add(new Docente(docente.getNome()));
        }
        return nomiDocenti;
    }

    public ArrayList<String> trovaNomiDocentiPerMateria(String materia) {
        ArrayList<String> nomiDocenti = new ArrayList<>();
        ArrayList<Docente> docentiPerMateria = trovaDocentiPerMateria(materia);

        for (Docente docente : docentiPerMateria) {
            nomiDocenti.add(docente.getNome());
        }

        return nomiDocenti;
    }

    public ArrayList<Lezione> trovaLezioniPerClasse(String classe) {
        ArrayList<Lezione> lezioniPerClasse = new ArrayList<>();

        for (Lezione lezione : tutteLezioni) {
            if (lezione.getClasse().equalsIgnoreCase(classe)) {
                lezioniPerClasse.add(lezione);
            }
        }

        return lezioniPerClasse;
    }


    public ArrayList<Lezione> trovaLezioniPerMateria(String materia) {
        ArrayList<Lezione> lezioniPerMateria = new ArrayList<>();

        for (Lezione lezione : tutteLezioni) {
            if (lezione.getMateria().equalsIgnoreCase(materia)) {
                lezioniPerMateria.add(lezione);
            }
        }

        return lezioniPerMateria;
    }

    public ArrayList<Docente> trovaDocentiDisponibiliPerSostituzione(String classe, String materia, ArrayList<String> docentiEsclusi) {
        ArrayList<Docente> docentiDisponibili = new ArrayList<>();

        for (Docente docente : docenti) {

            if (docentiEsclusi.contains(docente.getNome())) {
                continue;
            }

            if (docente.getMaterieDiQuestoDoc().contains(materia)) {

                if (docente.getClassiDiQuestoDoc().contains(classe)) {
                    docentiDisponibili.add(docente);
                }
            }
        }

        return docentiDisponibili;
    }

}