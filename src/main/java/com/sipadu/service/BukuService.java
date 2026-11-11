package com.sipadu.service;

import com.sipadu.model.Buku;
import com.sipadu.repository.BukuRepository;
import com.sipadu.repository.PeminjamanRepository;

import java.util.ArrayList;
import java.util.List;

public class BukuService {
    private final BukuRepository repo;
    private final PeminjamanRepository pinjamRepo;
    private int urut = 0;

    public BukuService(BukuRepository repo, PeminjamanRepository pinjamRepo) {
        this.repo = repo;
        this.pinjamRepo = pinjamRepo;
    }

    public Buku tambah(String judul, String penulis, String kategori, int stok) {
        if (judul == null || judul.isBlank()) {
            throw new IllegalArgumentException("Judul tidak boleh kosong.");
        }
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif.");
        }
        urut++;
        Buku b = new Buku(String.format("B%03d", urut), judul.trim(), penulis.trim(), kategori.trim(), stok);
        repo.simpan(b);
        return b;
    }

    /** Field kosong (atau stok -1) berarti tidak diubah. */
    public Buku ubah(String id, String judul, String penulis, String kategori, int stok) {
        Buku b = repo.cari(id);
        if (b == null) {
            throw new IllegalArgumentException("Buku dengan ID " + id + " tidak ditemukan.");
        }
        if (!judul.isBlank()) {
            b.setJudul(judul.trim());
        }
        if (!penulis.isBlank()) {
            b.setPenulis(penulis.trim());
        }
        if (!kategori.isBlank()) {
            b.setKategori(kategori.trim());
        }
        if (stok >= 0) {
            b.setStok(stok);
        }
        return b;
    }

    public void hapus(String id) {
        Buku b = repo.cari(id);
        if (b == null) {
            throw new IllegalArgumentException("Buku dengan ID " + id + " tidak ditemukan.");
        }
        if (pinjamRepo.adaAktifUntukBuku(b.getId())) {
            throw new IllegalStateException("Buku masih dipinjam, tidak bisa dihapus.");
        }
        repo.hapus(b.getId());
    }

    public Buku cari(String id) {
        return repo.cari(id);
    }

    public List<Buku> semua() {
        return repo.semua();
    }

    /** jenis: 1 = judul, 2 = penulis, 3 = kategori. */
    public List<Buku> cari(int jenis, String kata) {
        String k = kata.trim().toLowerCase();
        List<Buku> hasil = new ArrayList<>();
        for (Buku b : repo.semua()) {
            String target = switch (jenis) {
                case 1 -> b.getJudul();
                case 2 -> b.getPenulis();
                case 3 -> b.getKategori();
                default -> throw new IllegalArgumentException("Jenis pencarian tidak valid.");
            };
            if (target.toLowerCase().contains(k)) {
                hasil.add(b);
            }
        }
        return hasil;
    }
}
