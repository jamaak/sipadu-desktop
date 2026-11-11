package com.sipadu.gui;

import com.sipadu.model.Anggota;
import com.sipadu.model.Kunjungan;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

class LaporanPanel extends JPanel implements Refreshable {
    private final AppContext ctx;

    private final JLabel vHariIni = new JLabel("0");
    private final JLabel vTotal = new JLabel("0");
    private final JLabel vAktif = new JLabel("0");
    private final JLabel vTerlambat = new JLabel("0");
    private final JLabel vBlacklist = new JLabel("0");

    private final DefaultTableModel mSemua = Ui.model(Ui.KOLOM_PINJAM);
    private final DefaultTableModel mTerlambat = Ui.model(Ui.KOLOM_PINJAM);
    private final DefaultTableModel mBlacklist = Ui.model("ID", "Nama");
    private final DefaultTableModel mKunjungan = Ui.model("Waktu", "Nama", "Status");
    private final DefaultTableModel mPopuler = Ui.model("Peringkat", "Judul Buku", "Dipinjam (kali)");

    LaporanPanel(AppContext ctx) {
        this.ctx = ctx;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel kartu = new JPanel(new GridLayout(1, 5, 10, 0));
        kartu.add(Ui.kartu("Pengunjung hari ini", vHariIni));
        kartu.add(Ui.kartu("Total kunjungan", vTotal));
        kartu.add(Ui.kartu("Peminjaman aktif", vAktif));
        kartu.add(Ui.kartu("Terlambat", vTerlambat));
        kartu.add(Ui.kartu("Di-blacklist", vBlacklist));

        JTabbedPane isi = new JTabbedPane();
        isi.addTab("Daftar Peminjam", new JScrollPane(Ui.tabelPinjam(mSemua)));
        isi.addTab("Terlambat", new JScrollPane(Ui.tabelPinjam(mTerlambat)));
        isi.addTab("Blacklist", new JScrollPane(Ui.tabel(mBlacklist)));
        isi.addTab("Kunjungan", new JScrollPane(Ui.tabel(mKunjungan)));
        isi.addTab("Buku Terpopuler", new JScrollPane(Ui.tabel(mPopuler)));

        add(kartu, BorderLayout.NORTH);
        add(isi, BorderLayout.CENTER);
        refresh();
    }

    @Override
    public void refresh() {
        vHariIni.setText(String.valueOf(ctx.laporan.pengunjungHariIni()));
        vTotal.setText(String.valueOf(ctx.laporan.totalPengunjung()));
        vAktif.setText(String.valueOf(ctx.laporan.jumlahPeminjamanAktif()));
        vTerlambat.setText(String.valueOf(ctx.laporan.daftarTerlambat().size()));
        vBlacklist.setText(String.valueOf(ctx.laporan.daftarBlacklist().size()));

        Ui.isiPeminjaman(mSemua, ctx.laporan.semuaPeminjaman());
        Ui.isiPeminjaman(mTerlambat, ctx.laporan.daftarTerlambat());

        mBlacklist.setRowCount(0);
        for (Anggota a : ctx.laporan.daftarBlacklist()) {
            mBlacklist.addRow(new Object[]{a.getId(), a.getNama()});
        }

        mKunjungan.setRowCount(0);
        for (Kunjungan k : ctx.laporan.daftarKunjungan()) {
            mKunjungan.addRow(new Object[]{k.getWaktu().format(Ui.WAKTU), k.getNama(),
                    k.isAnggota() ? k.getIdAnggota() : "Non-Anggota"});
        }

        mPopuler.setRowCount(0);
        List<Map.Entry<String, Long>> top = ctx.laporan.bukuTerpopuler(10);
        int no = 1;
        for (Map.Entry<String, Long> e : top) {
            mPopuler.addRow(new Object[]{no++, e.getKey(), e.getValue()});
        }
    }
}
