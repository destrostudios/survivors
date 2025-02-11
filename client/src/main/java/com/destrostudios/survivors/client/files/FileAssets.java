package com.destrostudios.survivors.client.files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileAssets {

    public static String ROOT;

    public static void readRootFile() {
        ROOT = FileManager.getFileContent("./assets.ini");
    }

    public static FileInputStream getInputStream(String filePath) {
        try {
            return new FileInputStream(ROOT + filePath);
        } catch (FileNotFoundException ex) {
            System.err.println("Error while reading file '" + filePath + "': " + ex.getMessage());
        }
        return null;
    }
}
