import Gestori.GestioneDati;
import Interfaccia.HomePage;
//gestore
public class Main {
    public static void main(String[] args) {
        GestioneDati gestore = new GestioneDati();
        gestore.organizzazioneFile("letturaFile.txt");

    new HomePage(gestore);
    }
}