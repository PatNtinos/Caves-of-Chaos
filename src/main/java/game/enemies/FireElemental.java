package game.enemies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class FireElemental extends Enemy {

    private BufferedImage sprite;

    public FireElemental(int x, int y) {
        super(x, y, "Spider", 65, 10, 250);

        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/enemies/monster_elemental_fire.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Fire Elemental sprite!");
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

