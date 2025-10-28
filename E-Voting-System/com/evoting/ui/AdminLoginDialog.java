package com.evoting.ui;

import com.evoting.dao.DataStore;

import javax.swing.*;
import java.awt.*;

/**
 * Admin login dialog. Added a "Factory Reset" button that will wipe the system.
 * The reset requires admin password confirmation and an explicit final confirmation.
 */
public class AdminLoginDialog extends JDialog {
    // Demo admin credential (change for real use)
    private static final String ADMIN_PASS = "admin123";

    public AdminLoginDialog(JFrame parent) {
        super(parent, "Admin Login", true);
        setSize(420,200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(2,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        p.add(new JLabel("Password:"));
        JPasswordField pf = new JPasswordField();
        p.add(pf);

        // Two-button row: Login, Factory Reset
        JButton btnLogin = new JButton("Login");
        JButton btnReset = new JButton("Factory Reset");

        p.add(btnLogin);
        p.add(btnReset);

        add(p, BorderLayout.CENTER);

        // Standard admin login: opens AdminPanel on success
        btnLogin.addActionListener(e -> {
            String pass = new String(pf.getPassword());
            if (ADMIN_PASS.equals(pass)) {
                dispose();
                new AdminPanel((JFrame) parent).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Wrong password");
            }
        });

        // Factory Reset flow
        btnReset.addActionListener(e -> {
            // 1) require admin password
            String pass = JOptionPane.showInputDialog(this, "Enter admin password to allow Factory Reset:");
            if (pass == null) return; // cancelled
            if (!ADMIN_PASS.equals(pass)) {
                JOptionPane.showMessageDialog(this, "Wrong admin password. Reset aborted.");
                return;
            }

            // 2) explicit confirmation (double-check)
            int confirm = JOptionPane.showConfirmDialog(this,
                    "FACTORY RESET will DELETE ALL VOTERS, CANDIDATES and VOTES.\n" +
                    "This action is irreversible. Are you sure you want to continue?",
                    "Confirm Factory Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;

            // 3) final extra confirmation (type word)
            String type = JOptionPane.showInputDialog(this, "Type the word RESET to confirm:");
            if (type == null || !type.equals("RESET")) {
                JOptionPane.showMessageDialog(this, "Reset not confirmed. Aborted.");
                return;
            }

            // 4) perform reset
            try {
                DataStore.getInstance().resetAll();
                JOptionPane.showMessageDialog(this, "Factory Reset completed.\nThe system is now fresh (no voters, no candidates, no votes).");
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Factory Reset failed: " + ex.getMessage());
            }
        });
    }
}
