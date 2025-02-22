package com.destrostudios.survivors.game;

public abstract class Ability {

    protected int cooling;
    protected final int cooldown;

    protected int firing;
    protected final int firedown;
    protected final int projectiles;

    public Ability(int cooldown, int firedown, int projectiles) {
        this.cooldown = cooldown;
        this.firedown = firedown;
        this.projectiles = projectiles;
    }

    public void tick(Game game) {
        if (cooling > 0) {
            cooling--;
            if (cooling == 0) {
                firing = firedown / game.player.fireSpeedPercentage;
            }
        }
        if (firing > 0) {
            int prevRemaining = Math.floorDiv((projectiles + game.player.bonusProjectiles) * firing, firedown);
            firing--;
            int nextRemaining = Math.floorDiv((projectiles + game.player.bonusProjectiles) * firing, firedown);
            for (int i = nextRemaining; i < prevRemaining; i++) {
                fire(game);
            }
            if (firing == 0) {
                cooling = cooldown / game.player.coolingSpeedPercentage;
            }
        }

    }

    protected abstract void fire(Game game);
}
