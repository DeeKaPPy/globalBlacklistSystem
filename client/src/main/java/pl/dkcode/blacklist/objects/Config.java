package pl.dkcode.blacklist.objects;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @Author: Quosty
 * @Class: Config
 */

public class Config {
    private static Config instance;

    public String url;
    public Integer delay;

    public Config() {
        this.url = "http://localhost:3000";
        this.delay = 30000;
    }

    public static Config getInstance() {
        if (Config.instance == null) {
            Config.instance = fromDefaults();
        }
        return Config.instance;
    }

    public static void init(File filePath){
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
    }

    public static void load(final File file) {
        Config.instance = fromFile(file);
        if (Config.instance == null) {
            Config.instance = fromDefaults();
        }
    }

    public static void load(final String file) {
        load(new File(file));
    }

    private static Config fromDefaults() {
        final Config config = new Config();
        return config;
    }

    public void toFile(final String file) {
        this.toFile(new File(file));
    }

    public void toFile(final File file) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String jsonConfig = gson.toJson(this);
        try {
            final FileWriter writer = new FileWriter(file);
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Config fromFile(final File configFile) {
        try {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            return gson.fromJson(reader, Config.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}

