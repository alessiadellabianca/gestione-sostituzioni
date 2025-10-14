import javax.swing.*;
import java.awt.*;

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

    }
}
