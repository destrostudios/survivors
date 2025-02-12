package com.destrostudios.survivors.client;

import com.destrostudios.survivors.client.appstates.GameAppState;
import com.destrostudios.survivors.client.appstates.LoadingAppState;
import com.destrostudios.survivors.client.files.FileAssets;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

import java.util.concurrent.atomic.AtomicBoolean;

public class ClientApplication extends SimpleApplication {

    public ClientApplication() {
        loadSettings();
        setPauseOnLostFocus(false);
        setDisplayStatView(false);
    }

    private void loadSettings() {
        settings = new AppSettings(true);
        settings.setTitle("Survivors");
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setVSync(true);
        setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator(FileAssets.ROOT, FileLocator.class);

        flyCam.setEnabled(false);
        GuiGlobals.initialize(this);

        AtomicBoolean asyncInitDone = new AtomicBoolean(false);
        initAsync(asyncInitDone);
        stateManager.attach(new LoadingAppState() {

            @Override
            protected boolean shouldClose() {
                return asyncInitDone.get();
            }

            @Override
            protected void close() {
                super.close();
                stateManager.attach(new GameAppState());
            }
        });
    }

    private void initAsync(AtomicBoolean asyncInitDone) {
        new Thread(() -> {
            BaseStyles.loadGlassStyle();
            GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
            AssetPreloader.preload(assetManager);
            asyncInitDone.set(true);
        }).start();
    }
}
