package com.sipadu.model;

import java.time.LocalDateTime;

public class Kunjungan {
    private final String nama;
    private final String idAnggota; // null jika non-anggota
    private final LocalDateTime waktu;

    public Kunjungan(String nama, String idAnggota, LocalDateTime waktu) {
        this.nama = nama;
        this.idAnggota = idAnggota;
        this.waktu = waktu;
    }

    public String getNama() {
        return nama;
    }

    public String getIdAnggota() {
        return idAnggota;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public boolean isAnggota() {
        return idAnggota != null;
    }
}
