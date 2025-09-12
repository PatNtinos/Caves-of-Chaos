package game.player;

import game.enemies.Enemy;

import java.util.List;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Wizard extends Player {

    private BufferedImage sprite;

    public Wizard(int startX, int startY, int tileSize) {
        super(startX, startY, tileSize);

        // Wizard stats per level
        int[] health =        {20, 40, 50, 55, 60, 80};
        int[] strength =      {3, 5, 8, 12, 14, 18};
        int[] intelligence =  {10, 20, 30, 40, 50, 70};
        int[] mana =          {30, 50, 70, 90, 110, 140};

        setClassStats(health, strength, intelligence, mana);

        try {
            sprite = ImageIO.read(getClass().getResource("/sprites/Player/npc_wizzard.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Wizard sprite!");
        }
    }
    @Override
    public void attack(List<Enemy> enemies, int dx, int dy) {

        int manaCost = 5; // define how much mana the attack costs

        if (getMana() < manaCost) {
            getMessagePanel().logMessage("Not enough mana to cast the spell!");
            return; // can't attack without enough mana
        }

        // Spend mana
        currentMana -= manaCost;

        // Ranged attack up to 3 tiles in the chosen direction
        for (int distance = 1; distance <= 3; distance++) {
            int targetX = x + dx * distance;
            int targetY = y + dy * distance;

            for (Enemy e : enemies) {
                if (e.getX() == targetX && e.getY() == targetY) {
                    int damage = getIntelligence(); // Scale with intelligence
                    e.takeDamage(damage);
                    getMessagePanel().logMessage(
                            "You blast the " + e.getName() + " for " + damage + " magic damage!"
                    );

                    /*if (!e.isAlive()) {
                        getMessagePanel().logMessage("You incinerated the " + e.getName() + "!");
                    }*/
                    return; // Stop after hitting the first enemy in the path
                }
            }
        }

        getMessagePanel().logMessage("Your spell hits nothing.");
    }
    @Override
    public void draw(Graphics g, int screenX, int screenY, int tileSize) {
        if (sprite != null) {
            g.drawImage(sprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillOval(screenX, screenY, tileSize, tileSize);
        }
    }
}
