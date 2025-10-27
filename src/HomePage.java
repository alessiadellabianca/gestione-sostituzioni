import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JLabel scrittaIniziale = new JLabel("Gestione Orari e Sostituzioni - Scuola IS Saraceno-Romegialli");
    public JButton sostituzioni = new JButton("Sostituzioni");
    public JComboBox<String> orario = new JComboBox<>();
    public JButton caricaFile = new JButton("Carica File");
    String[] sceltaOrari = {"Docenti", "Classe", "Disposizione"};

    public HomePage(GestioneDati gestore) {
        super("HomePage");

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus Look and Feel non disponibile");
        }

        getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel pannelloScritta = new JPanel(new BorderLayout());
        pannelloScritta.setBorder(new EmptyBorder(15, 15, 15, 15));

        scrittaIniziale.setHorizontalAlignment(SwingConstants.CENTER);
        scrittaIniziale.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pannelloScritta.add(scrittaIniziale, BorderLayout.NORTH);

        JPanel pannelloBottoni = new JPanel(new GridLayout(1, 3, 15, 0));
        pannelloBottoni.setBorder(new EmptyBorder(15, 0, 0, 0));

        pannelloBottoni.add(sostituzioni);
        for (String item : sceltaOrari) {
            orario.addItem(item);
        }
        pannelloBottoni.add(orario);
        pannelloBottoni.add(caricaFile);

        Color lightBlue = new Color(135, 206, 250);
        JButton[] buttons = {sostituzioni, caricaFile};
        for (JButton bott : buttons) {
            bott.setBackground(lightBlue);
            bott.setForeground(Color.BLACK);
            bott.setFocusPainted(false);
            bott.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        orario.setBackground(lightBlue);
        orario.setForeground(Color.BLACK);
        orario.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pannelloScritta.add(pannelloBottoni, BorderLayout.SOUTH);
        getContentPane().add(pannelloScritta, BorderLayout.NORTH);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        getContentPane().add(container, BorderLayout.CENTER);

        setSize(900, 700);
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
            public void actionPerformed(ActionEvent e) {
                container.removeAll();
                container.add(new CaricamentoFile(), BorderLayout.CENTER);
                container.revalidate();
                container.repaint();
            }
        });
    }
}
