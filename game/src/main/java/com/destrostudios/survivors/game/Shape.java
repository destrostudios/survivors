package com.destrostudios.survivors.game;

public abstract class Shape {

    protected Transform transform;

    public abstract boolean contains(int x, int y);

    public abstract int lowerX();

    public abstract int lowerY();

    public abstract int upperX();

    public abstract int upperY();
}
