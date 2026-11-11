package com.sipadu.gui;

import com.sipadu.model.Buku;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

class BukuPanel extends JPanel implements Refreshable {
    private final AppContext ctx;
    private final DefaultTableModel model = Ui.model("ID", "Judul", "Penulis", "Kategori", "Stok");
    private final JTable tabel = Ui.tabel(model);
    private final JComboBox<String> jenis = new JComboBox<>(new String[]{"Judul", "Penulis", "Kategori"});
    private final JTextField kata = new JTextField(22);

    BukuPanel(AppContext ctx, boolean bisaKelola) {
        this.ctx = ctx;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton cari = Ui.tombol("Cari");
        JButton semua = Ui.tombol("Tampilkan Semua");
        atas.add(new JLabel("Cari berdasarkan:"));
        atas.add(jenis);
        atas.add(kata);
        atas.add(cari);
        atas.add(semua);
        cari.addActionListener(e -> cari());
        kata.addActionListener(e -> cari());
        semua.addActionListener(e -> {
            kata.setText("");
            refresh();
        });

        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        if (bisaKelola) {
            JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            JButton tambah = Ui.tombol("Tambah Buku");
            JButton ubah = Ui.tombol("Ubah Buku");
            JButton hapus = Ui.tombol("Hapus Buku");
            tambah.addActionListener(e -> tambah());
            ubah.addActionListener(e -> ubah());
            hapus.addActionListener(e -> hapus());
            bawah.add(tambah);
            bawah.add(ubah);
            bawah.add(hapus);
            add(bawah, BorderLayout.SOUTH);
        }
        refresh();
    }

    @Override
    public void refresh() {
        isi(ctx.buku.semua());
    }

    private void isi(List<Buku> list) {
        model.setRowCount(0);
        for (Buku b : list) {
            model.addRow(new Object[]{b.getId(), b.getJudul(), b.getPenulis(), b.getKategori(), b.getStok()});
        }
    }

    private void cari() {
        isi(ctx.buku.cari(jenis.getSelectedIndex() + 1, kata.getText()));
    }

    private void tambah() {
        String[] f = form("Tambah Buku", "", "", "", "0");
        if (f == null) {
            return;
        }
        try {
            Buku b = ctx.buku.tambah(f[0], f[1], f[2], Integer.parseInt(f[3].trim()));
            refresh();
            Ui.info(this, "Buku ditambahkan dengan ID " + b.getId() + ".");
        } catch (NumberFormatException e) {
            Ui.error(this, "Stok harus berupa angka.");
        } catch (IllegalArgumentException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void ubah() {
        String id = idTerpilih();
        if (id == null) {
            return;
        }
        Buku lama = ctx.buku.cari(id);
        String[] f = form("Ubah Buku " + id, lama.getJudul(), lama.getPenulis(), lama.getKategori(),
                String.valueOf(lama.getStok()));
        if (f == null) {
            return;
        }
        try {
            int stok = f[3].isBlank() ? -1 : Integer.parseInt(f[3].trim());
            ctx.buku.ubah(id, f[0], f[1], f[2], stok);
            refresh();
        } catch (NumberFormatException e) {
            Ui.error(this, "Stok harus berupa angka.");
        } catch (IllegalArgumentException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void hapus() {
        String id = idTerpilih();
        if (id == null) {
            return;
        }
        int yakin = JOptionPane.showConfirmDialog(this, "Hapus buku " + id + "?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (yakin != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            ctx.buku.hapus(id);
            refresh();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private String idTerpilih() {
        int r = tabel.getSelectedRow();
        if (r < 0) {
            Ui.error(this, "Pilih satu buku di tabel terlebih dahulu.");
            return null;
        }
        return (String) tabel.getValueAt(r, 0);
    }

    private String[] form(String judulDialog, String j, String p, String k, String s) {
        JTextField fj = new JTextField(j, 22);
        JTextField fp = new JTextField(p, 22);
        JTextField fk = new JTextField(k, 22);
        JTextField fs = new JTextField(s, 22);
        JPanel pnl = new JPanel(new GridLayout(0, 2, 8, 8));
        pnl.add(new JLabel("Judul"));
        pnl.add(fj);
        pnl.add(new JLabel("Penulis"));
        pnl.add(fp);
        pnl.add(new JLabel("Kategori"));
        pnl.add(fk);
        pnl.add(new JLabel("Stok"));
        pnl.add(fs);
        int ok = JOptionPane.showConfirmDialog(this, pnl, judulDialog, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        return ok == JOptionPane.OK_OPTION
                ? new String[]{fj.getText(), fp.getText(), fk.getText(), fs.getText()}
                : null;
    }
}
