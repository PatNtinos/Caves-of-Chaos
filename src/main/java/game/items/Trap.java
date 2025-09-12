package game.items;

import game.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Trap extends Item {
    private int baseDamage;
    private int mapLevel;
    private static BufferedImage trapSprite;

    static {
        try {
            trapSprite = ImageIO.read(Trap.class.getResource("/sprites/items/weapon_bomb.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load trap sprite!");
        }
    }

    public Trap(/*int x, int y,*/int baseDamage/*, int mapLevel*/) {
        //this.x = x;
        //this.y = y;
        this.baseDamage = baseDamage;
        this.mapLevel = mapLevel;
    }

    @Override
    public String getName() {
        return "Trap";
    }

    @Override
    public void applyEffect(Player player) {
        int damage = baseDamage * (mapLevel + 1);
        player.takeDamage(damage);
        player.getMessagePanel().logMessage("You stepped on a trap and took " + damage + " damage!");
    }

    @Override
    public boolean isConsumable() {

        return true; // Disappears after triggering
    }

    @Override
    public void render(Graphics g, int screenX, int screenY, int tileSize) {
        if (trapSprite != null) {
            g.drawImage(trapSprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            // fallback: magenta square if sprite fails
            g.setColor(Color.MAGENTA);
            g.fillRect(screenX + 4, screenY + 4, tileSize - 8, tileSize - 8);
        }
    }
}
