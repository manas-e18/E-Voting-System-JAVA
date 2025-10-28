package com.evoting.ui;

import com.evoting.dao.DataStore;
import com.evoting.model.Candidate;
import com.evoting.model.Voter;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Voter panel that lists candidates and allows a single vote.
 */
public class VoterPanelDialog extends JDialog {
    public VoterPanelDialog(JFrame parent, Voter voter) {
        super(parent, "Cast Your Vote - " + voter.getName(), true);
        setSize(520,360);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("<html><center><b>Cast Your Vote</b><br><small>Choose one candidate and submit</small></center></html>", SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(header, BorderLayout.NORTH);

        java.util.List<Candidate> candidates = new ArrayList<>(DataStore.getInstance().getAllCandidates());
        if (candidates.isEmpty()) {
            add(new JLabel("No candidates available. Contact admin.", SwingConstants.CENTER), BorderLayout.CENTER);
            JButton close = new JButton("Close");
            close.addActionListener(e -> dispose());
            add(close, BorderLayout.SOUTH);
            return;
        }

        JPanel center = new JPanel(new GridLayout(0,1,6,6));
        ButtonGroup bg = new ButtonGroup();
        Map<JRadioButton, String> map = new HashMap<>();
        for (Candidate c : candidates) {
            JRadioButton rb = new JRadioButton(c.getName() + " [" + c.getId() + "]");
            bg.add(rb);
            center.add(rb);
            map.put(rb, c.getId());
        }
        add(new JScrollPane(center), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submit = new JButton("Submit Vote");
        south.add(submit);
        add(south, BorderLayout.SOUTH);

        submit.addActionListener(e -> {
            String chosen = null;
            for (JRadioButton rb : map.keySet()) if (rb.isSelected()) chosen = map.get(rb);
            if (chosen == null) { JOptionPane.showMessageDialog(this, "Select a candidate"); return; }
            DataStore.getInstance().castVote(chosen);
            voter.setHasVoted(true);
            DataStore.getInstance().updateVoter(voter);
            JOptionPane.showMessageDialog(this, "Vote cast successfully. Thank you!");
            dispose();
        });
    }
}
