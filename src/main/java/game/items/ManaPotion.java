package game.items;

import game.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ManaPotion extends Potion {

    private static BufferedImage potionSprite;

    static {
        try {
            potionSprite = ImageIO.read(ManaPotion.class.getResource("/sprites/items/mana_pot.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load mana potion sprite!");
        }
    }

    @Override
    public String getName() {
        return "Mana Potion";
    }

    @Override
    public void applyEffect(Player player) {
        player.regenerateMana(20);
        player.getMessagePanel().logMessage("You used a Mana Potion!");
    }

    @Override
    public void render(Graphics g, int screenX, int screenY, int tileSize) {
        if (potionSprite != null) {
            g.drawImage(potionSprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            // fallback: cyan oval if sprite fails
            g.setColor(Color.CYAN);
            g.fillOval(screenX + 8, screenY + 8, tileSize - 16, tileSize - 16);
        }
    }
}
