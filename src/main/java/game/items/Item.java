package game.items;

import java.awt.*;
import game.player.Player;

public abstract class Item {
    public int x, y;
    public abstract String getName();
    public abstract void applyEffect(Player player);
    public abstract boolean isConsumable(); // For pickup if you walk on it logic
    public abstract void render(Graphics g, int screenX, int screenY, int tileSize);


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() { return x; }
    public int getY() { return y; }
}