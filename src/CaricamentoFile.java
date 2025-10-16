import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;

public class CaricamentoFile extends JPanel {

    JButton caricaCVS = new JButton("Seleziona file da caricare");
    GestioneDati gestoreDeiDati = new GestioneDati();

    public CaricamentoFile() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel centro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(centro, BorderLayout.CENTER);

        caricaCVS.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        caricaCVS.setBackground(new Color(135, 206, 250));
        caricaCVS.setForeground(Color.BLACK);
        caricaCVS.setFocusPainted(false);

        caricaCVS.setPreferredSize(new Dimension(200, 40));

        centro.add(caricaCVS);

        caricaCVS.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                File letturaFile = new File("letturaFile.txt");

                try (BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                     BufferedWriter bw = new BufferedWriter(new FileWriter(letturaFile))) {

                    br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {
                        bw.write(line);
                        bw.newLine();
                    }
                    JOptionPane.showMessageDialog(this,
                            "File salvato con successo in: " + letturaFile.getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Errore nel caricamento o salvataggio",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            gestoreDeiDati.organizzazioneFile("letturaFile.txt");

            System.out.println(gestoreDeiDati.toString());
        });
    }
}
