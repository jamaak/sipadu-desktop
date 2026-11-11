package com.sipadu.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Peminjaman {
    private final int id;
    private final Anggota anggota;
    private final Buku buku;
    private final LocalDate tglPinjam;
    private final LocalDate tglJatuhTempo;
    private LocalDate tglKembali;
    private int denda;

    public Peminjaman(int id, Anggota anggota, Buku buku, LocalDate tglPinjam, int lamaHari) {
        this.id = id;
        this.anggota = anggota;
        this.buku = buku;
        this.tglPinjam = tglPinjam;
        this.tglJatuhTempo = tglPinjam.plusDays(lamaHari);
    }

    public int getId() {
        return id;
    }

    public Anggota getAnggota() {
        return anggota;
    }

    public Buku getBuku() {
        return buku;
    }

    public LocalDate getTglPinjam() {
        return tglPinjam;
    }

    public LocalDate getTglJatuhTempo() {
        return tglJatuhTempo;
    }

    public LocalDate getTglKembali() {
        return tglKembali;
    }

    public void setTglKembali(LocalDate tglKembali) {
        this.tglKembali = tglKembali;
    }

    public int getDenda() {
        return denda;
    }

    public void setDenda(int denda) {
        this.denda = denda;
    }

    public boolean isAktif() {
        return tglKembali == null;
    }

    public int hariTerlambat(LocalDate hariIni) {
        LocalDate akhir = (tglKembali != null) ? tglKembali : hariIni;
        return (int) Math.max(0, ChronoUnit.DAYS.between(tglJatuhTempo, akhir));
    }

    public boolean isTerlambat(LocalDate hariIni) {
        return hariTerlambat(hariIni) > 0;
    }
}
