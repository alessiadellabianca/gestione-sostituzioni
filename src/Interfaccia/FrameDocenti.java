package Interfaccia;

import Gestori.CreazioneOrarioDocenti;
import Gestori.GestioneDati;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;



        /*ArrayList<String> docentiUnici = new ArrayList<>();

        for (String docente : gestore.getDocenti()) {
            String pulito = docente.replace("\"", "").trim();

            if (!pulito.isEmpty()) {

                boolean giaPresente = false;
                for (String d : docentiUnici) {
                    if (d.equals(pulito)) {
                        giaPresente = true;
                        break;
                    }
                }

                if (!giaPresente) {
                    docentiUnici.add(pulito);
                }
            }
        }*/

public class FrameDocenti extends JPanel {

    JComboBox<String> orarioDocente = new JComboBox<>();
    private JPanel pannelloOrarioContainer = new JPanel(new BorderLayout());


     FrameDocenti(GestioneDati gestore) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        CreazioneOrarioDocenti orario=new CreazioneOrarioDocenti(gestore);
        ArrayList<String> docentiDaMettereNellaJComboBox = new ArrayList<>();

        for (String docente : gestore.getDocenti()) {
            String senzaVirgolette = docente.replace("\"", "").trim();
            if (!senzaVirgolette.isEmpty()) {

                boolean pieno = false;  //variabile per verificare se il docente è già presente nella lista
                for (String docen : docentiDaMettereNellaJComboBox) {
                    if (docen.equals(senzaVirgolette)) {
                        pieno = true;
                        break;
                    }
                }

                if (!pieno) {
                    docentiDaMettereNellaJComboBox.add(senzaVirgolette);
                }
            }
        }
        //bubble sort <3
        int n = docentiDaMettereNellaJComboBox.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                if (docentiDaMettereNellaJComboBox.get(j).compareTo(docentiDaMettereNellaJComboBox.get(j + 1)) > 0) {
                    String temp = docentiDaMettereNellaJComboBox.get(j);
                    docentiDaMettereNellaJComboBox.set(j, docentiDaMettereNellaJComboBox.get(j + 1));
                    docentiDaMettereNellaJComboBox.set(j + 1, temp);
                }
            }
        }

        for (String docente : docentiDaMettereNellaJComboBox) {
            orarioDocente.addItem(docente);
        }


        orarioDocente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        orarioDocente.setBackground(new Color(135, 206, 250));
        orarioDocente.setForeground(Color.BLACK);

        add(orarioDocente, BorderLayout.NORTH);
        add(pannelloOrarioContainer, BorderLayout.CENTER);

        orarioDocente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orario.creazioneOrarioTabella(orarioDocente.getSelectedItem().toString());
                pannelloOrarioContainer.removeAll();
                pannelloOrarioContainer.add(orario.getPanelloOrario(), BorderLayout.CENTER);
                pannelloOrarioContainer.revalidate();
                pannelloOrarioContainer.repaint();
            }
        });
    }
}


