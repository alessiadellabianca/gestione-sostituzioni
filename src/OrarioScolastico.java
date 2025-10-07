public class OrarioScolastico {
}


/*import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class OrarioScolastico extends JFrame {

    public OrarioScolastico() {
        setTitle("Orario Scolastico");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] giorni = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
        Object[][] dati = {
                {"8:00-9:00", "Matematica", "Italiano", "Storia", "Inglese", "Arte"},
                {"9:00-10:00", "Scienze", "Matematica", "Educazione Fisica", "Musica", "Italiano"},
                {"10:00-11:00", "Italiano", "Geografia", "Matematica", "Storia", "Scienze"},
                {"11:00-12:00", "Inglese", "Arte", "Musica", "Matematica", "Educazione Fisica"}
        };

        DefaultTableModel model = new DefaultTableModel(dati, giorni);
        JTable table = new JTable(model);

        // Imposta altezza righe più grande
        table.setRowHeight(50);

        // Imposta larghezza colonne (modifica larghezza a piacere)
        int[] larghezze = {100, 130, 130, 130, 130, 130};
        for (int i = 0; i < larghezze.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(larghezze[i]);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrarioScolastico frame = new OrarioScolastico();
            frame.setVisible(true);
        });
    }
}
*/