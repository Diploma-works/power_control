package ru.guybydefault.foody.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class HealthDietDownloader {

    public static String HEALTH_DIET_API = "https://health-diet.ru/api2/base_of_food/common/";
    public static Integer MAX_ID = 30000;

    public static void main(String[] args) {
        parse();
    }

    public static void parse() {
        File dir = new File("data");
        int startId = 0;
        for (File file : dir.listFiles()) {
            int id = Integer.valueOf(file.getName().split(".json")[0]);
            if (id > startId) {
                startId = id;
            }
        }
        System.out.println("Starting from id " + startId);
        for (int id = startId; id < MAX_ID; id++) {
            try {
                if (!Files.exists(Path.of("data/" + id + ".json"))) {
                    downloadProduct(String.valueOf(id));
                    Thread.sleep(100);
                } else {
                    System.out.println(id + " exists");
                }
                if (id % 100 == 0) {
                    System.out.println(id + " ids processed");
                }
            } catch (IOException | InterruptedException e) {
                if (!e.getMessage().startsWith("Server returned HTTP response code: 403 for URL")) {
                    e.printStackTrace();
                } else {
                    System.out.println(id + " not exists in DB");
                }
            }
        }
    }

    public static void downloadProduct(String id) throws IOException {
        URL website = new URL(HEALTH_DIET_API + id + ".json?10");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        File file = new File("data/" + id + ".json");
        FileOutputStream fos = new FileOutputStream(file);
        try {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            throw e;
        } finally {
            fos.close();
            rbc.close();
        }
    }
}
