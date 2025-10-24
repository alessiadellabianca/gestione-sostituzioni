import java.util.ArrayList;

public class Docente {
    ArrayList<Lezione> lezioni;
    ArrayList<String> classiDiQuestoDoc=new ArrayList<>();
    ArrayList<String> MaterieDiQuestoDoc=new ArrayList<>();
    ArrayList<Lezione> lezioniDiQuestoDoc=new ArrayList<>();
    String nome;

    public Docente(ArrayList<Lezione> lezione,String docente) {
        this.lezioni = lezione;
        this.nome=docente;
        CreaDocente();
    }

    public void CreaDocente()
    {
        for(Lezione l:lezioni)
        {
            for(String z:l.getDocente())
            {
                if(z.trim().equals(nome.trim()))
                {
                    lezioniDiQuestoDoc.add(l);
                }
            }
        }
        CreaListe(); // Chiama CreaListe dopo aver popolato le lezioni
    }

    public void CreaListe()
    {
        for(Lezione l:lezioniDiQuestoDoc)
        {
            String materia = l.getMateria();
            if(materia != null && !MaterieDiQuestoDoc.contains(materia))
            {
                MaterieDiQuestoDoc.add(materia);
            }

            String classe = l.getClasse();
            if(!classiDiQuestoDoc.contains(classe))
            {
                classiDiQuestoDoc.add(classe);
            }
        }
    }

    public ArrayList<Lezione> getLezioniDiQuestoDoc() {
        return lezioniDiQuestoDoc;
    }

    public ArrayList<String> getClassiDiQuestoDoc() {
        return classiDiQuestoDoc;
    }

    public ArrayList<String> getMaterieDiQuestoDoc() {
        return MaterieDiQuestoDoc;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "lezioniDiQuestoDoc=" + lezioniDiQuestoDoc +
                '}';
    }
}

/*gestore.getdocenti la metto in un array list
passo il nome*/

