package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

import java.util.Random;

import game.enemies.*;
import game.items.*;
import game.map.MapManager;
import game.player.Player;

import game.player.Wizard;
import game.ui.MessagePanel;
import game.ui.UIPanel;

import static game.Main.uiPanel;
import javax.swing.Timer;


/* --- FIXES OR FEATURES ---

1. remove final stair (except if you extend the game)
2. Ai not following if there is a wall between you
4. More items like armor or mini health pot
5. Rest is too forgiving
6. Make the damage more random





*/
/* ----------- CREDITS --------------

Thanks to Deep Dive Game Studio for most of the game's sprites URL: https://deepdivegamestudio.itch.io/
Thanks to elevenlabs.io for some sound effects URL: https://elevenlabs.io/
Big thanks to RyiSnow for the Blue Boy Adventure playlist for the code and most of audio.
Youtube: https://www.youtube.com/@RyiSnow Drive:https://drive.google.com/drive/folders/1OBRM8M3qCNAfJDCaldg62yFMiyFaKgYx


*/
public class GamePanel extends JPanel {
    private final int tileSize = 32;
    private final int mapWidth = 30;
    private final int mapHeight = 30;
    private Tile[][] map;

    private Player player;
    private int cameraX = 0;
    private int cameraY = 0;

    private int stairsUpX, stairsUpY;
    private final int visibilityRadius = 9;

    private final int MAX_ENEMIES = 10;

    private int potionsSpawnedThisLevel = 0;
    private final int MAX_POTIONS_PER_LEVEL = 5;


    boolean attackMode = false;

    private boolean gameOver = false;

    private MessagePanel messagePanel;

    private MapManager mapManager = new MapManager();

    private List<Enemy> enemies = new ArrayList<>();

    private Enemy bossEnemy = null;


    private game.ui.UIPanel uiPanel;
    private Timer uiTimer;

    private AudioPlayer Music;



    public GamePanel(Player initialPlayer) {
        setPreferredSize(new Dimension(tileSize * mapWidth+200, tileSize * mapHeight));
        setBackground(Color.BLACK);

        // --- Initialization ---
        mapManager.generateAllLevels(mapWidth, mapHeight);
        map = mapManager.getCurrentLevel();
        Point start = mapManager.getStairsUp();

        this.player = initialPlayer;
        this.player.setPosition(start.x, start.y);
        this.player.setMessagePanel(messagePanel);

        // Background Music
        AudioPlayer.playMusic("/sound/Dungeon.wav");
        spawnRandomWeapons(mapManager.getCurrentLevelIndex());

        // --- Show lore ---
        SwingUtilities.invokeLater(() -> {
            if (messagePanel != null) {
                messagePanel.logMessage("📖 Long ago, chaos consumed these dungeons...");
                messagePanel.logMessage("⛓ The walls whisper of forgotten kings...");
                messagePanel.logMessage("💀 The Serpent of Chaos was sealed deep inside...");
                messagePanel.logMessage("⚔ Yet tonight, a wandering soul dares to descend where none should tread.");
                messagePanel.logMessage("Remember stranger: even Heroes must rest,for steel grows heavy in weary hands.");
                messagePanel.logMessage("And a weakened soul is less appetizing to the beasts inside");
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (gameOver) {
                    return;
                }// Ending Flag

                int key = e.getKeyCode();
                int dx = 0, dy = 0;
                if (uiPanel != null) {
                    uiPanel.repaint();
                }

                // --- Resting ---
                if (key == KeyEvent.VK_R) {
                    AudioPlayer.playSound("/sound/yawn.wav");
                    GamePanel.this.player.rest();
                    messagePanel.logMessage("You take a moment to rest...");
                    // 5% flat chance
                    if (new Random().nextDouble() <= 0.5) {
                        spawnRandomEnemy();
                    }
                    uiPanel.repaint(); // update screen
                }
                // --- Heal Potion ---
                if (key == KeyEvent.VK_H) {
                    if (GamePanel.this.player.useHealthPotion()) {
                        AudioPlayer.playSound("/sound/drinking.wav");
                        messagePanel.logMessage("You used a health potion.");
                    } else {
                        messagePanel.logMessage("No health potions left.");
                    }
                    uiPanel.repaint(); //HP UI needs to refresh
                }
                // --- Mana Potion ---
                if (key == KeyEvent.VK_M) {
                    if (GamePanel.this.player.useManaPotion()) {
                        messagePanel.logMessage("You used a mana potion.");
                    } else {
                        messagePanel.logMessage("No mana potions left.");
                    }
                    uiPanel.repaint(); // Mana UI needs to refresh
                }
                // --- Weapon Change ---
                if (key == KeyEvent.VK_P) {
                    AudioPlayer.playSound("/sound/pickingitem.wav");
                    Tile currentTile = map[GamePanel.this.player.y][GamePanel.this.player.x];
                    Item item = currentTile.getItem();

                    if (item instanceof Weapon) {
                        Weapon newWeapon = (Weapon) item;
                        Weapon oldWeapon = GamePanel.this.player.getInventory().getEquippedWeapon();

                        // Remove the picked-up weapon from the tile first
                        currentTile.removeItem();

                        // Drop the old weapon (if any) on the tile
                        if (oldWeapon != null) {
                            currentTile.setItem(oldWeapon);
                            messagePanel.logMessage("Dropped weapon: " + oldWeapon.getName());
                        }

                        // Equip the new weapon
                        GamePanel.this.player.getInventory().setEquippedWeapon(newWeapon);
                        messagePanel.logMessage("Picked up weapon: " + newWeapon.getName());

                        uiPanel.repaint();
                    } else {
                        // --- DROP ---
                        Weapon oldWeapon = GamePanel.this.player.getInventory().getEquippedWeapon();

                        if (oldWeapon != null && currentTile.getItem() == null) {
                            currentTile.setItem(oldWeapon);
                            GamePanel.this.player.getInventory().setEquippedWeapon(null);
                            messagePanel.logMessage("Dropped weapon: " + oldWeapon.getName());
                        } else if (oldWeapon == null) {
                            messagePanel.logMessage("No weapon equipped to drop.");
                        } else {
                            messagePanel.logMessage("Can't drop here.");
                        }
                    }
                }


                // --- Attacking ---
                if (key == KeyEvent.VK_SPACE) {
                    attackMode = true;
                    messagePanel.logMessage("Attack mode enabled! Press a direction to attack.");
                    return;
                }

                switch (key) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        dy = -1;
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_S:
                        dy = 1;
                        break;
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        dx = -1;
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        dx = 1;
                        break;
                }

                if (dx != 0 || dy != 0) {
                    if (attackMode) {
                        GamePanel.this.player.attack(enemies, dx, dy);
                        // Collect defeated enemies and reward EXP
                        List<Enemy> defeated = new ArrayList<>();
                        for (Enemy enemy : enemies) {
                            if (!enemy.isAlive()) {
                                int ex = enemy.getX();
                                int ey = enemy.getY();
                                Tile currentTile = map[enemy.getY()][enemy.getX()];
                                defeated.add(enemy);

                                // --- BOSS ---
                                if (enemy instanceof SerpentOfChaos) {
                                    if (currentTile.getItem() == null) {
                                        currentTile.setItem(new game.items.Diamond());
                                        messagePanel.logMessage("💎 The Serpent of Chaos dropped a Diamond!");
                                    }
                                }
                                else { // --- Normal Enemies ---
                                    if (Math.random() < 0.25) {// 25% chance to drop potion
                                        if (Math.random() < 0.5) {
                                            if (currentTile.getItem() == null) {
                                                map[ey][ex].setItem(new game.items.HealthPotion());
                                                messagePanel.logMessage("A Health Potion dropped!");
                                            }
                                        } else {
                                            if (map[ey][ex].getItem() == null) {
                                                map[ey][ex].setItem(new game.items.ManaPotion());
                                                messagePanel.logMessage("A Mana Potion dropped!");
                                            }
                                        }
                                    }
                                }
                                int exp = enemy.getExpReward();
                                GamePanel.this.player.gainExperience(exp);
                                messagePanel.logMessage("You defeated an enemy! +" + exp + " EXP");
                            }
                        }

                        enemies.removeAll(defeated);
                        attackMode = false;
                    } else {
                        GamePanel.this.player.move(dx, dy, map, enemies);
                        // PICK UP ITEM HERE
                        Tile currentTile = map[GamePanel.this.player.y][GamePanel.this.player.x];
                        Item item = currentTile.getItem();

                        if (item != null && item.isConsumable()) {
                            if (item instanceof HealthPotion) {
                                GamePanel.this.player.getInventory().addHealthPotion();
                                GamePanel.this.player.getInventory().addItem(item);
                                messagePanel.logMessage("Picked up: " + item.getName());
                            }
                            else if (item instanceof ManaPotion) {
                                GamePanel.this.player.getInventory().addManaPotion();
                                GamePanel.this.player.getInventory().addItem(item);
                                messagePanel.logMessage("Picked up: " + item.getName());
                            }
                            else if (item instanceof Trap) {
                                AudioPlayer.playSound("/sound/bomb.wav");
                                item.applyEffect(GamePanel.this.player); // trigger trap immediately
                            }
                            else if (item instanceof Diamond) {
                                AudioPlayer.stopMusic();
                                AudioPlayer.playSound("/sound/victory.wav");
                                messagePanel.logMessage("💎 You picked up the Diamond!");
                                messagePanel.logMessage("🎉 VICTORY! 🎉");
                                gameOver = true;
                            }


                            currentTile.removeItem();
                        }
                        spawnRandomEnemy();
                        spawnRandomPotion();
                        spawnRandomTrap(mapManager.getCurrentLevelIndex());
                    }

                    repaint(); 
                }

                Tile currentTile = map[GamePanel.this.player.y][GamePanel.this.player.x];

                // --- MOVEMENT BETWEEN FLOORS ---
                if (currentTile.type == Tile.Type.STAIRS_DOWN && mapManager.canGoDown() && GamePanel.this.player.justSteppedOnStairs()) {
                    mapManager.goDown();
                    messagePanel.logMessage("You descend deeper into the unknown...");
                    AudioPlayer.playSound("/sound/stairs.wav");
                    map = mapManager.getCurrentLevel();
                    Point newStart = mapManager.getStairsUp();
                    GamePanel.this.player.setPosition(newStart.x, newStart.y);
                    enemies = new ArrayList<>();




                    if (mapManager.getCurrentLevelIndex() == 9) { // final level
                        AudioPlayer.stopMusic();
                        AudioPlayer.playMusic("/sound/FinalBattle.wav");
                        if (bossEnemy != null && bossEnemy.isAlive()) {
                            Point safePos = getSafeBossPosition(map); // Put the boos far away when reentering
                            bossEnemy.setPosition(safePos.x, safePos.y);
                            enemies.add(bossEnemy); //  reuse the same boss
                        } else {
                            spawnBossIfFinalLevel(); // only spawn if never created
                        }
                    }

                    potionsSpawnedThisLevel = 0;
                    spawnRandomWeapons(mapManager.getCurrentLevelIndex());
                }

                if (currentTile.type == Tile.Type.STAIRS_UP && mapManager.canGoUp() && GamePanel.this.player.justSteppedOnStairs()) {
                    mapManager.goUp();
                    AudioPlayer.playSound("/sound/stairs.wav");
                    map = mapManager.getCurrentLevel();
                    Point newStart = mapManager.getStairsDown();
                    GamePanel.this.player.setPosition(newStart.x, newStart.y);
                    enemies = new ArrayList<>();
                    potionsSpawnedThisLevel = 0;

                    // If went to final level and then left
                    if (mapManager.getCurrentLevelIndex() != 9 && bossEnemy != null && bossEnemy.isAlive()){
                        AudioPlayer.stopMusic();
                        AudioPlayer.playMusic("/sound/Dungeon.wav");
                    }

                }
                GamePanel.this.player.updateLastPosition();

                // Check if any enemy is adjacent to the player, then attack
                for (Enemy enemy : enemies) {
                    int dxa = Math.abs(enemy.getX() - GamePanel.this.player.x);
                    int dya = Math.abs(enemy.getY() - GamePanel.this.player.y);
                    if (dxa + dya == 1) { // 4-directional adjacency
                        enemy.attack(GamePanel.this.player);
                        //messagePanel.logMessage("Player HP: " + player.getHp());
                    }
                }
                // --- IF CLOSE ENEMY MOVE ---
                for (Enemy enemy : enemies) {
                    double dist = Math.sqrt(Math.pow(GamePanel.this.player.x - enemy.getX(), 2) + Math.pow(GamePanel.this.player.y - enemy.getY(), 2));
                    if (dist <= 6) {
                        enemy.moveTowardPlayer(GamePanel.this.player, map, enemies);
                    }
                }
                // --- GAME OVER STATE ---
                if (GamePanel.this.player.getHp() <= 0) {
                    gameOver = true;
                    AudioPlayer.stopMusic();
                    AudioPlayer.playSound("/sound/gameover.wav");
                    messagePanel.logMessage("GAME OVER");
                    repaint(); // force redraw to show Game Over
                    return;
                }
                repaint();
            }
        });
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // --- Camera Center ---
        cameraX = (player.x * tileSize) - getWidth() / 2 + tileSize / 2;
        cameraY = (player.y * tileSize) - getHeight() / 2 + tileSize / 2;

        // --- Draw Tiles ---
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Tile tile = map[y][x];

                // Distance to player
                double dist = Math.sqrt(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2));

                int screenX = (x * tileSize) - cameraX;
                int screenY = (y * tileSize) - cameraY;

                // Draw tile only if within visibility radius
                if (dist <= visibilityRadius) {
                    // Mark the seen tiles
                    tile.visited = true;

                    tile.render(g, screenX, screenY, tileSize);

                    // Optional shading for distance (dim the tile)
                    float shade = (float) Math.max(0.3, 1.0 - (dist / visibilityRadius));
                    g.setColor(new Color(0, 0, 0, 1 - shade)); // transparent black overlay
                    g.fillRect(screenX, screenY, tileSize, tileSize);
                }else if (tile.visited) {
                    // Fogged tile: draw normally but overlay gray
                    tile.render(g, screenX, screenY, tileSize);
                    g.setColor(new Color(0, 0, 0, 180)); // semi-transparent black
                    g.fillRect(screenX, screenY, tileSize, tileSize);
                }
                else {
                    // Draw completely black for unseen tiles
                    g.setColor(Color.BLACK);
                    g.fillRect(screenX, screenY, tileSize, tileSize);
                }
            }
        }

        // --- Draw Items ---
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                Tile tile = map[y][x];
                Item item = tile.getItem();
                if (item != null) {
                    double dist = Math.sqrt(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2));
                    if (dist <= visibilityRadius) {
                        int screenX = (x * tileSize) - cameraX;
                        int screenY = (y * tileSize) - cameraY;
                        item.render(g, screenX, screenY, tileSize);
                    }
                }
            }
        }

        // --- Draw Player ---
        int playerScreenX = (player.x * tileSize) - cameraX;
        int playerScreenY = (player.y * tileSize) - cameraY;
        player.draw(g, playerScreenX, playerScreenY, tileSize);

        // --- Draw Enemies ---
        for (Enemy enemy : enemies) {
            double dist = Math.sqrt(Math.pow(player.x - enemy.getX(), 2) + Math.pow(player.y - enemy.getY(), 2));
            if (dist <= visibilityRadius) {
                int screenX = (enemy.getX() * tileSize) - cameraX;
                int screenY = (enemy.getY() * tileSize) - cameraY;
                enemy.draw(g, screenX, screenY, tileSize);
            }
        }

        // ---  Grid Lines ---
        g.setColor(new Color(0, 0, 0, 100)); // semi-transparent
        for (int y = 0; y <= mapHeight; y++) {
            g.drawLine(-cameraX, y * tileSize - cameraY, mapWidth * tileSize - cameraX, y * tileSize - cameraY);
        }
        for (int x = 0; x <= mapWidth; x++) {
            g.drawLine(x * tileSize - cameraX, -cameraY, x * tileSize - cameraX, mapHeight * tileSize - cameraY);
        }
    }


    // --- Enemy spawn Logic ---
    private void spawnRandomEnemy() {
        if (enemies.size() >= MAX_ENEMIES) return; // max limit
        Random rand = new Random();

        // Compute spawn chance based on player HP %
        double hpPercent = player.getHpPercent();
        double spawnChance = hpPercent * 0.2;

        if (rand.nextDouble() > spawnChance) return; // Not spawning this time


        Tile[][] map = mapManager.getCurrentLevel();
        int attempts = 0;
        int maxAttempts = 100;

        while (attempts < maxAttempts) {


            // Offset from player position in a square range
            int offsetX = rand.nextInt((visibilityRadius + 2) * 2 + 1) - (visibilityRadius + 2);
            int offsetY = rand.nextInt((visibilityRadius + 2) * 2 + 1) - (visibilityRadius + 2);

            int x = player.x + offsetX;
            int y = player.y + offsetY;

            // Stay within map bounds
            if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
                attempts++;
                continue;
            }
            // Must be a floor tile and not the player's current position
            if (map[y][x].type == Tile.Type.FLOOR &&
                    !(x == player.x && y == player.y)) {

                // Check distance from player
                double dist = Math.sqrt(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2));
                if (dist >= visibilityRadius - 2 && dist <= visibilityRadius) {
                    Enemy enemy = getRandomEnemyForLevel((mapManager.getCurrentLevelIndex()+1),x,y);
                    enemy.setMessagePanel(messagePanel);
                    enemies.add(enemy);
                    break;
                }
            }

            attempts++;
        }
    }
    // --- Trap Spawn Logic ---
    private void spawnRandomTrap(int mapLevel) {
        Random rand = new Random();

        double spawnChance = 0.3; // 30% chance per move

        if (rand.nextDouble() > spawnChance) return;

        Tile[][] map = mapManager.getCurrentLevel();
        int attempts = 0;
        int maxAttempts = 100;

        while (attempts < maxAttempts) {
            int offsetX = rand.nextInt((visibilityRadius + 2) * 2 + 1) - (visibilityRadius + 2);
            int offsetY = rand.nextInt((visibilityRadius + 2) * 2 + 1) - (visibilityRadius + 2);

            int x = player.x + offsetX;
            int y = player.y + offsetY;

            if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
                attempts++;
                continue;
            }

            if (map[y][x].type == Tile.Type.FLOOR &&
                    map[y][x].getItem() == null && // no item already there
                    !(x == player.x && y == player.y)) {

                // Trap damage can scale with mapLevel
                int baseDamage = 8;
                int scaledDamage = baseDamage + (mapLevel * 5);
                map[y][x].setItem(new game.items.Trap(scaledDamage));

                break;
            }

            attempts++;
        }
    }
    // --- Potion Spawn Logic ---
    private void spawnRandomPotion() {
        if (potionsSpawnedThisLevel >= MAX_POTIONS_PER_LEVEL) return; // Potion Limit

        Random rand = new Random();

        double spawnChance = 0.1; // 10% chance per move

        if (rand.nextDouble() > spawnChance) return;

        Tile[][] map = mapManager.getCurrentLevel();
        int attempts = 0;
        int maxAttempts = 100;

        while (attempts < maxAttempts) {
            int offsetX = rand.nextInt((visibilityRadius + 2) * 2 + 1) - (visibilityRadius + 2);
            int offsetY = rand.nextInt((visibilityRadius + 2) * 2 + 1) - (visibilityRadius + 2);

            int x = player.x + offsetX;
            int y = player.y + offsetY;

            if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
                attempts++;
                continue;
            }

            if (map[y][x].type == Tile.Type.FLOOR &&
                    map[y][x].getItem() == null && // no item already there
                    !(x == player.x && y == player.y)) {

                if (rand.nextBoolean()) {
                    map[y][x].setItem(new game.items.HealthPotion()); // Spawn Health Potion
                } else {
                    map[y][x].setItem(new game.items.ManaPotion()); // Spawn Mana Potion
                }

                potionsSpawnedThisLevel++;
                break;
            }

            attempts++;
        }
    }
    // --- Weapon Spawn Logic ---
    private void spawnRandomWeapons(int mapLevel) {
        Tile[][] map = mapManager.getCurrentLevel();
        Random rand = new Random();
        int weaponsToSpawn = 2; // Weapon Limit
        int attempts = 0;

        while (weaponsToSpawn > 0 && attempts < 200) {
            int x = rand.nextInt(map[0].length);
            int y = rand.nextInt(map.length);

            if (map[y][x].type == Tile.Type.FLOOR &&
                    map[y][x].getItem() == null &&
                    !(x == player.x && y == player.y)) {

                Weapon weapon = WeaponFactory.createRandomWeapon(mapLevel);
                map[y][x].setItem(weapon);
                weaponsToSpawn--;
            }

            attempts++;
        }
    }

    private void spawnBossIfFinalLevel() {
        int currentLevel = mapManager.getCurrentLevelIndex();
        int finalLevel = 9;

        // Only spawn once, and only on the final level
        if (currentLevel == finalLevel && (bossEnemy == null || !bossEnemy.isAlive() )) {
            Tile[][] map = mapManager.getCurrentLevel();
            Random rand = new Random();

            while (true) {
                int x = rand.nextInt(map[0].length);
                int y = rand.nextInt(map.length);

                if (map[y][x].type == Tile.Type.FLOOR && !(x == player.x && y == player.y)) {
                    bossEnemy = new game.enemies.SerpentOfChaos(x, y);
                    bossEnemy.setMessagePanel(messagePanel);
                    enemies.add(bossEnemy);
                    messagePanel.logMessage("⚔ The Dungeon Overlord has appeared!");
                    break;
                }
            }
        }
    }

    private Point getSafeBossPosition(Tile[][] map) {
        Random rand = new Random();
        Point stairs = mapManager.getStairsUp();
        int attempts = 0;

        while (attempts < 1000) {
            int x = rand.nextInt(map[0].length);
            int y = rand.nextInt(map.length);

            if (map[y][x].type == Tile.Type.FLOOR) {
                double dist = Math.hypot(x - stairs.x, y - stairs.y);
                if (dist > 6) { // at least 6 tiles away
                    return new Point(x, y);
                }
            }
            attempts++;
        }

        // fallback: just put it far corner
        return new Point(map[0].length/2, map.length/2);
    }


    public Player getPlayer() {
        return player;
    }

    public void setMessagePanel(MessagePanel messagePanel) {
        this.messagePanel = messagePanel;
        player.setMessagePanel(messagePanel); // connect to player

        for (Enemy enemy : enemies) {
            enemy.setMessagePanel(messagePanel); // connect to enemies
        }
    }
    public void setUiPanel(game.ui.UIPanel uiPanel) {
        this.uiPanel = uiPanel;

    }

    // --- What enemy in each Floor ---
    private Enemy getRandomEnemyForLevel(int level, int x, int y) {
        Random rand = new Random();

        switch (level) {
            case 1: {
                Enemy[] level1Enemies = {
                        new Goblin(x, y),
                        new Rat(x, y)
                };
                return level1Enemies[rand.nextInt(level1Enemies.length)];
            }
            case 2: {
                Enemy[] level2Enemies = {
                        new Goblin(x, y),
                        new Rat(x, y),
                        new Zombie(x, y)
                };
                return level2Enemies[rand.nextInt(level2Enemies.length)];
            }
            case 3: {
                Enemy[] level3Enemies = {
                        new Skeleton(x, y),
                        new FireElemental(x, y),
                        new Zombie(x, y)

                };
                return level3Enemies[rand.nextInt(level3Enemies.length)];
            }
            case 4: {
                Enemy[] level4Enemies = {
                        new Skeleton(x, y),
                        new Slime(x, y),
                        new FireElemental(x, y)
                };
                return level4Enemies[rand.nextInt(level4Enemies.length)];
            }
            case 5: {
                Enemy[] level5Enemies = {
                        new Skeleton(x, y),
                        new Slime(x, y),
                        new Orc(x, y),
                };
                return level5Enemies[rand.nextInt(level5Enemies.length)];
            }
            case 6: {
                Enemy[] level6Enemies = {
                        new Golem(x, y),
                        new Orc(x, y),
                };
                return level6Enemies[rand.nextInt(level6Enemies.length)];
            }
            case 7: {
                Enemy[] level7Enemies = {
                        new Orc(x, y),
                        new Golem(x, y),
                        new Knight(x, y)

                };
                return level7Enemies[rand.nextInt(level7Enemies.length)];
            }
            case 8: {
                Enemy[] level8Enemies = {
                        new Golem(x, y),
                        new Knight(x, y),
                };
                return level8Enemies[rand.nextInt(level8Enemies.length)];
            }
            case 9: {
                Enemy[] level9Enemies = {
                        new Wraith(x, y),
                        new Knight(x, y),
                };
                return level9Enemies[rand.nextInt(level9Enemies.length)];
            }
            default: {
                // fallback enemy pool
                Enemy[] defaultEnemies = {
                        new Goblin(x, y)
                };
                return defaultEnemies[rand.nextInt(defaultEnemies.length)];
            }
        }
    }


}
