import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreSostituzioni extends JFrame {
    List<String> docentiAssenti;
    String giorno;
    JPanel panelloOrario = new JPanel();
    File letturaFile = new File("letturaFile.txt");
    GestoreDocenti gestoreDocenti;
    ArrayList<Lezione> lezioniDaSostituire;
    ArrayList<Lezione> lezioniSostituite = new ArrayList<>();
    ArrayList<Lezione> listaDisposizioni = new ArrayList<>();
    File lettFile = new File("letturaFile.txt");
    String[] oreStampa = {"08:00-9:00", "09:00-10:00", "10:00-11:10", "11:10-12:05", "12:05-13:00", "13:00-14:00"};
    ArrayList<Lezione> sostituzioniAssegnate = new ArrayList<>();

    public GestoreSostituzioni(List<String> docenti, GestoreDocenti gestoreDocenti, String giorno) {
        this.docentiAssenti = docenti;
        this.gestoreDocenti = gestoreDocenti;
        this.giorno = giorno;
        this.lezioniDaSostituire = new ArrayList<>();
        creaListaOreDisponibili();
        trovaLezioniDaSostituire();
        assegnaSostituzioni();
        creaTabellaSostituzioni();

        setTitle("Gestione Sostituzioni - " + giorno);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        add(panelloOrario);
        setVisible(true);
    }

    private void creaListaOreDisponibili() {
        try (BufferedReader br = new BufferedReader(new FileReader(lettFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parti = linea.split(";");
                if (parti.length < 8) continue;
                String durata = parti[1];
                String materia = parti[2];
                ArrayList<String> docente = new ArrayList<>();
                String[] docentiSplit = parti[3].split(";");
                for (String d : docentiSplit) {
                    String pulito = d.replace("\"", "").trim();
                    if (!pulito.isEmpty()) docente.add(pulito);
                }
                String classe;
                String giorno;
                String ora;
                boolean codocenza;
                if (parti[4].contains("Cognome")) {
                    if (parti.length > 5 && parti[5].contains("Cognome")) {
                        String[] altriDoc = {parti[4], parti[5]};
                        for (String d : altriDoc) {
                            String pulito = d.replace("\"", "").trim();
                            if (!pulito.isEmpty()) docente.add(pulito);
                        }
                        classe = parti[6];
                        codocenza = parti[7].contains("S");
                        giorno = parti[8];
                        ora = parti[9];
                    } else {
                        String pulito = parti[4].replace("\"", "").trim();
                        if (!pulito.isEmpty()) docente.add(pulito);
                        classe = parti[5];
                        codocenza = parti[6].contains("S");
                        giorno = parti[7];
                        ora = parti[8];
                    }
                } else {
                    classe = parti[4];
                    codocenza = parti[5].contains("S");
                    giorno = parti[6];
                    ora = parti[7];
                }
                if (materia.contains("Disposizione")) {
                    listaDisposizioni.add(new Lezione(docente, codocenza, classe, materia, durata, ora, giorno));
                }
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file: " + e.getMessage());
        }
    }

    private void trovaLezioniDaSostituire() {
        lezioniDaSostituire.clear();
        for (String docenteAssente : docentiAssenti) {
            Docente docente = gestoreDocenti.trovaDocente(docenteAssente);
            if (docente != null) {
                for (Lezione l : docente.getLezioniDiQuestoDoc()) {
                    if (l.getGiorno().equalsIgnoreCase(giorno)) {
                        lezioniDaSostituire.add(l);
                    }
                }
            }
        }
    }

    private void assegnaSostituzioni() {
        System.out.println("\n=========================================");
        System.out.println("INIZIO PROCESSO DI ASSEGNAZIONE SOSTITUZIONI");
        System.out.println("Giorno: " + giorno);
        System.out.println("Docenti assenti: " + docentiAssenti);
        System.out.println("Lezioni da sostituire: " + lezioniDaSostituire.size());
        System.out.println("=========================================");

        lezioniSostituite.clear();
        sostituzioniAssegnate.clear();

        for (int i = 0; i < lezioniDaSostituire.size(); i++) {
            Lezione lezioneDaSostituire = lezioniDaSostituire.get(i);
            System.out.println("\n--- LEZIONE " + (i + 1) + " di " + lezioniDaSostituire.size() + " ---");

            Lezione sostituzione = assegnaSostituzionePerLezione(lezioneDaSostituire);
            if (sostituzione != null) {
                lezioniSostituite.add(lezioneDaSostituire);
                sostituzioniAssegnate.add(sostituzione);
                System.out.println("✓ SOSTITUZIONE REGISTRATA CON SUCCESSO");
            } else {
                System.out.println("✗ IMPOSSIBILE TROVARE SOSTITUUTO");
            }
        }

        System.out.println("\n=========================================");
        System.out.println("RIEPILOGO SOSTITUZIONI");
        System.out.println("Lezioni da sostituire: " + lezioniDaSostituire.size());
        System.out.println("Sostituzioni assegnate: " + sostituzioniAssegnate.size());
        System.out.println("Lezioni non coperte: " + (lezioniDaSostituire.size() - sostituzioniAssegnate.size()));

        System.out.println("\nDETTAGLIO SOSTITUZIONI:");
        for (int i = 0; i < sostituzioniAssegnate.size(); i++) {
            Lezione originale = lezioniSostituite.get(i);
            Lezione sostituzione = sostituzioniAssegnate.get(i);
            System.out.println((i + 1) + ". " + originale.getMateria() + " (" + originale.getClasse() +
                    ") → " + sostituzione.getDocente() + " [" + sostituzione.getMateria() + "]");
        }
        System.out.println("=========================================");
    }

    private Lezione assegnaSostituzionePerLezione(Lezione lezioneDaSostituire) {
        System.out.println("\n=== ANALISI SOSTITUZIONE ===");
        System.out.println("Lezione da sostituire: " + lezioneDaSostituire.getMateria() +
                " - " + lezioneDaSostituire.getClasse() + " - " + lezioneDaSostituire.getOra());
        System.out.println("Docenti assenti: " + lezioneDaSostituire.getDocente());
        System.out.println("È compresenza: " + lezioneDaSostituire.isCodocenza());

        // 1. COMPRESOZA - Se il docente assente è in compresenza, sostituto = collega presente
        if (lezioneDaSostituire.isCodocenza()) {
            System.out.println("\n--- CONTROLLO 1: COMPRESOZA ---");
            ArrayList<String> sostituti = new ArrayList<>();
            for (String docente : lezioneDaSostituire.getDocente()) {
                if (!docentiAssenti.contains(docente)) {
                    sostituti.add(docente);
                    System.out.println("Trovato collega presente in compresenza: " + docente);
                }
            }
            if (!sostituti.isEmpty()) {
                System.out.println("✓ SOSTITUZIONE ASSEGNATA (Compresenza): " + sostituti);
                return new Lezione(sostituti, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Compresenza)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
            System.out.println("✗ Nessun collega presente in compresenza");
        }

        // 2. DISPONIBILITÀ DA DISPOSIZIONI - Cercare nei docenti liberi in quell'ora
        System.out.println("\n--- CONTROLLO 2: DISPONIBILITÀ DA DISPOSIZIONI ---");
        for (Lezione disposizione : listaDisposizioni) {
            if (disposizione.getOra().equals(lezioneDaSostituire.getOra()) &&
                    disposizione.getGiorno().equalsIgnoreCase(lezioneDaSostituire.getGiorno())) {
                System.out.println("Trovata disposizione per ora " + lezioneDaSostituire.getOra());
                ArrayList<String> sostitutiValidi = new ArrayList<>();
                for (String docente : disposizione.getDocente()) {
                    if (!docentiAssenti.contains(docente)) {
                        sostitutiValidi.add(docente);
                        System.out.println("  Docente disponibile da disposizioni: " + docente);
                    }
                }
                if (!sostitutiValidi.isEmpty()) {
                    System.out.println("✓ SOSTITUZIONE ASSEGNATA (Disposizione): " + sostitutiValidi);
                    return new Lezione(sostitutiValidi, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Disposizione)",
                            lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
                }
            }
        }
        System.out.println("✗ Nessuna disposizione disponibile");

        // 3. DOCENTI LIBERI - Priorità: stessa classe -> materia affine -> qualsiasi altro
        System.out.println("\n--- CONTROLLO 3: DOCENTI LIBERI ---");

        // 3a. Docente della stessa classe
        System.out.println("3a. Ricerca docenti della stessa classe: " + lezioneDaSostituire.getClasse());
        ArrayList<Docente> docentiClasse = gestoreDocenti.trovaDocentiPerClasse(lezioneDaSostituire.getClasse());
        for (Docente docente : docentiClasse) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                System.out.println("✓ SOSTITUZIONE ASSEGNATA (Stessa classe): " + docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Stessa classe)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }
        System.out.println("✗ Nessun docente disponibile della stessa classe");

        // 3b. Docente di materia affine
        System.out.println("3b. Ricerca docenti di materia affine: " + lezioneDaSostituire.getMateria());
        ArrayList<Docente> docentiMateriaAffine = gestoreDocenti.trovaDocentiPerMateria(lezioneDaSostituire.getMateria());
        for (Docente docente : docentiMateriaAffine) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                System.out.println("✓ SOSTITUZIONE ASSEGNATA (Materia affine): " + docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Materia affine)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }
        System.out.println("✗ Nessun docente disponibile di materia affine");

        // 3c. Qualsiasi altro docente libero
        System.out.println("3c. Ricerca qualsiasi altro docente libero");
        for (Docente docente : gestoreDocenti.getDocenti()) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                System.out.println("✓ SOSTITUZIONE ASSEGNATA (Altro docente libero): " + docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Altro libero)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }
        System.out.println("✗ Nessun altro docente libero disponibile");

        // 4. GESTIONE COMPRESOZE NON QUINTE
        if (lezioneDaSostituire.isCodocenza() && !lezioneDaSostituire.getClasse().contains("5")) {
            System.out.println("\n--- CONTROLLO 4: COMPRESOZE NON QUINTE ---");

            // Prima docenti della classe o affini
            System.out.println("4a. Compresenze non quinte - docenti classe/affini");
            for (Docente docente : docentiClasse) {
                if (!docentiAssenti.contains(docente.getNome()) &&
                        isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                    ArrayList<String> sostituto = new ArrayList<>();
                    sostituto.add(docente.getNome());
                    System.out.println("✓ SOSTITUZIONE ASSEGNATA (Compresenza non quinta - classe): " + docente.getNome());
                    return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Comp. non quinta - classe)",
                            lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
                }
            }

            for (Docente docente : docentiMateriaAffine) {
                if (!docentiAssenti.contains(docente.getNome()) &&
                        isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                    ArrayList<String> sostituto = new ArrayList<>();
                    sostituto.add(docente.getNome());
                    System.out.println("✓ SOSTITUZIONE ASSEGNATA (Compresenza non quinta - affine): " + docente.getNome());
                    return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Comp. non quinta - affine)",
                            lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
                }
            }

            // Poi altri docenti non delle quinte
            System.out.println("4b. Compresenze non quinte - altri docenti non quinte");
            for (Docente docente : gestoreDocenti.getDocenti()) {
                if (!docentiAssenti.contains(docente.getNome()) &&
                        isDocenteDisponibile(docente, lezioneDaSostituire.getOra()) &&
                        !insegnaInQuinte(docente)) {
                    ArrayList<String> sostituto = new ArrayList<>();
                    sostituto.add(docente.getNome());
                    System.out.println("✓ SOSTITUZIONE ASSEGNATA (Compresenza non quinta - altro): " + docente.getNome());
                    return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Comp. non quinta - altro)",
                            lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
                }
            }
        }

        // 5. GESTIONE COMPRESOZE QUINTE
        if (lezioneDaSostituire.isCodocenza() && lezioneDaSostituire.getClasse().contains("5")) {
            System.out.println("\n--- CONTROLLO 5: COMPRESOZE QUINTE ---");

            // Prima materia affine
            System.out.println("5a. Compresenze quinte - materia affine");
            for (Docente docente : docentiMateriaAffine) {
                if (!docentiAssenti.contains(docente.getNome()) &&
                        isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                    ArrayList<String> sostituto = new ArrayList<>();
                    sostituto.add(docente.getNome());
                    System.out.println("✓ SOSTITUZIONE ASSEGNATA (Compresenza quinta - affine): " + docente.getNome());
                    return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Comp. quinta - affine)",
                            lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
                }
            }

            // Poi qualsiasi materia
            System.out.println("5b. Compresenze quinte - qualsiasi materia");
            for (Docente docente : gestoreDocenti.getDocenti()) {
                if (!docentiAssenti.contains(docente.getNome()) &&
                        isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                    ArrayList<String> sostituto = new ArrayList<>();
                    sostituto.add(docente.getNome());
                    System.out.println("✓ SOSTITUZIONE ASSEGNATA (Compresenza quinta - qualsiasi): " + docente.getNome());
                    return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Comp. quinta - qualsiasi)",
                            lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
                }
            }
        }

        // 6. ORE LIBERE A PAGAMENTO - Se ancora irrisolto, cercare docenti liberi con priorità a chi ha lezione adiacente
        System.out.println("\n--- CONTROLLO 6: ORE LIBERE A PAGAMENTO ---");
        System.out.println("6a. Ricerca docenti liberi con lezione adiacente");
        for (Docente docente : gestoreDocenti.getDocenti()) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra()) &&
                    haLezioneAdiacente(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                System.out.println("✓ SOSTITUZIONE ASSEGNATA (Pagamento con adiacente): " + docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Pagamento adiacente)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }

        System.out.println("6b. Ricerca qualsiasi docente libero per pagamento");
        for (Docente docente : gestoreDocenti.getDocenti()) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                System.out.println("✓ SOSTITUZIONE ASSEGNATA (Pagamento generico): " + docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Pagamento)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }

        System.out.println("✗ NESSUN SOSTITUTO TROVATO PER LA LEZIONE");
        return null;
    }

    private boolean isDocenteDisponibile(Docente docente, String ora) {
        for (Lezione lezione : docente.getLezioniDiQuestoDoc()) {
            if (lezione.getGiorno().equals(giorno) && lezione.getOra().equals(ora)) {
                return false;
            }
        }
        return true;
    }

    private boolean haLezioneAdiacente(Docente docente, String ora) {
        String[] ore = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00"};
        int oraCorrenteIndex = -1;
        for (int i = 0; i < ore.length; i++) {
            if (ore[i].equals(ora)) {
                oraCorrenteIndex = i;
                break;
            }
        }
        if (oraCorrenteIndex == -1) return false;
        String oraPrecedente = oraCorrenteIndex > 0 ? ore[oraCorrenteIndex - 1] : null;
        String oraSuccessiva = oraCorrenteIndex < ore.length - 1 ? ore[oraCorrenteIndex + 1] : null;
        for (Lezione lezione : docente.getLezioniDiQuestoDoc()) {
            if (lezione.getGiorno().equals(giorno)) {
                if ((oraPrecedente != null && lezione.getOra().equals(oraPrecedente)) ||
                        (oraSuccessiva != null && lezione.getOra().equals(oraSuccessiva))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean insegnaInQuinte(Docente docente) {
        for (String classe : docente.getClassiDiQuestoDoc()) {
            if (classe.contains("5")) {
                return true;
            }
        }
        return false;
    }

    public void creaTabellaSostituzioni() {
        panelloOrario.removeAll();
        panelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 0;
        JLabel vuoto = new JLabel("", SwingConstants.CENTER);
        vuoto.setOpaque(true);
        vuoto.setBackground(new Color(220, 220, 220));
        vuoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelloOrario.add(vuoto, c);

        for (int i = 0; i < docentiAssenti.size(); i++) {
            c.gridx = i + 1;
            c.gridy = 0;
            JLabel labelDocente = new JLabel(docentiAssenti.get(i), SwingConstants.CENTER);
            labelDocente.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelDocente.setOpaque(true);
            labelDocente.setBackground(new Color(180, 180, 180));
            labelDocente.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelloOrario.add(labelDocente, c);
        }

        for (int i = 0; i < oreStampa.length; i++) {
            c.gridx = 0;
            c.gridy = i + 1;
            JLabel labelOra = new JLabel(oreStampa[i], SwingConstants.CENTER);
            labelOra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            labelOra.setOpaque(true);
            labelOra.setBackground(new Color(200, 200, 200));
            labelOra.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelloOrario.add(labelOra, c);
        }

        for (int col = 0; col < docentiAssenti.size(); col++) {
            String docenteCorrente = docentiAssenti.get(col);
            for (int riga = 0; riga < oreStampa.length; riga++) {
                String oraCorrente = oreStampa[riga].split("-")[0];
                boolean lezioneTrovata = false;
                for (int i = 0; i < lezioniDaSostituire.size(); i++) {
                    Lezione lezioneOriginale = lezioniDaSostituire.get(i);
                    boolean docenteTrovato = false;
                    for (String docenteLezione : lezioneOriginale.getDocente()) {
                        if (docenteLezione.trim().equals(docenteCorrente.trim())) {
                            docenteTrovato = true;
                            break;
                        }
                    }

                    if (docenteTrovato && lezioneOriginale.getOra().replace("h",":").equals(oraCorrente)) {
                        int altezza = calcolaDurataBlocchi(lezioneOriginale);
                        c.gridx = col + 1;
                        c.gridy = riga + 1;
                        c.gridheight = altezza;
                        JPanel pannelloCompleto = creaPannelloSostituzione(lezioneOriginale);
                        panelloOrario.add(pannelloCompleto, c);
                        lezioneTrovata = true;
                        if (altezza > 1) riga += (altezza - 1);
                        break;
                    }
                }
                if (!lezioneTrovata) {
                    c.gridx = col + 1;
                    c.gridy = riga + 1;
                    c.gridheight = 1;
                    JPanel vuotoPanel = new JPanel();
                    vuotoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                    vuotoPanel.setBackground(Color.WHITE);
                    panelloOrario.add(vuotoPanel, c);
                }
            }
        }

        for (Component comp : panelloOrario.getComponents()) {
            if (comp instanceof JPanel || comp instanceof JLabel) {
                comp.setPreferredSize(new Dimension(150, 80));
            }
        }

        panelloOrario.setPreferredSize(new Dimension(1000, 500));
        revalidate();
        repaint();
    }

    private int calcolaDurataBlocchi(Lezione lezione) {
        try {
            String durata = lezione.getDurata().toLowerCase().trim();
            if (durata.contains("h")) {
                return Integer.parseInt(durata.split("h")[0].trim());
            } else if (durata.contains("ore")) {
                return Integer.parseInt(durata.split("ore")[0].trim());
            } else {
                return Integer.parseInt(durata);
            }
        } catch (Exception e) {
            return 1;
        }
    }
    int j=0;

    private JPanel creaPannelloSostituzione(Lezione lezioneOriginale) {

        JPanel panelSostituto = new JPanel(new BorderLayout());
        if(lezioneOriginale.getOra().replace("h",":").equals(sostituzioniAssegnate.get(j).getOra().replace("h",":")))
        {
            System.out.println("Assegnato sostituto: " + sostituzioniAssegnate.get(j).getDocente().getFirst() + " per lezione " + lezioneOriginale.getMateria() + " - " + lezioneOriginale.getClasse() + " - " + lezioneOriginale.getOra());
            JLabel docente = new JLabel(sostituzioniAssegnate.get(j).getDocente().getFirst(), SwingConstants.CENTER);
            panelSostituto.add(docente, BorderLayout.CENTER);
            panelSostituto.setBackground(new Color(200, 255, 200));
            j++;
            panelSostituto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelSostituto.setPreferredSize(new Dimension(120, 60));
            return panelSostituto;
        }
        else
        {
            panelSostituto.setBackground(new Color(255, 200, 200));
            return panelSostituto;
        }
    }
}