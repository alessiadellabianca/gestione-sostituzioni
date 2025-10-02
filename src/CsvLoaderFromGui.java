import javax.swing.*;
import java.io.*;

public class CsvLoaderFromGui {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Carica CSV e salva in file di testo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);

        JButton loadButton = new JButton("Carica file CSV");

        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                File outputFile = new File("output.txt"); // File di destinazione

                try (BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                     BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

                    String line;
                    while ((line = br.readLine()) != null) {
                        // Scrivo ogni riga letta nel file di output
                        bw.write(line);
                        bw.newLine(); // A capo
                    }

                    JOptionPane.showMessageDialog(frame, "File salvato con successo in: " + outputFile.getAbsolutePath());

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Errore nel caricamento o salvataggio", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.getContentPane().add(loadButton);
        frame.setVisible(true);
    }
}

