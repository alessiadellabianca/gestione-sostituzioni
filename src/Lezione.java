import java.util.ArrayList;

public class Lezione {

    ArrayList<String> docente=new ArrayList();
    String materia;
    String ora;
    String durata;
    String classe;
    boolean codocenza;
    String giorno;

    public Lezione(ArrayList<String> docente, boolean codocenza, String classe, String materia, String durata, String ora, String giorno) {
        this.docente = docente;
        this.codocenza = codocenza;
        this.classe = classe;
        this.materia = materia;
        this.durata = durata;
        this.ora = ora;
        this.giorno = giorno;
    }

    public boolean isCodocenza() {
        return codocenza;
    }

    public String getClasse() {
        return classe;
    }

    public ArrayList<String> getDocente() {
        return docente;
    }

    public String getMateria() {
        return materia;
    }

    public String getOra() {
        return ora;
    }

    public String getDurata() {
        return durata;
    }


    public String getGiorno() {
        return giorno;
    }

    @Override
    public String toString() {
        return "Lezione{" +
                "docente='" + docente + '\'' +
                ", materia='" + materia + '\'' +
                ", ora='" + ora + '\'' +
                ", durata='" + durata + '\'' +
                ", classe='" + classe + '\'' +
                ", codocenza=" + codocenza +
                ", giorno='" + giorno + '\'' +
                '}';
    }
}
