package game;

import game.items.Item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Tile {
    // Types of Tiles
    public enum Type {
        FLOOR, WALL, STAIRS_UP, STAIRS_DOWN
    }

    //Tile Fields
    public Type type;
    public boolean walkable;
    public Color color;
    public boolean visited = false;
    private Item item = null;

    // Sprites
    private BufferedImage sprite;

    public Tile(Type type) {
        this.type = type;
    try{
        // Tile properties for each type
        switch (type) {
            case FLOOR:
                walkable = true;
                color = Color.DARK_GRAY;
                sprite = ImageIO.read(getClass().getResource("/sprites/Tiles/floor_plain.png"));
                break;
            case WALL:
                walkable = false;
                color = Color.GRAY;
                sprite = ImageIO.read(getClass().getResource("/sprites/Tiles/wall_center.png"));
                break;
            case STAIRS_UP:
                walkable = true;
                color = Color.BLUE;
                sprite = ImageIO.read(getClass().getResource("/sprites/Tiles/Floor_ladder.png"));
                break;
            case STAIRS_DOWN:
                walkable = true;
                color = Color.GREEN;
                sprite = ImageIO.read(getClass().getResource("/sprites/Tiles/Floor_ladder.png"));
                break;
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Failed to load tile sprite for " + type);
    }
    }
    //To check item on Tile
    public Item getItem() {
        return item;
    }
    //To set item on Tile
    public void setItem(Item item) {
        this.item = item;
    }
    //To remove item on Tile
    public void removeItem() {
        this.item = null;
    }

    // --- Rendering method ---
    public void render(Graphics g, int x, int y, int tileSize) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, tileSize, tileSize, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, tileSize, tileSize);
        }
    }
}
