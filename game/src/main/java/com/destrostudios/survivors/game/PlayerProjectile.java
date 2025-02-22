package com.destrostudios.survivors.game;

public abstract class PlayerProjectile {
    protected Shape shape;
    protected int expiry;

    public void tick(Game game) {
        move(game);
        expiry--;
    }

    public abstract void move(Game game);

    public abstract void collide(Game game, Enemy enemy);

    public boolean isExpired() {
        return expiry <= 0;
    }
}
