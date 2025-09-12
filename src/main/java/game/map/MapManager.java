package game.map;

import game.Tile;
import java.awt.*;
import java.util.Random;

public class MapManager {
    private static final int MAX_LEVELS = 10; //Levels of the Dungeon
    private LevelData[] levels = new LevelData[MAX_LEVELS];
    private int currentLevel = 0;

    public Tile[][] getCurrentLevel() {
        return levels[currentLevel].map;
    }

    public int getCurrentLevelIndex() {
        return currentLevel;
    }

    // Generation of Levels from the start
    public void generateAllLevels(int width, int height) {
        for (int i = 0; i < MAX_LEVELS; i++) {
            levels[i] = generateLevel(width, height);
        }
    }

    public LevelData generateLevel(int width, int height) {
        Tile[][] map = new Tile[height][width];
        // Fill with WALL
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = new Tile(Tile.Type.WALL);
            }
        }

        Random rand = new Random();
        // The Entrance of the Level
        int stairsUpX = 0, stairsUpY = 0;
        int edge = rand.nextInt(4); // 0=top, 1=bottom, 2=left, 3=right
        switch (edge) {
            case 0 -> { stairsUpX = rand.nextInt(width - 2) + 1; stairsUpY = 0; }
            case 1 -> { stairsUpX = rand.nextInt(width - 2) + 1; stairsUpY = height - 1; }
            case 2 -> { stairsUpX = 0; stairsUpY = rand.nextInt(height - 2) + 1; }
            case 3 -> { stairsUpX = width - 1; stairsUpY = rand.nextInt(height - 2) + 1; }
        }

        map[stairsUpY][stairsUpX] = new Tile(Tile.Type.FLOOR); // temporary floor tile
        // Making of the Map (with 40% fillment)
        performRandomWalk(map, stairsUpX, stairsUpY, width, height, 0.4);

        map[stairsUpY][stairsUpX] = new Tile(Tile.Type.STAIRS_UP); // put stairs up tile
        Point stairsUp = new Point(stairsUpX, stairsUpY);

        // Place stairsDown the farthest away
        Point stairsDown = placeStairsDown(map, stairsUpX, stairsUpY, width, height);

        return new LevelData(map, stairsUp, stairsDown);
    }

    // Moving between levels
    public boolean canGoUp() {
        return currentLevel > 0;
    }

    public boolean canGoDown() {
        return currentLevel < MAX_LEVELS - 1;
    }

    public void goUp() {
        if (canGoUp()) currentLevel--;
    }

    public void goDown() {
        if (canGoDown()) currentLevel++;
    }

    // Random Walk (Map Generation) Algorithm (40%)
    private void performRandomWalk(Tile[][] map, int startX, int startY, int mapWidth, int mapHeight, double fillPercent) {
        int totalTiles = mapWidth * mapHeight;
        int targetFloorTiles = (int)(totalTiles * fillPercent);
        int floorCount = 1;

        int x = startX;
        int y = startY;

        Random rand = new Random();

        while (floorCount < targetFloorTiles) {
            int dir = rand.nextInt(4);
            switch (dir) {
                case 0 -> y = Math.max(1, y - 1); // up
                case 1 -> y = Math.min(mapHeight - 2, y + 1); // down
                case 2 -> x = Math.max(1, x - 1); // left
                case 3 -> x = Math.min(mapWidth - 2, x + 1); // right
            }

            if (map[y][x].type == Tile.Type.WALL) {
                map[y][x] = new Tile(Tile.Type.FLOOR);
                floorCount++;
            }
        }
    }

    // Find farthest away point
    private Point placeStairsDown(Tile[][] map, int stairsUpX, int stairsUpY, int mapWidth, int mapHeight) {
        int maxDist = -1;
        int stairsDownX = 1, stairsDownY = 1;

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (map[y][x].type == Tile.Type.FLOOR) {
                    int dist = Math.abs(x - stairsUpX) + Math.abs(y - stairsUpY);
                    if (dist > maxDist) {
                        maxDist = dist;
                        stairsDownX = x;
                        stairsDownY = y;
                    }
                }
            }
        }

        map[stairsDownY][stairsDownX] = new Tile(Tile.Type.STAIRS_DOWN);
        return new Point(stairsDownX, stairsDownY);
    }

    //Location of the stairs
    public Point getStairsUp() {
        return levels[currentLevel].stairsUp;
    }

    public Point getStairsDown() {
        return levels[currentLevel].stairsDown;
    }
}
