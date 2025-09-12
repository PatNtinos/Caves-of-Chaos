package game.enemies;

import game.Tile;
import game.player.Player;
import game.ui.MessagePanel;

import java.awt.*;
import java.util.List;

public abstract class Enemy {

    // Position
    protected int x;
    protected int y;

    // Stats
    protected String name;
    protected int maxHp;
    protected int currentHp;
    protected int strength;
    protected int xpReward;

    private MessagePanel messagePanel;

    public Enemy(int x, int y, String name, int hp, int strength, int xpReward) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.maxHp = hp;
        this.currentHp = hp;
        this.strength = strength;
        this.xpReward = xpReward;
    }


    // Getters
    public String getName() {
        return name;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getStrength() {
        return strength;
    }

    public int getExpReward() {
        return xpReward;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setMessagePanel(MessagePanel messagePanel) {
        this.messagePanel = messagePanel;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void takeDamage(int dmg) {
        currentHp -= dmg;
        if (currentHp < 0) currentHp = 0;
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    // Returns an attack value with a slight randomness
    public int doAttack() {
        return strength + (int)(Math.random() * 3);
    }

    public void attack(Player player) {
        int damage = doAttack();
        player.takeDamage(damage);
        messagePanel.logMessage(name + " attacks the player for " + damage + " damage!");
    }

    // Decreases the distance with x-axis or y-axis
    public void moveTowardPlayer(Player player, Tile[][] map, List<Enemy> enemies) {
        int dx = Integer.compare(player.x, this.x);
        int dy = Integer.compare(player.y, this.y);

        int distX = Math.abs(player.x - this.x);
        int distY = Math.abs(player.y - this.y);

        // Try to move in the direction that reduces the greater distance
        if (distX >= distY) {
            if (!tryMove(this.x + dx, this.y, map, enemies, player)) {
                tryMove(this.x, this.y + dy, map, enemies, player);
            }
        } else {
            if (!tryMove(this.x, this.y + dy, map, enemies, player)) {
                tryMove(this.x + dx, this.y, map, enemies, player);
            }
        }
    }

    // Checking for collisions and spaces enemies can move
    private boolean tryMove(int newX, int newY, Tile[][] map, List<Enemy> enemies, Player player) {
        if (newY < 0 || newY >= map.length || newX < 0 || newX >= map[0].length) return false;
        if (map[newY][newX].type == Tile.Type.WALL) return false;

        for (Enemy other : enemies) {
            if (other != this && other.getX() == newX && other.getY() == newY) {
                return false;
            }
        }

        if (player.x == newX && player.y == newY) {
            return false;
        }

        this.x = newX;
        this.y = newY;
        return true;
    }

    public void draw(Graphics g, int screenX, int screenY, int tileSize) {
        g.setColor(Color.RED);
        g.fillOval(screenX, screenY, tileSize, tileSize);
    }
}
