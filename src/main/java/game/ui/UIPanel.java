package game.ui;

import game.map.MapManager;
import game.player.Player;

import javax.swing.*;
import java.awt.*;

public class UIPanel extends JPanel {
    private Player player;
    private MapManager mapManager;

    public UIPanel(Player player) {
        this.player = player;
        setPreferredSize(new Dimension(200, 600)); // width = 200
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("Class: " + player.getClass().getSimpleName(), 10, 20);
        g.drawString("HP: " + player.getHp() + "/" + player.getMaxHp(), 10, 40);
        g.drawString("Mana: " + player.getMana() + "/" + player.getMaxMana(), 10, 60);
        g.drawString("Level: " + player.getLevel(), 10, 80);
        g.drawString("XP: " + player.getExperience(), 10, 100);
        g.drawString("Strength: " + player.getStrength(), 10, 120);
        g.drawString("Intelligence: " + player.getIntelligence(), 10, 140);
        g.drawString("Health Potions: " + player.getInventory().getHealthPotionCount(), 10, 180);
        g.drawString("Mana Potions: " + player.getInventory().getManaPotionCount(), 10, 200);
        //g.drawString("Cave " + (mapManager.getCurrentLevelIndex() + 1), 10, 780);
        g.drawString("Move: WASD", 10, 800);
        g.drawString("Attack: SPACE and WASD", 10, 820);
        g.drawString("HP POT: H /Mana POT: M /Rest: R", 10, 840);
        g.drawString("Change Weapon: P", 10, 860);

        // Draw equipped weapon info
        g.drawString("Equipped Weapon:", 10, 240);
        if (player.getInventory().getEquippedWeapon() != null) {
            var w = player.getInventory().getEquippedWeapon();
            g.drawString("Name: " + w.getName(), 10, 260);
            g.drawString("Attack: +" + w.getStrengthBonus(), 10, 280);
            g.drawString("Intelligence: +" + w.getIntelligenceBonus(), 10, 300);
            g.drawString("HP: +" + w.getHpBonus(), 10, 320);
            g.drawString("Mana: +" + w.getManaBonus(), 10, 340);
        } else {
            g.drawString("None", 10, 260);
        }
        repaint();
    }
}