import javax.swing.*;
import java.awt.*;

public class FrameOrario extends JPanel {


    JComboBox orario = new JComboBox();
    String[] sceltaOrari = {"bgbf", "rgregrg", "rthrgrgdfgf"};


    public FrameOrario()
    {
        setLayout(new BorderLayout());

        for (int i = 0; i < sceltaOrari.length; i++) {
            orario.addItem(sceltaOrari[i]);
        }
        add(orario, BorderLayout.CENTER);
    }
}
