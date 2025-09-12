package game.enemies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Goblin extends Enemy {

    private BufferedImage sprite;

    public Goblin(int x, int y) {
        super(x, y, "Goblin", 30, 5, 100); // x, y, name, hp, strength, xpReward

        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/enemies/First_Goblin.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Goblin sprite!");
        }
    }
    @Override
    public void draw(Graphics g, int screenX, int screenY, int tileSize) {
        if (sprite != null) {
            g.drawImage(sprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(screenX, screenY, tileSize, tileSize);
        }
    }
}

