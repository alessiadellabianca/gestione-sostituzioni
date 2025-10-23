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
        String doc=nome+" ";
        String doc1=" "+nome;
        for(Lezione l: lezioni)
        {
          if(l.getDocente().equals(nome))
          {
              lezioniDiQuestoDoc.add(l);
          }
          else if(l.getDocente().contains(doc))
          {
              lezioniDiQuestoDoc.add(l);
          }
          else if(l.getDocente().contains(doc1))
          {
              lezioniDiQuestoDoc.add(l);
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

