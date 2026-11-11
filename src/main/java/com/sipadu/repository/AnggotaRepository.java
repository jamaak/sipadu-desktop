package com.sipadu.repository;

import com.sipadu.model.Anggota;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnggotaRepository {
    private final Map<String, Anggota> data = new LinkedHashMap<>();

    public void simpan(Anggota anggota) {
        data.put(anggota.getId(), anggota);
    }

    public Anggota cari(String id) {
        return id == null ? null : data.get(id.trim().toUpperCase());
    }

    public Anggota cariByNama(String nama) {
        if (nama == null) {
            return null;
        }
        for (Anggota a : data.values()) {
            if (a.getNama().equalsIgnoreCase(nama.trim())) {
                return a;
            }
        }
        return null;
    }

    public List<Anggota> semua() {
        return new ArrayList<>(data.values());
    }
}
