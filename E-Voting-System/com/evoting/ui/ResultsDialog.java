package com.evoting.ui;

import com.evoting.dao.DataStore;
import com.evoting.model.Candidate;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ResultsDialog extends JDialog {
    public ResultsDialog(JFrame parent) {
        super(parent, "Election Results", true);
        setSize(700,500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("<html><center><b>Election Results</b><br><small>Graphical representation of votes</small></center></html>", SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(header, BorderLayout.NORTH);

        Map<String,Integer> votes = DataStore.getInstance().getVotes();
        List<Candidate> candidates = new ArrayList<>(DataStore.getInstance().getAllCandidates());

        JPanel center = new JPanel(new BorderLayout());
        center.add(new ChartPanel(candidates, votes), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Close"); close.addActionListener(e -> dispose());
        south.add(close);
        add(south, BorderLayout.SOUTH);
    }

    static class ChartPanel extends JPanel {
        private final List<Candidate> candidates;
        private final Map<String,Integer> votes;
        public ChartPanel(List<Candidate> candidates, Map<String,Integer> votes) {
            this.candidates = candidates;
            this.votes = votes;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (candidates.isEmpty()) {
                g.drawString("No candidates to show", 20, 20);
                return;
            }
            int width = getWidth(), height = getHeight();
            int padding = 60;
            int chartWidth = width - 2*padding;
            int chartHeight = height - 2*padding;

            int maxVotes = 1;
            for (Candidate c : candidates) maxVotes = Math.max(maxVotes, votes.getOrDefault(c.getId(), 0));

            int barWidth = Math.max(20, (chartWidth / candidates.size()) - 20);
            int x = padding + 10;
            int i = 0;
            for (Candidate c : candidates) {
                int v = votes.getOrDefault(c.getId(), 0);
                int barHeight = (int)((double)v / maxVotes * (chartHeight - 40));
                int y = padding + (chartHeight - barHeight);

                float hue = (i * 0.12f) % 1.0f;
                g.setColor(Color.getHSBColor(hue, 0.6f, 0.7f));
                g.fillRect(x, y, barWidth, barHeight);

                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, barWidth, barHeight);

                g.setColor(Color.BLACK);
                String label = c.getName() + " (" + v + ")";
                FontMetrics fm = g.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                int lx = x + (barWidth - labelWidth)/2;
                int ly = y + barHeight + 18;
                g.drawString(label, Math.max(lx, x-10), Math.min(ly, getHeight() - 10));

                x += barWidth + 20; i++;
            }

            g.setColor(Color.BLACK);
            g.drawLine(padding, padding + chartHeight, padding + chartWidth, padding + chartHeight);
            g.drawLine(padding, padding, padding, padding + chartHeight);
        }
    }
}
