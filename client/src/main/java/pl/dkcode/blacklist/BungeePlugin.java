package pl.dkcode.blacklist;

import net.md_5.bungee.api.plugin.Plugin;
import pl.dkcode.blacklist.handlers.BlacklistAPI;
import pl.dkcode.blacklist.objects.Blacklist;
import pl.dkcode.blacklist.objects.Config;

import java.io.File;

/**
 * @Author: Kacper 'DeeKaPPy' Horbacz
 * @Created: 28.03.2021
 * @Class: BungeePlugin
 */

public class BungeePlugin extends Plugin {

    @Override
    public void onEnable() {

        Config.init(new File("./plugins/dkBlacklist"));
        Config.load("./plugins/dkBlacklist/blacklists.json");
        BlacklistAPI blacklistHandler = new BlacklistAPI();
        blacklistHandler.setup(Config.getInstance());
        blacklistHandler.start();

        Blacklist blacklist = blacklistHandler.get("name","ip","uuid");
        if(blacklist != null){
            // Player is blacklisted
        }
    }

}
