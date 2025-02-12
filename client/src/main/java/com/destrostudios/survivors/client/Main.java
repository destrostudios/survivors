package com.destrostudios.survivors.client;

import com.destrostudios.survivors.client.files.FileAssets;
import com.jme3.anim.SkinningControl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        FileOutputStream logFileOutputStream = new FileOutputStream("./log.txt");
        System.setOut(new PrintStream(new MultipleOutputStream(System.out, logFileOutputStream)));
        System.setErr(new PrintStream(new MultipleOutputStream(System.err, logFileOutputStream)));

        // Both are required to avoid the hardware skinning info logs
        Logger.getLogger("").setLevel(Level.WARNING);
        Logger.getLogger(SkinningControl.class.getName()).setLevel(Level.WARNING);

        FileAssets.readRootFile();

        new ClientApplication().start();
    }
}
