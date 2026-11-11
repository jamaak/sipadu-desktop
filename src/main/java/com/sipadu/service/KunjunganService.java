package com.sipadu.service;

import com.sipadu.model.Anggota;
import com.sipadu.model.Kunjungan;
import com.sipadu.repository.AnggotaRepository;
import com.sipadu.repository.KunjunganRepository;

import java.time.LocalDateTime;

public class KunjunganService {
    private final KunjunganRepository repo;
    private final AnggotaRepository anggotaRepo;

    public KunjunganService(KunjunganRepository repo, AnggotaRepository anggotaRepo) {
        this.repo = repo;
        this.anggotaRepo = anggotaRepo;
    }

    /** Input bisa ID Anggota atau nama. Jika tidak cocok dengan anggota, dicatat sebagai non-anggota. */
    public Kunjungan catat(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Nama/ID tidak boleh kosong.");
        }
        Anggota a = anggotaRepo.cari(input);
        if (a == null) {
            a = anggotaRepo.cariByNama(input);
        }
        Kunjungan k = (a != null)
                ? new Kunjungan(a.getNama(), a.getId(), LocalDateTime.now())
                : new Kunjungan(input.trim(), null, LocalDateTime.now());
        repo.simpan(k);
        return k;
    }
}
