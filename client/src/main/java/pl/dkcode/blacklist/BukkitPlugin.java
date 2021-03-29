package pl.dkcode.blacklist;

import org.bukkit.plugin.java.JavaPlugin;
import pl.dkcode.blacklist.handlers.BlacklistAPI;
import pl.dkcode.blacklist.objects.Config;

import java.io.File;

/**
 * @Author: Kacper 'DeeKaPPy' Horbacz
 * @Created: 29.03.2021
 * @Class: BukkitPlugin
 */

public class BukkitPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Config.init(new File("./plugins/dkBlacklist"));
        Config.load("./plugins/dkBlacklist/blacklists.json");
        BlacklistAPI blacklistHandler = new BlacklistAPI();
        blacklistHandler.setup(Config.getInstance());
        blacklistHandler.start();
    }

}
