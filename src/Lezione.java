public class Lezione {

    String docente;
    String materia;
    String ora;
    String durata;
    String classe;
    boolean codocenza;

    public Lezione(String docente, boolean codocenza, String classe, String materia, String durata, String ora) {
        this.docente = docente;
        this.codocenza = codocenza;
        this.classe = classe;
        this.materia = materia;
        this.durata = durata;
        this.ora = ora;
    }


}
