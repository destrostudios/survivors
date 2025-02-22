package com.destrostudios.survivors.game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    final List<Ability> abilities = new ArrayList<>();
    public List<PlayerProjectile> projectiles = new ArrayList<>();

    int x;
    int y;
    int moveSpeed;
    int health;
    int maxHealth;

    int coolingSpeedPercentage;
    int fireSpeedPercentage;
    int bonusProjectiles;
    int areaScalePercentage;
    int speedPercentage;
    int healthPercentage;

    public void tick(Game game, int dX, int dY) {
        double d = Math.sqrt(dX * dX + dY * dY);
        x += (int) Math.floor(dX / d * moveSpeed * speedPercentage / 100);
        y += (int) Math.floor(dY / d * moveSpeed * speedPercentage / 100);
    }
}
