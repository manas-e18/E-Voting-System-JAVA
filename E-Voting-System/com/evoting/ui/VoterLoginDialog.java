package com.evoting.ui;

import com.evoting.dao.DataStore;
import com.evoting.model.Voter;
import com.evoting.util.Utils;

import javax.swing.*;
import java.awt.*;

public class VoterLoginDialog extends JDialog {
    public VoterLoginDialog(JFrame parent) {
        super(parent, "Voter Login", true);
        setSize(380,200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(3,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.add(new JLabel("Voter ID:")); JTextField txtId = new JTextField(); p.add(txtId);
        p.add(new JLabel("Password:")); JPasswordField pf = new JPasswordField(); p.add(pf);
        p.add(new JLabel()); JButton btn = new JButton("Login"); p.add(btn);
        add(p, BorderLayout.CENTER);

        btn.addActionListener(e -> {
            String id = txtId.getText().trim();
            String pass = new String(pf.getPassword()).trim();
            if (id.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter credentials"); return; }
            Voter v = DataStore.getInstance().getVoterById(id);
            if (v == null) { JOptionPane.showMessageDialog(this, "Voter not found"); return; }
            if (!v.getPasswordHash().equals(Utils.hash(pass))) { JOptionPane.showMessageDialog(this, "Wrong password"); return; }
            if (v.hasVoted()) { JOptionPane.showMessageDialog(this, "You have already voted. Thank you!"); return; }
            dispose();
            new VoterPanelDialog((JFrame)parent, v).setVisible(true);
        });
    }
}
