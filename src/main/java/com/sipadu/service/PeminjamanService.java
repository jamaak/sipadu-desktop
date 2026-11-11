package com.sipadu.service;

import com.sipadu.model.Anggota;
import com.sipadu.model.Buku;
import com.sipadu.model.Peminjaman;
import com.sipadu.repository.AnggotaRepository;
import com.sipadu.repository.BukuRepository;
import com.sipadu.repository.PeminjamanRepository;

import java.time.LocalDate;

public class PeminjamanService {
    public static final int LAMA_PINJAM = 7;          // hari
    public static final int DENDA_PER_HARI = 1000;    // Rp
    public static final int BATAS_BLACKLIST = 7;      // terlambat lebih dari 7 hari -> blacklist

    private final PeminjamanRepository repo;
    private final AnggotaRepository anggotaRepo;
    private final BukuRepository bukuRepo;
    private int urut = 0;

    public PeminjamanService(PeminjamanRepository repo, AnggotaRepository anggotaRepo, BukuRepository bukuRepo) {
        this.repo = repo;
        this.anggotaRepo = anggotaRepo;
        this.bukuRepo = bukuRepo;
    }

    public Peminjaman pinjam(String idAnggota, String idBuku) {
        return pinjam(idAnggota, idBuku, LocalDate.now());
    }

    public Peminjaman pinjam(String idAnggota, String idBuku, LocalDate tanggal) {
        Anggota a = anggotaRepo.cari(idAnggota);
        if (a == null) {
            throw new IllegalArgumentException("Anggota tidak ditemukan. Non-anggota harus mendaftar dulu.");
        }
        Buku b = bukuRepo.cari(idBuku);
        if (b == null) {
            throw new IllegalArgumentException("Buku tidak ditemukan.");
        }
        if (a.isBlacklist()) {
            throw new IllegalStateException("Anggota di-blacklist, tidak boleh meminjam.");
        }
        if (repo.jumlahAktif(a.getId()) >= Anggota.MAX_PINJAM) {
            throw new IllegalStateException("Limit pinjaman tercapai (maks " + Anggota.MAX_PINJAM + " buku).");
        }
        if (adaTerlambat(a)) {
            throw new IllegalStateException("Anggota masih punya buku yang terlambat dikembalikan.");
        }
        if (repo.cariAktif(a.getId(), b.getId()) != null) {
            throw new IllegalStateException("Anggota sedang meminjam buku yang sama.");
        }
        if (b.getStok() <= 0) {
            throw new IllegalStateException("Stok buku habis.");
        }
        b.setStok(b.getStok() - 1);
        Peminjaman p = new Peminjaman(++urut, a, b, tanggal, LAMA_PINJAM);
        repo.simpan(p);
        return p;
    }

    public Peminjaman kembalikan(String idAnggota, String idBuku) {
        Peminjaman p = repo.cariAktif(idAnggota, idBuku);
        if (p == null) {
            throw new IllegalArgumentException("Tidak ada peminjaman aktif untuk kombinasi ID tersebut.");
        }
        LocalDate hariIni = LocalDate.now();
        p.setTglKembali(hariIni);
        int hari = p.hariTerlambat(hariIni);
        p.setDenda(hari * DENDA_PER_HARI);
        p.getBuku().setStok(p.getBuku().getStok() + 1);
        if (hari > BATAS_BLACKLIST) {
            p.getAnggota().setBlacklist(true);
        }
        return p;
    }

    /** Denda sementara untuk peminjaman aktif, denda final untuk yang sudah kembali. */
    public static int estimasiDenda(Peminjaman p) {
        return p.isAktif() ? p.hariTerlambat(LocalDate.now()) * DENDA_PER_HARI : p.getDenda();
    }

    private boolean adaTerlambat(Anggota a) {
        LocalDate hariIni = LocalDate.now();
        for (Peminjaman p : repo.semua()) {
            if (p.isAktif() && p.getAnggota().getId().equals(a.getId()) && p.isTerlambat(hariIni)) {
                return true;
            }
        }
        return false;
    }
}
