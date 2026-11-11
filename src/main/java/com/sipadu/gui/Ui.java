package com.sipadu.gui;

import com.sipadu.model.Peminjaman;
import com.sipadu.service.PeminjamanService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

final class Ui {
    static final Color PRIMARY = new Color(0x1F4E79);
    static final Color BG = new Color(0xF4F6F8);
    static final Color HIJAU = new Color(0x1E8449);
    static final Color MERAH = new Color(0xC0392B);

    static final DateTimeFormatter TGL = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    static final DateTimeFormatter WAKTU = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    static final String[] KOLOM_PINJAM =
            {"No", "Anggota", "Buku", "Tgl Pinjam", "Jatuh Tempo", "Kembali", "Status", "Denda"};

    private Ui() {
    }

    static DefaultTableModel model(String... kolom) {
        return new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    static JTable tabel(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(26);
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setReorderingAllowed(false);
        t.getTableHeader().setFont(t.getTableHeader().getFont().deriveFont(Font.BOLD));
        return t;
    }

    /** Tabel peminjaman; status "TERLAMBAT" diberi warna merah. */
    static JTable tabelPinjam(DefaultTableModel m) {
        JTable t = tabel(m);
        int[] lebar = {40, 140, 240, 100, 100, 100, 140, 90};
        for (int i = 0; i < lebar.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }
        t.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean sel,
                                                           boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, sel, focus, row, col);
                if (!sel) {
                    String s = String.valueOf(value);
                    c.setForeground(s.startsWith("TERLAMBAT") ? MERAH : table.getForeground());
                }
                return c;
            }
        });
        return t;
    }

    static void isiPeminjaman(DefaultTableModel m, List<Peminjaman> list) {
        m.setRowCount(0);
        LocalDate hariIni = LocalDate.now();
        for (Peminjaman p : list) {
            String status;
            if (!p.isAktif()) {
                status = "Dikembalikan";
            } else if (p.isTerlambat(hariIni)) {
                status = "TERLAMBAT " + p.hariTerlambat(hariIni) + " hari";
            } else {
                status = "Dipinjam";
            }
            m.addRow(new Object[]{p.getId(), p.getAnggota().getNama(), p.getBuku().getJudul(),
                    p.getTglPinjam().format(TGL), p.getTglJatuhTempo().format(TGL),
                    p.getTglKembali() == null ? "-" : p.getTglKembali().format(TGL),
                    status, rp(PeminjamanService.estimasiDenda(p))});
        }
    }

    static String rp(int nilai) {
        return "Rp" + String.format("%,d", nilai).replace(',', '.');
    }

    static JButton tombol(String teks) {
        JButton b = new JButton(teks);
        b.setFocusPainted(false);
        return b;
    }

    static JLabel judul(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 20f));
        l.setForeground(PRIMARY);
        return l;
    }

    static JPanel kartu(String judul, JLabel nilai) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xD5DBDB)),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        JLabel j = new JLabel(judul);
        j.setForeground(Color.GRAY);
        nilai.setFont(nilai.getFont().deriveFont(Font.BOLD, 24f));
        nilai.setForeground(PRIMARY);
        p.add(j, BorderLayout.NORTH);
        p.add(nilai, BorderLayout.CENTER);
        return p;
    }

    static void info(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "SIPADU", JOptionPane.INFORMATION_MESSAGE);
    }

    static void error(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "SIPADU", JOptionPane.WARNING_MESSAGE);
    }
}
