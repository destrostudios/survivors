package com.destrostudios.survivors.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    public final Player player;
    public final List<Enemy> enemies = new ArrayList<>();

    public Game(Player player) {
        this.player = player;
    }

    public void tick() {
        player.tick(this, 0, 0);
        for (Enemy enemy : enemies) {
            enemy.tick(this);
        }
        for (Ability ability : player.abilities) {
            ability.tick(this);
        }
        Iterator<PlayerProjectile> iterator = player.projectiles.iterator();
        while (iterator.hasNext()) {
            PlayerProjectile projectile = iterator.next();
            projectile.tick(this);
            if (projectile.isExpired()) {
                iterator.remove();
            }
        }
        Iterator<Enemy> it = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = it.next();
            if (enemy.isDead()) {
                it.remove();
            }
        }
    }
}
