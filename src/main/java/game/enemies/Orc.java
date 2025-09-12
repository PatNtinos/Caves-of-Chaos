package game.enemies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Orc extends Enemy {

    private BufferedImage sprite;

    public Orc(int x, int y) {
        super(x, y, "Orc", 80, 18, 400);

        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/enemies/monster_ogre.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Orc sprite!");
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

