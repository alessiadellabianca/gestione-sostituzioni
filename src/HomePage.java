import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JLabel scrittaIniziale = new JLabel("Gestione Orari e Sostituzioni - Scuola IS Saraceno-Romegialli");
    public JButton sostituzioni = new JButton("Sostituzioni");
    public JButton orario = new JButton("Orario");
    public JButton caricaFile = new JButton("Carica File");

    public HomePage() {
        super("HomePage");
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel pannelloScritta = new JPanel(new GridLayout(2, 1));
        pannelloScritta.add(scrittaIniziale);
        scrittaIniziale.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel pannelloBottoni = new JPanel(new GridLayout(1, 3, 5, 0));
        pannelloBottoni.add(sostituzioni);
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

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        //sostituzioni.addActionListener(e -> new FrameSostituzioni());
        //orario.addActionListener(e -> new FrameOrario());

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
