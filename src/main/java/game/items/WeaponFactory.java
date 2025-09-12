package game.items;

import java.util.Random;

public class WeaponFactory {

    public static Weapon createRandomWeapon(int mapLevel) {
        Random rand = new Random();
        int type = rand.nextInt(6);

        int base = Math.max(mapLevel, 1);

        switch (type) {
            case 0:
                return new Weapon("Frostfang Dagger", 2 * base, 0, 10 * base, 0);
            case 1:
                return new Weapon("Ironheart Longsword", 3 * base, 0, 12 * base, 0);
            case 2:
                return new Weapon("Deathbringer Scythe", 4 * base, 0, 8 * base, 0);
            case 3:
                return new Weapon("Inferno Staff", 0, 3 * base, 0, 10 * base);
            case 4:
                return new Weapon("Celestial Orb", 0, 2 * base, 0, 15 * base);
            case 5:
                return new Weapon("Phoenix Wand", 0, 4 * base, 0, 12 * base);
            default:
                return new Weapon("Stick", 1, 0, 0, 0); // Fallback
        }
    }
}