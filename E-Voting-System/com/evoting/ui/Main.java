package com.evoting.ui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }
}
