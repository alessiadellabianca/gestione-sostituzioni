package Interfaccia;

import Gestori.CreazioneOrarioDisposizioni;
import Gestori.GestioneDati;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameDisposizioni extends JPanel {

    JComboBox<String> orario = new JComboBox<>();
    CreazioneOrarioDisposizioni tabella = new CreazioneOrarioDisposizioni();
    private JPanel pannelloOrario = new JPanel(new BorderLayout());
    String[] sceltaGiorno = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato"};

    public FrameDisposizioni(GestioneDati gestore) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        for (String disp : sceltaGiorno) {
            orario.addItem(disp);
        }

        orario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orario.setBackground(new Color(135, 206, 250));
        orario.setForeground(Color.BLACK);

        add(orario, BorderLayout.NORTH);
        add(pannelloOrario, BorderLayout.CENTER);

        orario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tabella.tabellaDisposizioni(orario.getSelectedItem().toString());
                pannelloOrario.removeAll();
                pannelloOrario.add(tabella.getPanelloOrarioDisposizioni(), BorderLayout.CENTER);
                pannelloOrario.revalidate();
                pannelloOrario.repaint();
            }
        });
    }

}
