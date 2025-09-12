
package game;

import game.player.Player;
import game.player.Warrior;
import game.player.Wizard;
import game.ui.ClassSelectionPanel;
import game.ui.MessagePanel;
import game.ui.StartPanel;
import game.ui.UIPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static JFrame window;
    private static StartPanel startPanel; //The first screen
    private static ClassSelectionPanel classSelectionPanel; // The selection screen

    private static GamePanel gamePanel; // Core game area
    public static UIPanel uiPanel; // Stats area at right
    private static MessagePanel messagePanel; // Event log at bottom

    public static void main(String[] args) {

        // Window Creation
        window = new JFrame("Caves of Chaos");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        //Start Panel with play button
        startPanel = new StartPanel(e -> showClassSelectionPanel());
        window.add(startPanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static void showClassSelectionPanel() {
        window.remove(startPanel);

        classSelectionPanel = new ClassSelectionPanel(
                e -> startGame(new Warrior(1, 1, 32)),
                e -> startGame(new Wizard(1, 1, 32))
        );
        window.add(classSelectionPanel);

        window.revalidate();
        window.repaint();
        window.pack();
        window.setLocationRelativeTo(null);
    }

    private static void startGame(Player player) {
        window.remove(classSelectionPanel);

        gamePanel = new GamePanel(player);
        uiPanel = new UIPanel(player);
        messagePanel = new MessagePanel();


        gamePanel.setMessagePanel(messagePanel);
        gamePanel.setUiPanel(uiPanel);

        window.setLayout(new BorderLayout());
        window.add(gamePanel, BorderLayout.CENTER);
        gamePanel.requestFocusInWindow();
        window.add(uiPanel, BorderLayout.EAST);
        window.add(messagePanel, BorderLayout.SOUTH);

        window.revalidate();
        window.repaint();
        window.pack();
        window.setLocationRelativeTo(null);
    }
}
