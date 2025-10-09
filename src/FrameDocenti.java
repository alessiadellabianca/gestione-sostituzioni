import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FrameDocenti extends JPanel {

    JComboBox<String> orario = new JComboBox<>();
    GestioneDati docentx=new GestioneDati();
    ArrayList<String> listaDocenti=new ArrayList<>();

    public FrameDocenti() {
        setLayout(new BorderLayout());

        for(int i=0;i<docentx.getDocenti().size();i++)
        {
            listaDocenti.add(docentx.getDocenti().get(i));
            orario.addItem(listaDocenti.get(i));
        }

        add(orario, BorderLayout.CENTER);
    }
}
