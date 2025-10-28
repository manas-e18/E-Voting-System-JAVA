package com.evoting.ui;

import com.evoting.dao.DataStore;
import com.evoting.model.Candidate;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AdminPanel extends JDialog {
    public AdminPanel(JFrame parent) {
        super(parent, "Admin Panel", true);
        setSize(700,450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add Candidate");
        JButton btnResults = new JButton("View Results");
        JButton btnReset = new JButton("Reset Election");
        top.add(btnAdd); top.add(btnResults); top.add(btnReset);
        add(top, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        refreshCandidates(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Candidate name:");
            if (name != null && !name.trim().isEmpty()) {
                DataStore.getInstance().addCandidate(new Candidate(name.trim()));
                refreshCandidates(model);
            }
        });

        btnResults.addActionListener(e -> {
            ResultsDialog rd = new ResultsDialog((JFrame)parent);
            rd.setVisible(true);
        });

        btnReset.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this, "Reset election and clear votes?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                DataStore.getInstance().resetElection();
                JOptionPane.showMessageDialog(this, "Election reset");
            }
        });
    }

    private void refreshCandidates(DefaultListModel<String> model) {
        model.clear();
        for (Candidate c : DataStore.getInstance().getAllCandidates()) model.addElement(c.toString());
    }
}
