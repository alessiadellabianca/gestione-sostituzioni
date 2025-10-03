import javax.swing.*;
import java.awt.*;

public class FrameOrario extends JPanel {

    JButton orario = new JButton("orariiiiii");

    public FrameOrario()
    {
        setLayout(new BorderLayout());
        add(orario, BorderLayout.CENTER);
    }
}
