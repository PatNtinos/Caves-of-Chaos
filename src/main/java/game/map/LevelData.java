package game.map;

import game.Tile;

import java.awt.*;

// Add this as a nested class or in a new file
public class LevelData {
    public Tile[][] map;
    public Point stairsUp;
    public Point stairsDown;

    // Floor Info
    public LevelData(Tile[][] map, Point stairsUp, Point stairsDown) {
        this.map = map;
        this.stairsUp = stairsUp;
        this.stairsDown = stairsDown;
    }
}
