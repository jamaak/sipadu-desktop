package com.sipadu.service;

import com.sipadu.model.Anggota;
import com.sipadu.repository.AnggotaRepository;

import java.util.List;

public class AnggotaService {
    private final AnggotaRepository repo;
    private int urut = 0;

    public AnggotaService(AnggotaRepository repo) {
        this.repo = repo;
    }

    public Anggota daftar(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        urut++;
        Anggota a = new Anggota(String.format("AGT%03d", urut), nama.trim());
        repo.simpan(a);
        return a;
    }

    public Anggota cari(String id) {
        return repo.cari(id);
    }

    public List<Anggota> semua() {
        return repo.semua();
    }

    public void cabutBlacklist(String id) {
        Anggota a = repo.cari(id);
        if (a == null) {
            throw new IllegalArgumentException("Anggota dengan ID " + id + " tidak ditemukan.");
        }
        a.setBlacklist(false);
    }
}
