package game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClassSelectionPanel extends JPanel {
    private JButton warriorButton;
    private JButton wizardButton;

    public ClassSelectionPanel(ActionListener warriorListener, ActionListener wizardListener) {
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel selectLabel = new JLabel("Select Your Class", SwingConstants.CENTER);
        selectLabel.setFont(new Font("Serif", Font.BOLD, 32));
        add(selectLabel);

        warriorButton = new JButton("Warrior");
        warriorButton.setFont(new Font("Serif", Font.PLAIN, 24));
        warriorButton.addActionListener(warriorListener);
        add(warriorButton);

        wizardButton = new JButton("Wizard");
        wizardButton.setFont(new Font("Serif", Font.PLAIN, 24));
        wizardButton.addActionListener(wizardListener);
        add(wizardButton);
    }
}
