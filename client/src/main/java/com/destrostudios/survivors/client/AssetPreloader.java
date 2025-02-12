package com.destrostudios.survivors.client;

import com.destrostudios.survivors.client.files.FileAssets;
import com.jme3.asset.AssetManager;

import java.io.File;

public class AssetPreloader {

    public static void preload(AssetManager assetManager) {
        preloadDirectory(assetManager, "models/");
        preloadDirectory(assetManager, "textures/");
    }

    private static void preloadDirectory(AssetManager assetManager, String directoryPath) {
        File directory = new File(FileAssets.ROOT + directoryPath);
        for (File file : directory.listFiles()) {
            String assetPath = (directoryPath + file.getName());
            if (file.isDirectory()) {
                preloadDirectory(assetManager, assetPath + "/");
            } else {
                preloadAsset(assetManager, assetPath);
            }
        }
    }

    private static void preloadAsset(AssetManager assetManager, String assetPath) {
        if (assetPath.endsWith(".png")) {
            assetManager.loadTexture(assetPath);
        } else if (assetPath.endsWith(".j3o")) {
            assetManager.loadModel(assetPath);
        }
    }
}
