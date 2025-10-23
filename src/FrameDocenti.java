import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameDocenti extends JPanel {

    JComboBox<String> orarioDocente = new JComboBox<>();
    private JPanel pannelloOrarioContainer = new JPanel(new BorderLayout());


    public FrameDocenti(GestioneDati gestore) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        CreazioneOrarioDocenti orario=new CreazioneOrarioDocenti(gestore);

        for (String docente : gestore.getDocenti()) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {
                orarioDocente.addItem(senzaVirgolette);
            }
        }


        orarioDocente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orarioDocente.setBackground(new Color(135, 206, 250));
        orarioDocente.setForeground(Color.BLACK);

        add(orarioDocente, BorderLayout.NORTH);
        add(pannelloOrarioContainer, BorderLayout.CENTER);

        orarioDocente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orario.creazioneOrarioTabella(orarioDocente.getSelectedItem().toString());
                pannelloOrarioContainer.removeAll();
                pannelloOrarioContainer.add(orario.getPanelloOrario(), BorderLayout.CENTER);
                pannelloOrarioContainer.revalidate();
                pannelloOrarioContainer.repaint();
            }
        });
    }

}


