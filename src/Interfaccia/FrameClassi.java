package Interfaccia;

import Gestori.CreazioneOrario;
import Gestori.GestioneDati;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameClassi extends JPanel {

    JComboBox<String> orarioClasse = new JComboBox<>();
    private JPanel pannelloOrarioContainer = new JPanel(new BorderLayout());

    public FrameClassi(GestioneDati gestore) {
        CreazioneOrario orario = new CreazioneOrario(gestore);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        for (String classe : gestore.getClassi()) {
            if (!classe.contains("Disposizione")) {
                orarioClasse.addItem(classe);
            }
        }

        orarioClasse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orarioClasse.setBackground(new Color(135, 206, 250));
        orarioClasse.setForeground(Color.BLACK);

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
