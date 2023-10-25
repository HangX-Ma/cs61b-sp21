package byow.Core.Avatar;

import byow.Core.World;

import java.io.Serializable;

public class Tofu implements Serializable {
    private int x;
    private int y;
    private int health;

    public enum TofuAction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }

    public Tofu(int x, int y) {
        health = 100;
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void move(TofuAction to, World world) {
        switch (to) {
            case UP -> {
                if (world.isAccessible(x, y + 1)) {
                    y += 1;
                }
            }
            case DOWN -> {
                if (world.isAccessible(x, y - 1)) {
                    y -= 1;
                }
            }
            case LEFT -> {
                if (world.isAccessible(x - 1, y)) {
                    x -= 1;
                }
            }
            case RIGHT -> {
                if (world.isAccessible(x + 1, y)) {
                    x += 1;
                }
            }
        }
    }

}
