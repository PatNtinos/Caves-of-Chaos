package game.enemies;

import game.items.Diamond;
import game.player.Player;
import game.Tile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SerpentOfChaos extends Enemy {

    private BufferedImage sprite;

    public SerpentOfChaos(int x, int y) {
        super(x, y, "Serpent Of Chaos", 500, 30, 500);

        try {
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/enemies/serpent.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Serpent sprite!");
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

    @Override
    public void takeDamage(int dmg) {
        super.takeDamage(dmg);
        if (!isAlive()) {
            //dropDiamond();
        }
    }

    /*private void dropDiamond() {
        messagePanel.logMessage("💎 The Serpent of Chaos dropped a Diamond!");

    }*/
}