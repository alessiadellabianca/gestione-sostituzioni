import java.util.ArrayList;

public class Docente {
    ArrayList<Lezione> lezioni;
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
                if(z.equals(nome))
                {

                }
            }
        }
    }

    public ArrayList<Lezione> getLezioniDiQuestoDoc() {
        return lezioniDiQuestoDoc;
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

