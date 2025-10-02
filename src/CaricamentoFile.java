import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CaricamentoFile extends JPanel
{
    JButton caricaCVS = new JButton("Seleziona file da caricare");

    public CaricamentoFile()
    {
        setLayout(new BorderLayout());
        add(caricaCVS, BorderLayout.CENTER);


        caricaCVS.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                File lunediFile = new File("lunediFile.txt");
                File martediFile = new File("martediFile.txt");
                File mercolediFile = new File("mercolediFile.txt");
                File giovediFile = new File("giovediFile.txt");
                File venerdiFile = new File("venerdiFile.txt");
                File sabatoFile = new File("sabatoFile.txt");


                try (BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                     BufferedWriter bw = new BufferedWriter(new FileWriter(martediFile)))
                {


                    String line;
                    while ((line = br.readLine()) != null) {
                        String trimmedLine = line.trim().toLowerCase();
                        if (trimmedLine.equals("martedì") || trimmedLine.equals("martedi")) {
                            break;
                        }
                        bw.write(line);
                        bw.newLine();
                    }


                    JOptionPane.showMessageDialog(this, "File salvato con successo in: " + lunediFile.getAbsolutePath());

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Errore nel caricamento o salvataggio", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });





    }
}