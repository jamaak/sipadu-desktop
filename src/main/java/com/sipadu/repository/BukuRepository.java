package com.sipadu.repository;

import com.sipadu.model.Buku;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BukuRepository {
    private final Map<String, Buku> data = new LinkedHashMap<>();

    public void simpan(Buku buku) {
        data.put(buku.getId(), buku);
    }

    public Buku cari(String id) {
        return id == null ? null : data.get(id.trim().toUpperCase());
    }

    public List<Buku> semua() {
        return new ArrayList<>(data.values());
    }

    public void hapus(String id) {
        data.remove(id.trim().toUpperCase());
    }
}
