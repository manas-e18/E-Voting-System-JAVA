package com.evoting.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("E-Voting System");
        setSize(520,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("<html><center><b>E-VOTING SYSTEM</b><br><small>Secure & Simple Electronic Voting (Lab Demo)</small></center></html>", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2,2,12,12));
        center.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        JButton btnAdmin = new JButton("Admin Login");
        JButton btnVoterLogin = new JButton("Voter Login");
        JButton btnRegister = new JButton("Register Voter");
        JButton btnResults = new JButton("View Results");

        center.add(btnAdmin); center.add(btnVoterLogin); center.add(btnRegister); center.add(btnResults);
        add(center, BorderLayout.CENTER);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel hint = new JLabel("Note: Registration shows your Voter ID — store it safely.");
        foot.add(hint);
        add(foot, BorderLayout.SOUTH);

        btnAdmin.addActionListener(e -> new AdminLoginDialog(this).setVisible(true));
        btnVoterLogin.addActionListener(e -> new VoterLoginDialog(this).setVisible(true));
        btnRegister.addActionListener(e -> new RegisterDialog(this).setVisible(true));
        btnResults.addActionListener(e -> new ResultsDialog(this).setVisible(true));
    }
}
