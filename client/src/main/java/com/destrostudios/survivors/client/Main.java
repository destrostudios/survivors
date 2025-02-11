package com.destrostudios.survivors.client;

import com.destrostudios.survivors.client.files.FileAssets;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {
        try {
            FileOutputStream logFileOutputStream = new FileOutputStream("./log.txt");
            System.setOut(new PrintStream(new MultipleOutputStream(System.out, logFileOutputStream)));
            System.setErr(new PrintStream(new MultipleOutputStream(System.err, logFileOutputStream)));
        } catch (FileNotFoundException ex) {
            System.err.println("Error while accessing log file: " + ex.getMessage());
        }
        FileAssets.readRootFile();
        new ClientApplication().start();
    }
}
