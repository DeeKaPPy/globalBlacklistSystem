package pl.dkcode.blacklist.handlers;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import pl.dkcode.blacklist.objects.Blacklist;
import pl.dkcode.blacklist.objects.Config;
import pl.dkcode.blacklist.utils.HttpUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Kacper 'DeeKaPPy' Horbacz
 * @Created: 29.03.2021
 * @Class: BlacklistHandler
 */

public class BlacklistAPI {

    private static final Set<Blacklist> blacklists = new HashSet<>();
    ScheduledExecutorService service = new ScheduledThreadPoolExecutor(4);

    private String url;
    private String secret = null;
    private Integer delay;

    public BlacklistAPI(){ }

    public void setup(Config config) {
        this.url = config.url;
        this.delay = config.delay;
        Config.getInstance().toFile("./plugins/dkBlacklist/blacklists.json");
    }

    @Deprecated
    public void setup(String url, Integer delay){
        this.url = url;
        this.delay = delay;
    }

    public void start(){
        service.scheduleAtFixedRate(() -> {
            HttpUtil httpUtil = new HttpUtil();
            // getSecret
            String secretData = httpUtil.sendGet(url+"/api/blacklists/secret");
            JsonObject secretObject = new Gson().fromJson(secretData, JsonObject.class);
            String oldSecret = secret;
            if(secretObject.has("secret")) secret = secretObject.get("secret").getAsString();
            else secret = null;
            if((oldSecret == null && secret != null) || !oldSecret.equals(secret))
                System.out.println("[Blacklists] New secret key has been generated!");

            // getAllBlacklists
            String data = httpUtil.sendGet(url+"/api/blacklists");
            try {
                JsonArray object = new Gson().fromJson(data, JsonArray.class);
                blacklists.clear();
                object.forEach(element -> blacklists.add(new Gson().fromJson(element.getAsJsonObject(),Blacklist.class)));
            }catch (Exception e){
                JsonObject object = new Gson().fromJson(data, JsonObject.class);
                System.out.println(object);
            }
        },0,delay, TimeUnit.MILLISECONDS);
    }

    public Blacklist get(String name, String ip, String uuid){
        return blacklists.stream()
                .filter(blacklist -> blacklist.getUsername().equalsIgnoreCase(name) || blacklist.getIpAddress().equals(ip) || blacklist.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    public boolean create(String name, String ip, String uuid, String admin, String server){
        Blacklist blacklist = new Blacklist(name, ip,uuid,admin,server);
        blacklists.add(blacklist);
        JsonObject object = new Gson().fromJson(new Gson().toJson(blacklist),JsonObject.class);
        object.addProperty("secret",secret);
        try {
            String data = new HttpUtil().sendJsonPost(
                    url + "/api/blacklists/add",
                    object.toString()
            );
            return new Gson().fromJson(data,JsonObject.class).get("status").getAsInt() == 200;
        }catch (Exception e){
            return false;
        }
    }

    public boolean remove(String name){
        JsonObject object = new JsonObject();
        object.addProperty("username",name);
        object.addProperty("secret",secret);
        try {
            String data = new HttpUtil().sendJsonPost(
                    url + "/api/blacklists/remove",
                    object.toString()
            );
            return new Gson().fromJson(data,JsonObject.class).get("status").getAsInt() == 200;
        }catch (Exception e){
            return false;
        }
    }

}
