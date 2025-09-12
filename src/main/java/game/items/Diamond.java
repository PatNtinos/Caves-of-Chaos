package game.items;

import game.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Diamond extends Item {

    private static BufferedImage diamondSprite;

    static {
        try {
            diamondSprite = ImageIO.read(Diamond.class.getResource("/sprites/items/diamond.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load diamond sprite!");
        }
    }
    @Override
    public String getName() {
        return "Diamond";
    }

    @Override
    public void applyEffect(Player player) {

    }

    @Override
    public boolean isConsumable() {
        return true;
    }

    @Override
    public void render(Graphics g, int screenX, int screenY, int tileSize) {
        if (diamondSprite != null) {
            g.drawImage(diamondSprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            // Fallback white circle if sprite fails
            g.setColor(Color.WHITE);
            g.fillOval(screenX + 8, screenY + 8, tileSize - 16, tileSize - 16);
        }
    }
}