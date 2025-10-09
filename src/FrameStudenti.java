import javax.swing.*;
import java.awt.*;

public class FrameStudenti extends JPanel {


    JComboBox orario = new JComboBox();
    String[] sceltaOrari = {"stud1", "stud10", "stud3"};



    public FrameStudenti()
    {
        setLayout(new BorderLayout());

        for (int i = 0; i < sceltaOrari.length; i++) {
            orario.addItem(sceltaOrari[i]);
        }
        add(orario, BorderLayout.CENTER);

    }
}
