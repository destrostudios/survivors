package com.destrostudios.survivors.game;

public abstract class Enemy {
    public int health;
    public int x, y;

    public boolean isDead() {
        return health <= 0;
    }

    public abstract void tick(Game game);
}
