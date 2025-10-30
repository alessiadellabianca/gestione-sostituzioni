package Classi;

import java.util.ArrayList;

public class Docente {
    private ArrayList<Lezione> tutteLezioni;
    private ArrayList<String> classiDiQuestoDoc = new ArrayList<>();
    private ArrayList<String> materieDiQuestoDoc = new ArrayList<>();
    private ArrayList<Lezione> lezioniDiQuestoDoc = new ArrayList<>();
    private String nome;

    public Docente(String nome) {
        this.nome = nome;
        this.tutteLezioni = new ArrayList<>();
    }

    public Docente(String nome, ArrayList<Lezione> tutteLezioni) {
        this.nome = nome;
        this.tutteLezioni = tutteLezioni;
        CreaDocente();
    }

    public void CreaDocente()
    {
        for(Lezione l:tutteLezioni)
        {
            for(String z:l.getDocente())
            {
                if(z.trim().equals(nome.trim()))
                {
                    lezioniDiQuestoDoc.add(l);
                }
            }
        }
        CreaListe();
    }

    public void CreaListe()
    {
        for(Lezione l:lezioniDiQuestoDoc)
        {
            String materia = l.getMateria();
            if(materia != null && !materieDiQuestoDoc.contains(materia))
            {
                materieDiQuestoDoc.add(materia);
            }

            String classe = l.getClasse();
            if(!classiDiQuestoDoc.contains(classe))
            {
                classiDiQuestoDoc.add(classe);
            }
        }
    }

    public void aggiungiLezione(Lezione lezione) {
        if (lezione != null && !lezioniDiQuestoDoc.contains(lezione)) {
            lezioniDiQuestoDoc.add(lezione);
            aggiornaListe(lezione);
        }
    }

    private void aggiornaListe(Lezione lezione) {
        String materia = lezione.getMateria();
        if(materia != null && !materieDiQuestoDoc.contains(materia))
        {
            materieDiQuestoDoc.add(materia);
        }

        String classe = lezione.getClasse();
        if(!classiDiQuestoDoc.contains(classe))
        {
            classiDiQuestoDoc.add(classe);
        }
    }

    public void setTutteLezioni(ArrayList<Lezione> tutteLezioni) {
        this.tutteLezioni = tutteLezioni;
        lezioniDiQuestoDoc.clear();
        classiDiQuestoDoc.clear();
        materieDiQuestoDoc.clear();
        CreaDocente();
    }

    public ArrayList<Lezione> getLezioniDiQuestoDoc() {
        return lezioniDiQuestoDoc;
    }

    public ArrayList<String> getClassiDiQuestoDoc() {
        return classiDiQuestoDoc;
    }

    public ArrayList<String> getMaterieDiQuestoDoc() {
        return materieDiQuestoDoc;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Classi.Docente{" +
                "nome='" + nome + '\'' +
                ", lezioniDiQuestoDoc=" + lezioniDiQuestoDoc.size() +
                ", classiDiQuestoDoc=" + classiDiQuestoDoc +
                ", materieDiQuestoDoc=" + materieDiQuestoDoc +
                '}';
    }
}

