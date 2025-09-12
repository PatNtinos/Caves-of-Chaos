package game.enemies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Golem extends Enemy {
    private BufferedImage sprite;

    public Golem(int x, int y) {
        super(x, y, "Golem", 200, 6, 500);

        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/enemies/golem.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Golem sprite!");
        }
    }
    @Override
    public void draw(Graphics g, int screenX, int screenY, int tileSize) {
        if (sprite != null) {
            g.drawImage(sprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillOval(screenX, screenY, tileSize, tileSize);
        }
    }
}


