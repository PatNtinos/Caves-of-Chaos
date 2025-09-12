package game.items;

import game.AudioPlayer;
import game.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory {
    private int healthPotions = 0;
    private int manaPotions = 0;
    private List<Item> items = new ArrayList<>();


    private Weapon equippedWeapon = null;

    public void addHealthPotion() {
        healthPotions++;
    }

    public void addManaPotion() {
        manaPotions++;
    }

    public boolean useHealthPotion(Player player) {
        if (healthPotions > 0) {
            player.heal(20); // example
            healthPotions--;
            return true;
        }
        return false;
    }

    public boolean useManaPotion(Player player) {
        if (manaPotions > 0) {
            player.regenerateMana(15);
            manaPotions--;
            return true;
        }
        return false;
    }

    public int getHealthPotionCount() {
        return healthPotions;
    }
    public int getManaPotionCount() {
        return manaPotions;
    }
    public void addItem(Item item) {
        AudioPlayer.playSound("/sound/pickingitem.wav");
        items.add(item);
    }
    public void setEquippedWeapon(Weapon w) {
        equippedWeapon = w;
    }
    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }
}
