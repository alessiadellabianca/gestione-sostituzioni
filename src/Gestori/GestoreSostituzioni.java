package Gestori;
import Classi.Docente;
import Classi.Lezione;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
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

    String[] oreStampa = {
            "08:00-9:00", "09:00-10:00", "10:00-11:10",
            "11:10-12:05", "12:05-13:00", "13:00-14:00"
    };

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
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Layout principale con pulsanti
        setLayout(new BorderLayout());

        // Pannello per i pulsanti
        JPanel pannelloPulsanti = new JPanel(new FlowLayout());
        JButton pulsanteStampa = new JButton("Stampa");
        JButton pulsantePDF = new JButton("Esporta PDF");

        pulsantePDF.addActionListener(e -> esportaPDF());

        pannelloPulsanti.add(pulsanteStampa);
        pannelloPulsanti.add(pulsantePDF);

        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.add(panelloOrario, BorderLayout.CENTER);
        pannelloPrincipale.add(creaLeggenda(), BorderLayout.SOUTH);

        add(pannelloPulsanti, BorderLayout.NORTH);
        add(pannelloPrincipale, BorderLayout.CENTER);

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

                String pulito1 = parti[3].replace("\"", "").trim();
                if (!pulito1.isEmpty()) docente.add(pulito1);

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
        System.out.println("INIZIO PROCESSO DI ASSEGNAZIONE SOSTITUZIONI");
        System.out.println("Giorno: " + giorno);
        System.out.println("Docenti assenti: " + docentiAssenti);
        System.out.println("Lezioni da sostituire: " + lezioniDaSostituire.size());

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
                System.out.println("✗ IMPOSSIBILE TROVARE SOSTITUTO");
            }
        }
        //zona di controllo
        System.out.println("RIEPILOGO SOSTITUZIONI");
        System.out.println("Lezioni da sostituire: " + lezioniDaSostituire.size());
        System.out.println("Sostituzioni assegnate: " + sostituzioniAssegnate.size());
        System.out.println("Lezioni non coperte: " + (lezioniDaSostituire.size() - sostituzioniAssegnate.size()));
        System.out.println("\nDETTAGLIO SOSTITUZIONI:");
        for (int i = 0; i < sostituzioniAssegnate.size(); i++) {
            Lezione originale = lezioniSostituite.get(i);
            Lezione sostituzione = sostituzioniAssegnate.get(i);
            System.out.println((i + 1) + ". " + originale.getMateria() + " (" +
                    originale.getClasse() + ") → " +
                    sostituzione.getDocente() + " [" + sostituzione.getMateria() + "]");
        }

    }

    private Lezione assegnaSostituzionePerLezione(Lezione lezioneDaSostituire) {
        System.out.println("\n=== ANALISI SOSTITUZIONE ===");
        System.out.println("Lezione da sostituire: " + lezioneDaSostituire.getMateria() + " - " +
                lezioneDaSostituire.getClasse() + " - " + lezioneDaSostituire.getOra());
        System.out.println("Docenti assenti: " + lezioneDaSostituire.getDocente());
        System.out.println("È compresenza: " + lezioneDaSostituire.isCodocenza());

        if (lezioneDaSostituire.isCodocenza()) {
            ArrayList<String> sostituti = new ArrayList<>();
            for (String docente : lezioneDaSostituire.getDocente()) {
                if (!docentiAssenti.contains(docente)) {
                    sostituti.add(docente);
                }
            }
            if (!sostituti.isEmpty()) {
                return new Lezione(sostituti, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Compresenza)",
                        lezioneDaSostituire.getDurata(),
                        lezioneDaSostituire.getOra(),
                        lezioneDaSostituire.getGiorno());
            }
        }

        for (Lezione disposizione : listaDisposizioni) {
            if (disposizione.getOra().equals(lezioneDaSostituire.getOra()) &&
                    disposizione.getGiorno().equalsIgnoreCase(lezioneDaSostituire.getGiorno())) {

                ArrayList<String> sostitutiValidi = new ArrayList<>();
                for (String docente : disposizione.getDocente()) {
                    if (!docentiAssenti.contains(docente)) {
                        sostitutiValidi.add(docente);
                    }
                }
                if (!sostitutiValidi.isEmpty()) {
                    boolean giaAssegnato = false;
                    for (Lezione sostituzioneEsistente : sostituzioniAssegnate) {
                        if (sostituzioneEsistente.getOra().equals(lezioneDaSostituire.getOra()) &&
                            sostituzioneEsistente.getGiorno().equalsIgnoreCase(lezioneDaSostituire.getGiorno())) {
                            for (String sostitutoValido : sostitutiValidi) {
                                if (sostituzioneEsistente.getDocente().contains(sostitutoValido)) {
                                    giaAssegnato = true;
                                    break;
                                }
                            }
                        }
                        if (giaAssegnato)
                        {
                            break;
                        }
                    }
                    if (!giaAssegnato) {
                        return new Lezione(sostitutiValidi, false, lezioneDaSostituire.getClasse(),
                                lezioneDaSostituire.getMateria() + " (Disposizione)",
                                lezioneDaSostituire.getDurata(),
                                lezioneDaSostituire.getOra(),
                                lezioneDaSostituire.getGiorno());
                    }
                }
            }
        }

        for (Lezione altraCompresenza : gestoreDocenti.getTutteLezioni()) {
            if (altraCompresenza.isCodocenza() && !(altraCompresenza.getOra().equals(lezioneDaSostituire.getOra()) &&
                      altraCompresenza.getGiorno().equalsIgnoreCase(lezioneDaSostituire.getGiorno()) &&
                      altraCompresenza.getClasse().equals(lezioneDaSostituire.getClasse()))) {

                ArrayList<String> docentiCompresenzaDisponibili = new ArrayList<>();
                for (String docente : altraCompresenza.getDocente()) {
                    if (!docentiAssenti.contains(docente)) {
                        docentiCompresenzaDisponibili.add(docente);
                    }
                }

                if (!docentiCompresenzaDisponibili.isEmpty()) {
                    boolean giaAssegnato = false;
                    for (Lezione sostituzioneEsistente : sostituzioniAssegnate) {
                        if (sostituzioneEsistente.getOra().equals(lezioneDaSostituire.getOra()) &&
                            sostituzioneEsistente.getGiorno().equalsIgnoreCase(lezioneDaSostituire.getGiorno())) {
                            for (String docenteCompresenza : docentiCompresenzaDisponibili) {
                                if (sostituzioneEsistente.getDocente().contains(docenteCompresenza)) {
                                    giaAssegnato = true;
                                    break;
                                }
                            }
                        }
                        if (giaAssegnato) {
                            break;
                        }
                    }

                    if (!giaAssegnato) {
                        ArrayList<String> docenteSingolo = new ArrayList<>();
                        docenteSingolo.add(docentiCompresenzaDisponibili.get(0));

                        return new Lezione(docenteSingolo, false, lezioneDaSostituire.getClasse(),
                                lezioneDaSostituire.getMateria() + " (Da compresenza divisa)",
                                lezioneDaSostituire.getDurata(),
                                lezioneDaSostituire.getOra(),
                                lezioneDaSostituire.getGiorno());
                    }
                }
            }
        }

        for (Docente docente : gestoreDocenti.getDocenti()) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno())) {

                boolean giaAssegnato = false;
                for (Lezione sostituzioneEsistente : sostituzioniAssegnate) {
                    if (sostituzioneEsistente.getOra().equals(lezioneDaSostituire.getOra()) &&
                        sostituzioneEsistente.getGiorno().equalsIgnoreCase(lezioneDaSostituire.getGiorno()) &&
                        sostituzioneEsistente.getDocente().contains(docente.getNome())) {
                        giaAssegnato = true;

                        break;
                    }
                }

                if (!giaAssegnato) {
                    ArrayList<String> sostituto = new ArrayList<>();
                    sostituto.add(docente.getNome());
                    return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Altro libero)",
                            lezioneDaSostituire.getDurata(),
                            lezioneDaSostituire.getOra(),
                            lezioneDaSostituire.getGiorno());
                }
            }
        }

        return null;
    }

    private boolean isDocenteDisponibile(Docente docente, String ora, String giorno) {
        for (Lezione lezione : docente.getLezioniDiQuestoDoc()) {
            if (lezione.getGiorno().equalsIgnoreCase(giorno) && lezione.getOra().replace("h", ":").equals(ora.replace("h", ":"))) {
                return false;
            }
        }
        return true;
    }

    public void creaTabellaSostituzioni() {
        panelloOrario.removeAll();
        panelloOrario.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;


        JLabel vuoto = new JLabel("", SwingConstants.CENTER);
        vuoto.setOpaque(true);
        vuoto.setBackground(new Color(189, 195, 199));
        vuoto.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
        vuoto.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 0;
        panelloOrario.add(vuoto, c);

        for (int i = 0; i < docentiAssenti.size(); i++) {
            c.gridx = i + 1;
            c.gridy = 0;
            JLabel labelDocente = new JLabel(docentiAssenti.get(i), SwingConstants.CENTER);
            labelDocente.setFont(new Font("Segoe UI", Font.BOLD, 12));
            labelDocente.setOpaque(true);
            labelDocente.setBackground(new Color(174, 214, 241));
            labelDocente.setForeground(Color.BLACK);
            labelDocente.setBorder(BorderFactory.createLineBorder(new Color(133, 193, 233)));
            panelloOrario.add(labelDocente, c);
        }

        for (int i = 0; i < oreStampa.length; i++) {
            c.gridx = 0;
            c.gridy = i + 1;
            JLabel labelOra = new JLabel(oreStampa[i], SwingConstants.CENTER);
            labelOra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            labelOra.setOpaque(true);
            labelOra.setBackground(new Color(212, 224, 226));
            labelOra.setForeground(Color.BLACK);
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

                    if (docenteTrovato && lezioneOriginale.getOra().replace("h", ":").equals(oraCorrente)) {
                        // no mostrare le lezioni di materia "Disposizione"
                        if (lezioneOriginale.getMateria().equalsIgnoreCase("Disposizione")) {
                            lezioneTrovata = true;
                            break;
                        }

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
                    vuotoPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
                    vuotoPanel.setBackground(new Color(236, 240, 241));
                    panelloOrario.add(vuotoPanel, c);
                }
            }
        }

        panelloOrario.setPreferredSize(new Dimension(1000, 500));
        revalidate();
        repaint();
    }

    private JPanel creaLeggenda() {
        JPanel leggenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leggenda.setBorder(BorderFactory.createTitledBorder("Legenda"));
        leggenda.setBackground(new Color(248, 249, 250));

        JPanel panelCompresenza = new JPanel(new FlowLayout());
        JPanel coloreCompresenza = new JPanel();
        coloreCompresenza.setBackground(new Color(255, 182, 193));
        coloreCompresenza.setPreferredSize(new Dimension(20, 20));
        coloreCompresenza.setBorder(BorderFactory.createLineBorder(new Color(255, 160, 180)));
        panelCompresenza.add(coloreCompresenza);
        panelCompresenza.add(new JLabel("Compresenza (stessa lezione)"));
        leggenda.add(panelCompresenza);

        JPanel panelDisposizione = new JPanel(new FlowLayout());
        JPanel coloreDisposizione = new JPanel();
        coloreDisposizione.setBackground(new Color(173, 216, 230));
        coloreDisposizione.setPreferredSize(new Dimension(20, 20));
        coloreDisposizione.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 235)));
        panelDisposizione.add(coloreDisposizione);
        panelDisposizione.add(new JLabel("Sostituzione da Disposizione"));
        leggenda.add(panelDisposizione);

        JPanel panelCompresenzaDivisa = new JPanel(new FlowLayout());
        JPanel coloreCompresenzaDivisa = new JPanel();
        coloreCompresenzaDivisa.setBackground(new Color(255, 218, 185));
        coloreCompresenzaDivisa.setPreferredSize(new Dimension(20, 20));
        coloreCompresenzaDivisa.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193)));
        panelCompresenzaDivisa.add(coloreCompresenzaDivisa);
        panelCompresenzaDivisa.add(new JLabel("Sostituzione da Compresenza divisa"));
        leggenda.add(panelCompresenzaDivisa);

        JPanel panelAltroLibero = new JPanel(new FlowLayout());
        JPanel coloreAltroLibero = new JPanel();
        coloreAltroLibero.setBackground(new Color(144, 238, 144));
        coloreAltroLibero.setPreferredSize(new Dimension(20, 20));
        coloreAltroLibero.setBorder(BorderFactory.createLineBorder(new Color(124, 218, 124)));
        panelAltroLibero.add(coloreAltroLibero);
        panelAltroLibero.add(new JLabel("Sostituzione da Altro docente libero"));
        leggenda.add(panelAltroLibero);

        JPanel panelNonCoperto = new JPanel(new FlowLayout());
        JPanel coloreNonCoperto = new JPanel();
        coloreNonCoperto.setBackground(new Color(200, 50, 50));
        coloreNonCoperto.setPreferredSize(new Dimension(20, 20));
        coloreNonCoperto.setBorder(BorderFactory.createLineBorder(new Color(255, 208, 161)));
        panelNonCoperto.add(coloreNonCoperto);
        panelNonCoperto.add(new JLabel("Non coperto"));
        leggenda.add(panelNonCoperto);
        return leggenda;
    }




    private void esportaPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva PDF");
        fileChooser.setSelectedFile(new File("sostituzioni_" + giorno + ".pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Creazione immagine della tabella
                BufferedImage image = new BufferedImage(panelloOrario.getWidth(), panelloOrario.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image.createGraphics();
                panelloOrario.printAll(g2d);
                g2d.dispose();

                // Salvataggio come immagine (soluzione semplice)
                String fileName = file.getAbsolutePath();
                if (!fileName.toLowerCase().endsWith(".png")) {
                    fileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".png";
                }

                javax.imageio.ImageIO.write(image, "PNG", new File(fileName));

                JOptionPane.showMessageDialog(this, "Tabella salvata come immagine in: " + fileName, "Esportazione completata", JOptionPane.INFORMATION_MESSAGE);

                // Tentativo di apertura automatica del file
                try {
                    Desktop.getDesktop().open(new File(fileName));
                } catch (Exception ex) {
                    // Ignora errore di apertura
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore durante l'esportazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    private JPanel creaPannelloSostituzione(Lezione lezioneOriginale) {
        JPanel panelSostituto = new JPanel(new BorderLayout());

        Lezione sostituzione = null;
        for (int i = 0; i < sostituzioniAssegnate.size(); i++) {
            Lezione s = sostituzioniAssegnate.get(i);
            if (lezioneOriginale.getOra().replace("h", ":").equals(s.getOra().replace("h", ":")) &&
                lezioneOriginale.getGiorno().equalsIgnoreCase(s.getGiorno()) &&
                lezioneOriginale.getClasse().equals(s.getClasse())) {
                sostituzione = s;
                break;
            }
        }

        if (sostituzione != null) {
            JLabel docente = new JLabel(String.join(", ", sostituzione.getDocente()), SwingConstants.CENTER);

            String tipoSostituzione = sostituzione.getMateria();
            Color coloreSfondo;
            Color coloreBordo;

            if (lezioneOriginale.isCodocenza() && tipoSostituzione.contains("(Compresenza)")) {
                coloreSfondo = new Color(255, 182, 193);
                coloreBordo = new Color(255, 160, 180);
            } else if (tipoSostituzione.contains("(Disposizione)")) {
                coloreSfondo = new Color(173, 216, 230);
                coloreBordo = new Color(135, 206, 235);
            } else if (tipoSostituzione.contains("(Da compresenza divisa)")) {
                coloreSfondo = new Color(255, 218, 185);
                coloreBordo = new Color(255, 182, 193);
            } else if (tipoSostituzione.contains("(Altro libero)")) {
                coloreSfondo = new Color(144, 238, 144);
                coloreBordo = new Color(124, 218, 124);
            } else {
                coloreSfondo = new Color(200, 50, 50);
                coloreBordo = new Color(200, 50, 50);
            }

            panelSostituto.add(docente, BorderLayout.CENTER);
            panelSostituto.setBackground(coloreSfondo);
            panelSostituto.setForeground(Color.BLACK);
            panelSostituto.setBorder(BorderFactory.createLineBorder(coloreBordo));
            panelSostituto.setPreferredSize(new Dimension(120, 60));

            return panelSostituto;
        } else {
            panelSostituto.setBackground(new Color(255, 228, 181));
            panelSostituto.setForeground(Color.BLACK);
            panelSostituto.setBorder(BorderFactory.createLineBorder(new Color(255, 208, 161)));
            JLabel nonCoperto = new JLabel("NON COPERTO", SwingConstants.CENTER);
            panelSostituto.add(nonCoperto, BorderLayout.CENTER);
            return panelSostituto;
        }
    }
}
