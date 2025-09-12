package game.items;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import game.player.Player;

public class HealthPotion extends Potion {

    private static BufferedImage potionSprite;

    static {
        try {
            potionSprite = ImageIO.read(HealthPotion.class.getResource("/sprites/items/health_pot.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load health potion sprite!");
        }
    }

    @Override
    public String getName() {
        return "Health Potion";
    }

    @Override
    public void applyEffect(Player player) {
        player.heal(20);
        player.getMessagePanel().logMessage("You used a Health Potion!");
    }

    @Override
    public void render(Graphics g, int screenX, int screenY, int tileSize) {
        if (potionSprite != null) {
            g.drawImage(potionSprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            // fallback: pink oval if sprite fails
            g.setColor(Color.PINK);
            g.fillOval(screenX + 8, screenY + 8, tileSize - 16, tileSize - 16);
        }
    }
}
