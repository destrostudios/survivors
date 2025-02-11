package com.destrostudios.survivors.client.files;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class FileManager{

    public static String getFileContent(String filePath) {
        String text = "";
        List<String> lines = getFileLines(filePath);
        int i = 0;
        for (String line : lines) {
            if (i != 0) {
                text += "\n";
            }
            text += line;
            i++;
        }
        return text;
    }

    public static List<String> getFileLines(String filePath) {
        LinkedList<String> lines = new LinkedList<>();
        try {
            BufferedReader reader;
            if (filePath.startsWith("http:")) {
                reader = new BufferedReader(new InputStreamReader(new URL(filePath).openStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return lines;
    }

    public static void putFileContent(String filePath, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
            String[] lines = content.split("\n");
            for (int i = 0; i < lines.length; i++) {
                if (i != 0) {
                    writer.newLine();
                }
                writer.write(lines[i]);
            }
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
