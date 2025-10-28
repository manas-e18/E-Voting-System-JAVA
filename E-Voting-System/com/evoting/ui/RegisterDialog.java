package com.evoting.ui;

import com.evoting.dao.DataStore;
import com.evoting.model.Voter;
import com.evoting.util.Utils;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {
    public RegisterDialog(JFrame parent) {
        super(parent, "Register Voter", true);
        setSize(380,220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(3,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.add(new JLabel("Full Name:")); JTextField txtName = new JTextField(); p.add(txtName);
        p.add(new JLabel("Password:")); JPasswordField pf = new JPasswordField(); p.add(pf);
        p.add(new JLabel()); JButton btn = new JButton("Register"); p.add(btn);
        add(p, BorderLayout.CENTER);

        btn.addActionListener(e -> {
            String name = txtName.getText().trim();
            String pass = new String(pf.getPassword()).trim();
            if (name.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter name and password"); return; }
            Voter v = new Voter(name, Utils.hash(pass));
            DataStore.getInstance().addVoter(v);
            JOptionPane.showMessageDialog(this, "Registered successfully.\nYour Voter ID: " + v.getId() + "\nKeep it safe to login and vote.");
            dispose();
        });
    }
}
