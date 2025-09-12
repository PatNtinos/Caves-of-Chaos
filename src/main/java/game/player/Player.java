package game.player;

import game.AudioPlayer;
import game.Tile;
import game.enemies.Enemy;
import game.items.Inventory;
import game.items.Weapon;
import game.ui.MessagePanel;

import java.awt.*;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;




public abstract class Player {

    // Position Stats
    public int x, y;
    private final int tileSize;
    private int lastX, lastY;

    // Sprite
    private BufferedImage sprite;

    // RPG Stats
    private int level = 1;
    private int experience = 0;
    private final int maxLevel = 6;

    private int[] healthPerLevel;
    private int[] strengthPerLevel;
    private int[] intelligencePerLevel;
    private int[] manaPerLevel;

    private int currentHP;
    public int currentMana;

    private MessagePanel messagePanel;
    private Inventory inventory = new Inventory();

    // Leveling thresholds
    private final int[] levelThresholds = {0, 300, 900, 2700, 6500, 14000, 999999999,999999999};


    public Player(int startX, int startY, int tileSize) {
        this.x = startX;
        this.y = startY;
        this.tileSize = tileSize;
        this.lastX = x;
        this.lastY = y;

        try {
            sprite = ImageIO.read(getClass().getResource("/sprites/Player/npc_knight_blue.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load player sprite!");
        }

    }


    public abstract void draw(Graphics g, int screenX, int screenY, int tileSize);


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.lastX = x;
        this.lastY = y;
    }
    public void updateLastPosition() {
        this.lastX = this.x;
        this.lastY = this.y;
    }

    public boolean justSteppedOnStairs() {
        return !(lastX == x && lastY == y);
    }

    // --- RPG Methods ---

    public void setClassStats(int[] hp, int[] str, int[] intl, int[] mana) {
        this.healthPerLevel = hp;
        this.strengthPerLevel = str;
        this.intelligencePerLevel = intl;
        this.manaPerLevel = mana;

        this.currentHP = hp[0];
        this.currentMana = mana[0];
    }
    // --- Getters ---
    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getHp() {
        return currentHP;
    }

    public int getMaxHp() {

        int baseMaxHP = healthPerLevel[level - 1];
        Weapon weapon = inventory.getEquippedWeapon();
        if (weapon != null) {
            baseMaxHP += weapon.getHpBonus();
        }
        return baseMaxHP;
    }

    public int getMaxMana() {
        int baseMaxMana = manaPerLevel[level - 1];
        Weapon weapon = inventory.getEquippedWeapon();
        if (weapon != null) {
            baseMaxMana += weapon.getManaBonus();
        }
        return baseMaxMana;
    }

    public int getStrength() {

        int baseStrength = strengthPerLevel[level - 1];
        Weapon weapon = inventory.getEquippedWeapon();
        if (weapon != null) {
            baseStrength += weapon.getStrengthBonus();
        }
        return baseStrength;
    }

    public int getIntelligence() {

        int baseintelligence = intelligencePerLevel[level - 1];
        Weapon weapon = inventory.getEquippedWeapon();
        if (weapon != null) {
            baseintelligence += weapon.getIntelligenceBonus();
        }
        return baseintelligence;
    }

    public double getHpPercent() {
        return (double) getHp() / getMaxHp();
    }
    public int getMana() { return currentMana; }

    public void gainExperience(int xp) {
        experience += xp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (level < maxLevel && experience >= levelThresholds[level]) {
            levelUp();
        }
    }

    // --- Inventory ---
    public Inventory getInventory() {
        return inventory;
    }

    // Convenience methods to use in GamePanel:
    public boolean useHealthPotion() {
        return inventory.useHealthPotion(this);
    }

    public boolean useManaPotion() {
        return inventory.useManaPotion(this);
    }

    // --- Methods ---
    public void move(int dx, int dy, Tile[][] map, List<Enemy> enemies) {
        int newX = x + dx;
        int newY = y + dy;

        // Check bounds and collisions
        if (newY >= 0 && newY < map.length &&
                newX >= 0 && newX < map[0].length &&
                map[newY][newX].type != Tile.Type.WALL) {

            // Check if an enemy is on that tile
            for (Enemy enemy : enemies) {
                if (enemy.getX() == newX && enemy.getY() == newY) {
                    return;
                }
            }
            lastX = x;
            lastY = y;
            x = newX;
            y = newY;
        }
    }

    private void levelUp() {
        level++;
        AudioPlayer.playSound("/sound/powerup.wav");
        // Update stats according to arrays
        currentHP = healthPerLevel[level - 1]; // Heal fully on level up
        currentMana = manaPerLevel[level - 1];
        //strengthPerLevel[level - 1] = strengthPerLevel[level]; // I don't even know why i added this line....
        // it threw out of bounds error so I commented it out

        messagePanel.logMessage("Leveled up to level " + level + "!");
    }

    public void takeDamage(int amount) {
        AudioPlayer.playSound("/sound/takedamage.wav");
        currentHP -= amount;
        if (currentHP < 0) {
            currentHP = 0;
        }
    }

    public void rest() {
        int healAmount = getMaxHp() / 20;       // Heal 5% of max HP
        int manaRegen = getMaxMana() / 20;      // Restore 5% of max Mana

        currentHP += healAmount;
        currentMana += manaRegen;

        if (currentHP > getMaxHp()) {
            currentHP = getMaxHp();
        }

        if (currentMana > getMaxMana()) {
            currentMana = getMaxMana();
        }
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public void attack(List<Enemy> enemies, int dx, int dy) {
        // Default = melee 1 tile away in (dx, dy) direction
        int targetX = x + dx;
        int targetY = y + dy;

        for (Enemy e : enemies) {
            if (e.getX() == targetX && e.getY() == targetY) {
                int damage = getStrength();
                e.takeDamage(damage);
                AudioPlayer.playSound("/sound/hitmonster.wav");
                messagePanel.logMessage("You hit the " + e.getName() + " for " + damage + " damage!");
                if (!e.isAlive()) {
                    messagePanel.logMessage("You killed the " + e.getName() + "!");
                }
                return;
            }
        }

        messagePanel.logMessage("No enemy to attack there.");
    }

    // ---- Message Panel ----
    public void setMessagePanel(MessagePanel messagePanel) {
        this.messagePanel = messagePanel;
    }
    public MessagePanel getMessagePanel() {
        return messagePanel;
    }

    public void heal(int amount) {
        if (currentHP >= getMaxHp()) {
            return; // Already at full HP
        }
        currentHP += amount;
        if (currentHP > getMaxHp()) {
            currentHP = getMaxHp();
        }
    }

    public void regenerateMana(int amount) {
        if (currentMana >= getMaxMana()) {
            return; // Already full
        }
        currentMana += amount;
        if (currentMana > getMaxMana()) {
            currentMana = getMaxMana();
        }
    }
}

