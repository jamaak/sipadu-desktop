package com.sipadu.gui;

import com.sipadu.model.Anggota;
import com.sipadu.model.Buku;
import com.sipadu.model.Peminjaman;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;

class PeminjamanPanel extends JPanel implements Refreshable {
    private final AppContext ctx;
    private final JComboBox<String> cbAnggota = new JComboBox<>();
    private final JComboBox<String> cbBuku = new JComboBox<>();
    private final DefaultTableModel model = Ui.model(Ui.KOLOM_PINJAM);
    private final JTable tabel = Ui.tabelPinjam(model);

    PeminjamanPanel(AppContext ctx) {
        this.ctx = ctx;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        cbAnggota.setPrototypeDisplayValue("AGT000 - Nama Anggota Panjang");
        cbBuku.setPrototypeDisplayValue("B000 - Judul Buku yang Cukup Panjang (stok 00)");

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton pinjam = Ui.tombol("Pinjamkan");
        JButton kembali = Ui.tombol("Kembalikan");
        atas.add(new JLabel("Anggota:"));
        atas.add(cbAnggota);
        atas.add(new JLabel("Buku:"));
        atas.add(cbBuku);
        atas.add(pinjam);
        atas.add(kembali);
        pinjam.addActionListener(e -> pinjam());
        kembali.addActionListener(e -> kembalikan());

        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        refresh();
    }

    @Override
    public void refresh() {
        int ia = cbAnggota.getSelectedIndex();
        int ib = cbBuku.getSelectedIndex();
        cbAnggota.removeAllItems();
        for (Anggota a : ctx.anggota.semua()) {
            cbAnggota.addItem(a.getId() + " - " + a.getNama());
        }
        cbBuku.removeAllItems();
        for (Buku b : ctx.buku.semua()) {
            cbBuku.addItem(b.getId() + " - " + b.getJudul() + " (stok " + b.getStok() + ")");
        }
        if (ia >= 0 && ia < cbAnggota.getItemCount()) {
            cbAnggota.setSelectedIndex(ia);
        }
        if (ib >= 0 && ib < cbBuku.getItemCount()) {
            cbBuku.setSelectedIndex(ib);
        }
        Ui.isiPeminjaman(model, ctx.laporan.semuaPeminjaman());
    }

    private String idDari(JComboBox<String> cb) {
        String s = (String) cb.getSelectedItem();
        return s == null ? "" : s.split(" - ")[0];
    }

    private void pinjam() {
        try {
            Peminjaman p = ctx.peminjaman.pinjam(idDari(cbAnggota), idDari(cbBuku));
            refresh();
            Ui.info(this, "Peminjaman berhasil.\n\nAnggota     : " + p.getAnggota().getNama()
                    + "\nBuku         : " + p.getBuku().getJudul()
                    + "\nTgl Pinjam  : " + p.getTglPinjam().format(Ui.TGL)
                    + "\nJatuh Tempo : " + p.getTglJatuhTempo().format(Ui.TGL));
        } catch (IllegalArgumentException | IllegalStateException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void kembalikan() {
        try {
            Peminjaman p = ctx.peminjaman.kembalikan(idDari(cbAnggota), idDari(cbBuku));
            refresh();
            int hari = p.hariTerlambat(LocalDate.now());
            if (hari > 0) {
                String pesan = "Terlambat " + hari + " hari.\nDenda: " + Ui.rp(p.getDenda());
                if (p.getAnggota().isBlacklist()) {
                    pesan += "\n\nAnggota otomatis masuk daftar blacklist.";
                }
                Ui.error(this, pesan);
            } else {
                Ui.info(this, "Pengembalian tepat waktu. Tidak ada denda.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            Ui.error(this, e.getMessage());
        }
    }
}
