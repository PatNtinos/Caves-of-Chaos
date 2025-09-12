package game.player;

import game.enemies.Enemy;

import java.util.List;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Warrior extends Player {

    private BufferedImage sprite;

    public Warrior(int startX, int startY, int tileSize) {
        super(startX, startY, tileSize);

        // Warrior stats per level
        int[] health =        {30, 60, 80, 90, 100, 140};
        int[] strength =      {10, 20, 25, 30, 35, 45};
        int[] intelligence =  {2, 5, 7, 8, 11, 13};
        int[] mana =          {0, 0, 0, 0, 0, 0};

        setClassStats(health, strength, intelligence, mana);
        try {
            sprite = ImageIO.read(getClass().getResource("/sprites/Player/npc_knight_blue.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Warrior sprite!");
        }
    }

    @Override
    public void attack(List<Enemy> enemies, int dx, int dy) {
        super.attack(enemies, dx, dy); // simple melee attack
    }
    @Override
    public void draw(Graphics g, int screenX, int screenY, int tileSize) {
        if (sprite != null) {
            g.drawImage(sprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillOval(screenX, screenY, tileSize, tileSize);
        }
    }

}
