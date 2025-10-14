import javax.swing.*;
import java.awt.*;

public class FrameDisposizioni extends JPanel {


    JComboBox orario = new JComboBox();
    String[] sceltaOrari = {"disp1", "disp33", "disp35"};



    public FrameDisposizioni(GestioneDati gestore)
    {
        setLayout(new BorderLayout());

        for (int i = 0; i < sceltaOrari.length; i++) {
            orario.addItem(sceltaOrari[i]);
        }
        add(orario, BorderLayout.CENTER);

    }
}
