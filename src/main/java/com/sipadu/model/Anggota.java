package com.sipadu.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Anggota extends Pengguna {
    public static final int MAX_PINJAM = 3;

    private LocalDate tanggalDaftar;
    private boolean blacklist;

    public Anggota(String id, String nama) {
        super(id, nama);
        this.tanggalDaftar = LocalDate.now();
        this.blacklist = false;
    }

    public LocalDate getTanggalDaftar() {
        return tanggalDaftar;
    }

    public void setTanggalDaftar(LocalDate tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    /** ID Card berbentuk teks untuk dicetak di console. */
    public String cetakIdCard() {
        String garis = "+" + "-".repeat(34) + "+";
        String tgl = tanggalDaftar.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return garis + "\n"
                + String.format("| %-32s |%n", "     KARTU ANGGOTA SIPADU")
                + garis + "\n"
                + String.format("| %-32s |%n", "ID      : " + getId())
                + String.format("| %-32s |%n", "Nama    : " + getNama())
                + String.format("| %-32s |%n", "Terdaftar: " + tgl)
                + garis;
    }
}
