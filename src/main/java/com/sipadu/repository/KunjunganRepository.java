package com.sipadu.repository;

import com.sipadu.model.Kunjungan;

import java.util.ArrayList;
import java.util.List;

public class KunjunganRepository {
    private final List<Kunjungan> data = new ArrayList<>();

    public void simpan(Kunjungan k) {
        data.add(k);
    }

    public List<Kunjungan> semua() {
        return new ArrayList<>(data);
    }
}
