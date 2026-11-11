package com.sipadu.service;

import com.sipadu.model.Anggota;
import com.sipadu.model.Kunjungan;
import com.sipadu.model.Peminjaman;
import com.sipadu.repository.AnggotaRepository;
import com.sipadu.repository.KunjunganRepository;
import com.sipadu.repository.PeminjamanRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LaporanService {
    private final PeminjamanRepository pinjamRepo;
    private final KunjunganRepository kunjunganRepo;
    private final AnggotaRepository anggotaRepo;

    public LaporanService(PeminjamanRepository pinjamRepo, KunjunganRepository kunjunganRepo,
                          AnggotaRepository anggotaRepo) {
        this.pinjamRepo = pinjamRepo;
        this.kunjunganRepo = kunjunganRepo;
        this.anggotaRepo = anggotaRepo;
    }

    public long pengunjungHariIni() {
        LocalDate hariIni = LocalDate.now();
        return kunjunganRepo.semua().stream()
                .filter(k -> k.getWaktu().toLocalDate().equals(hariIni))
                .count();
    }

    public int totalPengunjung() {
        return kunjunganRepo.semua().size();
    }

    public List<Kunjungan> daftarKunjungan() {
        return kunjunganRepo.semua();
    }

    public List<Map.Entry<String, Long>> bukuTerpopuler(int n) {
        return pinjamRepo.semua().stream()
                .collect(Collectors.groupingBy(p -> p.getBuku().getJudul(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Peminjaman> semuaPeminjaman() {
        return pinjamRepo.semua();
    }

    public List<Peminjaman> daftarTerlambat() {
        LocalDate hariIni = LocalDate.now();
        return pinjamRepo.semua().stream()
                .filter(p -> p.isAktif() && p.isTerlambat(hariIni))
                .collect(Collectors.toList());
    }

    public List<Anggota> daftarBlacklist() {
        return anggotaRepo.semua().stream()
                .filter(Anggota::isBlacklist)
                .collect(Collectors.toList());
    }

    public long jumlahPeminjamanAktif() {
        return pinjamRepo.semua().stream().filter(Peminjaman::isAktif).count();
    }
}
