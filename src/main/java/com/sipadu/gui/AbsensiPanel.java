package com.sipadu.gui;

import com.sipadu.model.Kunjungan;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;

class AbsensiPanel extends JPanel {

    AbsensiPanel(AppContext ctx) {
        setLayout(new GridBagLayout());
        setBackground(Ui.BG);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);

        JLabel judul = Ui.judul("Absensi Kunjungan");
        JLabel ket = new JLabel("Ketik nama atau ID Anggota, lalu tekan Catat Kehadiran.");
        JTextField input = new JTextField(24);
        input.setMaximumSize(new Dimension(380, 32));
        JButton catat = Ui.tombol("Catat Kehadiran");
        JLabel hasil = new JLabel(" ");

        for (Component c : new Component[]{judul, ket, input, catat, hasil}) {
            ((javax.swing.JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        Runnable aksi = () -> {
            try {
                Kunjungan k = ctx.kunjungan.catat(input.getText());
                hasil.setForeground(Ui.HIJAU);
                hasil.setText("Tercatat: " + k.getNama() + " ("
                        + (k.isAnggota() ? k.getIdAnggota() : "Non-Anggota") + ") - "
                        + k.getWaktu().format(Ui.WAKTU));
                input.setText("");
            } catch (IllegalArgumentException e) {
                hasil.setForeground(Color.RED);
                hasil.setText(e.getMessage());
            }
        };
        catat.addActionListener(e -> aksi.run());
        input.addActionListener(e -> aksi.run());

        box.add(judul);
        box.add(javax.swing.Box.createVerticalStrut(8));
        box.add(ket);
        box.add(javax.swing.Box.createVerticalStrut(16));
        box.add(input);
        box.add(javax.swing.Box.createVerticalStrut(12));
        box.add(catat);
        box.add(javax.swing.Box.createVerticalStrut(16));
        box.add(hasil);
        add(box);
    }
}
