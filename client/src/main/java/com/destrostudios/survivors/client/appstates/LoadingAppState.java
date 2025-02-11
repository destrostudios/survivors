package com.destrostudios.survivors.client.appstates;

import com.jme3.math.ColorRGBA;

public abstract class LoadingAppState extends OverlayAppState {

    public LoadingAppState() {
        super("Loading...", ColorRGBA.White, false);
    }

    @Override
    protected abstract boolean shouldClose();
}
