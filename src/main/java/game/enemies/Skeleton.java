package game.enemies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Skeleton extends Enemy {

    private BufferedImage sprite;

    public Skeleton(int x, int y) {
        super(x, y, "Skeleton", 60, 15, 300);

        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/enemies/skeleton.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Skeleton sprite!");
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

