package game.items;

public abstract class Potion extends Item {
    @Override
    public boolean isConsumable() {
        return true;
    }
}