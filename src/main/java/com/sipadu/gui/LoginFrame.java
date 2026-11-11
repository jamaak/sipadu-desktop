package com.sipadu.gui;

import com.sipadu.model.Staf;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

class LoginFrame extends JFrame {
    private final AppContext ctx;

    LoginFrame(AppContext ctx) {
        super("SIPADU");
        this.ctx = ctx;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 24));
        root.setBackground(Ui.BG);
        root.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        JLabel judul = new JLabel("SIPADU", SwingConstants.CENTER);
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 34f));
        judul.setForeground(Ui.PRIMARY);
        JLabel sub = new JLabel("Sistem Perpustakaan Terpadu", SwingConstants.CENTER);
        sub.setForeground(Color.GRAY);
        JPanel atas = new JPanel(new GridLayout(2, 1));
        atas.setOpaque(false);
        atas.add(judul);
        atas.add(sub);

        JPanel tombol = new JPanel(new GridLayout(3, 1, 0, 12));
        tombol.setOpaque(false);
        for (Peran p : Peran.values()) {
            JButton b = Ui.tombol("Masuk sebagai " + p.label);
            b.setPreferredSize(new Dimension(300, 48));
            b.addActionListener(e -> masuk(p));
            tombol.add(b);
        }

        root.add(atas, BorderLayout.NORTH);
        root.add(tombol, BorderLayout.CENTER);
        setContentPane(root);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void masuk(Peran peran) {
        String nama = "Tamu";
        if (peran == Peran.ADMIN) {
            if (!login(ctx.admin)) {
                return;
            }
            nama = ctx.admin.getNama();
        } else if (peran == Peran.KEPALA) {
            if (!login(ctx.kepala)) {
                return;
            }
            nama = ctx.kepala.getNama();
        }
        new MainFrame(ctx, peran, nama).setVisible(true);
        dispose();
    }

    private boolean login(Staf staf) {
        JTextField user = new JTextField(16);
        JPasswordField pass = new JPasswordField(16);
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Username"));
        form.add(user);
        form.add(new JLabel("Password"));
        form.add(pass);
        int ok = JOptionPane.showConfirmDialog(this, form, "Login", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) {
            return false;
        }
        if (staf.login(user.getText().trim(), new String(pass.getPassword()))) {
            return true;
        }
        Ui.error(this, "Username atau password salah.");
        return false;
    }
}
