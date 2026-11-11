package com.sipadu.gui;

import com.sipadu.model.Anggota;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

class AnggotaPanel extends JPanel implements Refreshable {
    private final AppContext ctx;
    private final DefaultTableModel model = Ui.model("ID", "Nama", "Terdaftar", "Status");
    private final JTable tabel = Ui.tabel(model);
    private final JTextField nama = new JTextField(24);

    AnggotaPanel(AppContext ctx) {
        this.ctx = ctx;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton daftar = Ui.tombol("Daftarkan Anggota Baru");
        atas.add(new JLabel("Nama lengkap calon anggota:"));
        atas.add(nama);
        atas.add(daftar);
        daftar.addActionListener(e -> daftar());
        nama.addActionListener(e -> daftar());

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton cabut = Ui.tombol("Cabut Blacklist");
        cabut.addActionListener(e -> cabutBlacklist());
        bawah.add(cabut);

        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(bawah, BorderLayout.SOUTH);
        refresh();
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        for (Anggota a : ctx.anggota.semua()) {
            model.addRow(new Object[]{a.getId(), a.getNama(), a.getTanggalDaftar().format(Ui.TGL),
                    a.isBlacklist() ? "BLACKLIST" : "Aktif"});
        }
    }

    private void daftar() {
        try {
            Anggota a = ctx.anggota.daftar(nama.getText());
            nama.setText("");
            refresh();
            JTextArea kartu = new JTextArea(a.cetakIdCard());
            kartu.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            kartu.setEditable(false);
            JOptionPane.showMessageDialog(this, kartu, "ID Card Anggota", JOptionPane.PLAIN_MESSAGE);
        } catch (IllegalArgumentException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void cabutBlacklist() {
        int r = tabel.getSelectedRow();
        if (r < 0) {
            Ui.error(this, "Pilih anggota di tabel terlebih dahulu.");
            return;
        }
        String id = (String) tabel.getValueAt(r, 0);
        ctx.anggota.cabutBlacklist(id);
        refresh();
        Ui.info(this, "Status blacklist " + id + " dicabut.");
    }
}
