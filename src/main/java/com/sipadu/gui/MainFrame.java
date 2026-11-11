package com.sipadu.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

class MainFrame extends JFrame {

    MainFrame(AppContext ctx, Peran peran, String nama) {
        super("SIPADU - Sistem Perpustakaan Terpadu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Ui.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        JLabel judul = new JLabel("SIPADU");
        judul.setForeground(Color.WHITE);
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 22f));
        JLabel siapa = new JLabel(nama.equals(peran.label) ? nama : peran.label + " - " + nama);
        siapa.setForeground(Color.WHITE);
        JButton logout = Ui.tombol("Logout");
        logout.addActionListener(e -> {
            new LoginFrame(ctx).setVisible(true);
            dispose();
        });
        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        kanan.setOpaque(false);
        kanan.add(siapa);
        kanan.add(logout);
        header.add(judul, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        switch (peran) {
            case PENGUNJUNG -> {
                tabs.addTab("Absensi Kunjungan", new AbsensiPanel(ctx));
                tabs.addTab("Katalog Buku", new BukuPanel(ctx, false));
            }
            case ADMIN -> {
                tabs.addTab("Manajemen Buku", new BukuPanel(ctx, true));
                tabs.addTab("Keanggotaan", new AnggotaPanel(ctx));
                tabs.addTab("Peminjaman & Pengembalian", new PeminjamanPanel(ctx));
            }
            case KEPALA -> {
                tabs.addTab("Laporan", new LaporanPanel(ctx));
                tabs.addTab("Katalog Buku", new BukuPanel(ctx, false));
            }
        }
        tabs.addChangeListener(e -> {
            Component c = tabs.getSelectedComponent();
            if (c instanceof Refreshable r) {
                r.refresh();
            }
        });

        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setPreferredSize(new Dimension(1040, 660));
        pack();
        setLocationRelativeTo(null);
    }
}
