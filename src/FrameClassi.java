import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameClassi extends JPanel {


    JComboBox orarioClasse = new JComboBox();

    public FrameClassi(GestioneDati gestore)
    {
        setLayout(new BorderLayout());

        for (String classe : gestore.getClassi())
        {
            if(!classe.contains("Disposizione")){
            orarioClasse.addItem(classe);}
        }

        add(orarioClasse, BorderLayout.CENTER);

        orarioClasse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreazioneOrario orario = new CreazioneOrario(gestore);
                orario.creazioneOrarioClasse(orarioClasse.getSelectedItem().toString());
            }
        });

    }


}
