package com.sipadu.repository;

import com.sipadu.model.Peminjaman;

import java.util.ArrayList;
import java.util.List;

public class PeminjamanRepository {
    private final List<Peminjaman> data = new ArrayList<>();

    public void simpan(Peminjaman p) {
        data.add(p);
    }

    public List<Peminjaman> semua() {
        return new ArrayList<>(data);
    }

    public Peminjaman cariAktif(String idAnggota, String idBuku) {
        for (Peminjaman p : data) {
            if (p.isAktif()
                    && p.getAnggota().getId().equalsIgnoreCase(idAnggota)
                    && p.getBuku().getId().equalsIgnoreCase(idBuku)) {
                return p;
            }
        }
        return null;
    }

    public int jumlahAktif(String idAnggota) {
        int n = 0;
        for (Peminjaman p : data) {
            if (p.isAktif() && p.getAnggota().getId().equalsIgnoreCase(idAnggota)) {
                n++;
            }
        }
        return n;
    }

    public boolean adaAktifUntukBuku(String idBuku) {
        for (Peminjaman p : data) {
            if (p.isAktif() && p.getBuku().getId().equalsIgnoreCase(idBuku)) {
                return true;
            }
        }
        return false;
    }
}
