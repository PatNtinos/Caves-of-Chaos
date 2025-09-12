package game.ui;

import game.player.Player;
import game.player.Warrior;
import game.player.Wizard;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectionWindow extends JFrame {

    public CharacterSelectionWindow() {
        setTitle("Select Your Class");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton warriorButton = new JButton("Warrior");
        JButton wizardButton = new JButton("Wizard");

        warriorButton.addActionListener(e -> {
            Player player = new Warrior(5, 5, 32);
            startGame(player);
        });

        wizardButton.addActionListener(e -> {
            Player player = new Wizard(5, 5, 32);
            startGame(player);
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.add(warriorButton);
        panel.add(wizardButton);

        add(panel);
        setVisible(true);
    }

    private void startGame(Player player) {
        dispose(); // close selection window

        // Now start your actual game and pass the player object
        // Example:
        //new GamePanel(player); // You should create this to launch your game logic
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CharacterSelectionWindow::new);
    }
}
