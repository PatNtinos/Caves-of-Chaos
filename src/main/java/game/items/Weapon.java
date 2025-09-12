package game.items;

import game.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Weapon extends Item {
    private String name;
    private int strengthBonus;
    private int intelligenceBonus;
    private int hpBonus;
    private int manaBonus;

    private static BufferedImage weaponSprite;
    static {
        try {
            weaponSprite = ImageIO.read(Weapon.class.getResource("/sprites/items/chest_golden_open_full.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load weapon sprite!");
        }
    }

    public Weapon(String name, int strengthBonus, int intelligenceBonus, int hpBonus, int manaBonus) {
        this.name = name;
        this.strengthBonus = strengthBonus;
        this.intelligenceBonus = intelligenceBonus;
        this.hpBonus = hpBonus;
        this.manaBonus = manaBonus;
    }

    @Override
    public String getName() {
        return name;
    }
    public int getStrengthBonus() {
        return strengthBonus;
    }

    public int getIntelligenceBonus() {
        return intelligenceBonus;
    }

    public int getHpBonus() {
        return hpBonus;
    }

    public int getManaBonus() {
        return manaBonus;
    }
    @Override
    public void applyEffect(Player player) {

    }

    @Override
    public boolean isConsumable() {
        return false;
    }

    @Override
    public void render(Graphics g, int screenX, int screenY, int tileSize) {
        if (weaponSprite != null) {
            g.drawImage(weaponSprite, screenX, screenY, tileSize, tileSize, null);
        } else {
            // fallback rectangle if sprite fails
            g.setColor(Color.ORANGE);
            g.fillRect(screenX + 8, screenY + 8, tileSize - 16, tileSize - 16);
        }
    }
    }



