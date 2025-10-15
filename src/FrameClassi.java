import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameClassi extends JPanel {


    JComboBox orarioClasse = new JComboBox();
    private JPanel pannelloOrarioContainer = new JPanel(new BorderLayout());

    public FrameClassi(GestioneDati gestore)
    {
        CreazioneOrario orario = new CreazioneOrario(gestore);
        setLayout(new BorderLayout());
        for (String classe : gestore.getClassi())
        {
            if(!classe.contains("Disposizione")){
            orarioClasse.addItem(classe);}
        }

        add(orarioClasse, BorderLayout.NORTH);
        add(pannelloOrarioContainer, BorderLayout.CENTER);

        orarioClasse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orario.creazioneOrarioClasse(orarioClasse.getSelectedItem().toString());
                pannelloOrarioContainer.removeAll();
                pannelloOrarioContainer.add(orario.getPanelloOrario(), BorderLayout.CENTER);
                pannelloOrarioContainer.revalidate();
                pannelloOrarioContainer.repaint();
            }
        });

    }


}
