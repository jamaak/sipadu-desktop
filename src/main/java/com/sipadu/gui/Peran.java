package com.sipadu.gui;

enum Peran {
    PENGUNJUNG("Pengunjung"),
    ADMIN("Admin"),
    KEPALA("Kepala Operasional");

    final String label;

    Peran(String label) {
        this.label = label;
    }
}
