package com.sipadu.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DesktopMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // pakai tampilan bawaan Java
            }
            new LoginFrame(new AppContext()).setVisible(true);
        });
    }
}
