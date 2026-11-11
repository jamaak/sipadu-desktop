package com.sipadu.gui;

import com.sipadu.model.Admin;
import com.sipadu.model.Anggota;
import com.sipadu.model.KepalaOperasional;
import com.sipadu.repository.AnggotaRepository;
import com.sipadu.repository.BukuRepository;
import com.sipadu.repository.KunjunganRepository;
import com.sipadu.repository.PeminjamanRepository;
import com.sipadu.service.AnggotaService;
import com.sipadu.service.BukuService;
import com.sipadu.service.KunjunganService;
import com.sipadu.service.LaporanService;
import com.sipadu.service.PeminjamanService;

import java.time.LocalDate;

/** Menyatukan repository dan service, plus data contoh agar tampilan tidak kosong. */
final class AppContext {
    final BukuService buku;
    final AnggotaService anggota;
    final KunjunganService kunjungan;
    final PeminjamanService peminjaman;
    final LaporanService laporan;

    final Admin admin = new Admin("ADM01", "Petugas Perpustakaan", "admin", "admin123");
    final KepalaOperasional kepala = new KepalaOperasional("KOP01", "Kepala Operasional", "kepala", "kepala123");

    AppContext() {
        BukuRepository bukuRepo = new BukuRepository();
        AnggotaRepository anggotaRepo = new AnggotaRepository();
        PeminjamanRepository pinjamRepo = new PeminjamanRepository();
        KunjunganRepository kunjunganRepo = new KunjunganRepository();

        buku = new BukuService(bukuRepo, pinjamRepo);
        anggota = new AnggotaService(anggotaRepo);
        kunjungan = new KunjunganService(kunjunganRepo, anggotaRepo);
        peminjaman = new PeminjamanService(pinjamRepo, anggotaRepo, bukuRepo);
        laporan = new LaporanService(pinjamRepo, kunjunganRepo, anggotaRepo);

        isiDataContoh();
    }

    private void isiDataContoh() {
        buku.tambah("Pemrograman Java Dasar", "Budi Santoso", "Teknologi", 5);
        buku.tambah("Algoritma dan Struktur Data", "Rinaldi Munir", "Teknologi", 3);
        buku.tambah("Laskar Pelangi", "Andrea Hirata", "Novel", 4);
        buku.tambah("Bumi Manusia", "Pramoedya Ananta Toer", "Novel", 2);
        buku.tambah("Sejarah Indonesia Modern", "M.C. Ricklefs", "Sejarah", 2);
        buku.tambah("Basis Data untuk Pemula", "Fathansyah", "Teknologi", 3);
        buku.tambah("Negeri 5 Menara", "Ahmad Fuadi", "Novel", 3);
        buku.tambah("Ekonomi Dasar", "N. Gregory Mankiw", "Ekonomi", 2);

        Anggota andi = anggota.daftar("Andi Pratama");
        Anggota siti = anggota.daftar("Siti Rahma");
        Anggota budi = anggota.daftar("Budi Hartono");
        Anggota dewi = anggota.daftar("Dewi Lestari");
        Anggota rudi = anggota.daftar("Rudi Hartanto");
        rudi.setBlacklist(true);

        LocalDate hariIni = LocalDate.now();
        peminjaman.pinjam(andi.getId(), "B001", hariIni.minusDays(3));
        peminjaman.pinjam(andi.getId(), "B003", hariIni.minusDays(1));
        peminjaman.pinjam(siti.getId(), "B003", hariIni.minusDays(12)); // terlambat 5 hari
        peminjaman.pinjam(budi.getId(), "B002", hariIni.minusDays(2));
        peminjaman.pinjam(dewi.getId(), "B004", hariIni.minusDays(1));
        peminjaman.kembalikan(dewi.getId(), "B004");

        kunjungan.catat("Andi Pratama");
        kunjungan.catat("Siti Rahma");
        kunjungan.catat("Tamu Umum");
    }
}
