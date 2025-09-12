package game.enemies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Rat extends Enemy {

    private BufferedImage sprite;

    public Rat(int x, int y) {
        super(x, y, "Rat", 20, 7, 80);

        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/enemies/rat.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Rat sprite!");
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


