import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JLabel scrittaIniziale = new JLabel("Gestione Orari e Sostituzioni - Scuola IS Saraceno-Romegialli");
    public JButton sostituzioni = new JButton("Sostituzioni");
    public JComboBox orario = new JComboBox<>();
    public JButton caricaFile = new JButton("Carica File");
    String[] sceltaOrari = {"Docenti", "Classe", "Disposizione"};

    public HomePage(GestioneDati gestore) {
        super("HomePage");
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel pannelloScritta = new JPanel(new GridLayout(2, 1));
        pannelloScritta.add(scrittaIniziale);
        scrittaIniziale.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel pannelloBottoni = new JPanel(new GridLayout(1, 3, 5, 0));
        pannelloBottoni.add(sostituzioni);
        for (int i = 0; i < sceltaOrari.length; i++) {
            orario.addItem(sceltaOrari[i]);
        }

        pannelloBottoni.add(orario);
        pannelloBottoni.add(caricaFile);

        pannelloScritta.add(pannelloBottoni);

        sostituzioni.setBackground(new Color(173, 216, 230));
        sostituzioni.setForeground(Color.BLACK);
        orario.setBackground(new Color(173, 216, 230));
        orario.setForeground(Color.BLACK);
        caricaFile.setBackground(new Color(173, 216, 230));
        caricaFile.setForeground(Color.BLACK);

        getContentPane().add(pannelloScritta, BorderLayout.NORTH);

        JPanel container = new JPanel();

        getContentPane().add(container, BorderLayout.CENTER);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);



        sostituzioni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.removeAll();
                container.add(new FrameSostituzioni(gestore), BorderLayout.CENTER);
                container.revalidate();
                container.repaint();
            }
        });

        orario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(orario.getSelectedItem() == "Docenti") {
                    container.removeAll();
                    container.add(new FrameDocenti(gestore), BorderLayout.CENTER);
                    container.revalidate();
                    container.repaint();
                }
                if(orario.getSelectedItem() == "Classe") {
                    container.removeAll();
                    container.add(new FrameClassi(gestore), BorderLayout.CENTER);
                    container.revalidate();
                    container.repaint();
                }
                if(orario.getSelectedItem() == "Disposizione") {
                    container.removeAll();
                    container.add(new FrameDisposizioni(gestore), BorderLayout.CENTER);
                    container.revalidate();
                    container.repaint();
                }
            }
        });

        caricaFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                container.removeAll();
                container.add(new CaricamentoFile(), BorderLayout.CENTER);
                container.revalidate();
                container.repaint();
            }
        });
    }
}
