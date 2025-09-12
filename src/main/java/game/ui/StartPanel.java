package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    private JButton playButton;

    public StartPanel(ActionListener playListener) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Caves of Chaos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        add(titleLabel, BorderLayout.CENTER);

        playButton = new JButton("Play");
        playButton.setFont(new Font("Serif", Font.PLAIN, 24));
        playButton.addActionListener(playListener);
        JPanel btnPanel = new JPanel();
        btnPanel.add(playButton);
        add(btnPanel, BorderLayout.SOUTH);
    }
}
