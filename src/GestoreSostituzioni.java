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
    String[] oreStampa = {"8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00"};
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
        lezioniSostituite.clear();
        sostituzioniAssegnate.clear();
        for (Lezione lezioneDaSostituire : lezioniDaSostituire) {
            Lezione sostituzione = assegnaSostituzionePerLezione(lezioneDaSostituire);
            if (sostituzione != null) {
                lezioniSostituite.add(lezioneDaSostituire);
                sostituzioniAssegnate.add(sostituzione);
            }
        }
    }

    private Lezione assegnaSostituzionePerLezione(Lezione lezioneDaSostituire) {
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
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
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
                    return new Lezione(sostitutiValidi, false, lezioneDaSostituire.getClasse(),
                            lezioneDaSostituire.getMateria() + " (Disposizione)",
                            lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
                }
            }
        }
        ArrayList<Docente> docentiClasse = gestoreDocenti.trovaDocentiPerClasse(lezioneDaSostituire.getClasse());
        for (Docente docente : docentiClasse) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Stessa classe)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }
        ArrayList<Docente> docentiMateriaAffine = gestoreDocenti.trovaDocentiPerMateria(lezioneDaSostituire.getMateria());
        for (Docente docente : docentiMateriaAffine) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Materia affine)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }
        for (Docente docente : gestoreDocenti.getDocenti()) {
            if (!docentiAssenti.contains(docente.getNome()) &&
                    isDocenteDisponibile(docente, lezioneDaSostituire.getOra()) &&
                    haLezioneAdiacente(docente, lezioneDaSostituire.getOra())) {
                ArrayList<String> sostituto = new ArrayList<>();
                sostituto.add(docente.getNome());
                return new Lezione(sostituto, false, lezioneDaSostituire.getClasse(),
                        lezioneDaSostituire.getMateria() + " (Pagamento)",
                        lezioneDaSostituire.getDurata(), lezioneDaSostituire.getOra(), lezioneDaSostituire.getGiorno());
            }
        }
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
                    if (docenteTrovato && lezioneOriginale.getOra().equals(oraCorrente)) {
                        int altezza = calcolaDurataBlocchi(lezioneOriginale);
                        c.gridx = col + 1;
                        c.gridy = riga + 1;
                        c.gridheight = altezza;
                        JPanel pannelloCompleto = creaPannelloSostituzione(lezioneOriginale, i);
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

    private JPanel creaPannelloSostituzione(Lezione lezioneOriginale, int indiceLezione) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelOriginale = new JPanel(new BorderLayout());
        String risultato = String.join(" ", lezioneOriginale.getDocente());
        JLabel docenteOriginale = new JLabel(risultato, SwingConstants.CENTER);
        JLabel materiaOriginale = new JLabel(lezioneOriginale.getMateria(), SwingConstants.CENTER);
        JLabel classeOriginale = new JLabel(lezioneOriginale.getClasse(), SwingConstants.CENTER);
        panelOriginale.add(docenteOriginale, BorderLayout.NORTH);
        panelOriginale.add(materiaOriginale, BorderLayout.CENTER);
        panelOriginale.add(classeOriginale, BorderLayout.SOUTH);
        panelOriginale.setBackground(new Color(255, 200, 200));
        panelOriginale.setBorder(BorderFactory.createLineBorder(Color.RED));

        JPanel panelSostituto = new JPanel(new BorderLayout());
        Lezione sostituzione = null;
        for (int i = 0; i < lezioniSostituite.size(); i++) {
            Lezione l = lezioniSostituite.get(i);
            if (l.getOra().equals(lezioneOriginale.getOra()) && l.getClasse().equals(lezioneOriginale.getClasse())) {
                sostituzione = sostituzioniAssegnate.get(i);
                break;
            }
        }
        if (sostituzione != null) {
            String risultatoSostituto = String.join(" ", sostituzione.getDocente());
            JLabel docenteSostituto = new JLabel("→ " + risultatoSostituto, SwingConstants.CENTER);
            JLabel tipoSostituzione = new JLabel(sostituzione.getMateria(), SwingConstants.CENTER);
            panelSostituto.add(docenteSostituto, BorderLayout.NORTH);
            panelSostituto.add(tipoSostituzione, BorderLayout.CENTER);
            panelSostituto.setBackground(new Color(200, 255, 200));
            panelSostituto.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        } else {
            JLabel nessunSostituto = new JLabel("Nessun sostituto", SwingConstants.CENTER);
            nessunSostituto.setForeground(Color.RED);
            panelSostituto.add(nessunSostituto, BorderLayout.CENTER);
            panelSostituto.setBackground(new Color(255, 230, 230));
            panelSostituto.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        panel.add(panelOriginale, BorderLayout.NORTH);
        panel.add(panelSostituto, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(150, 80));
        return panel;
    }
}
