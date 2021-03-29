package pl.dkcode.blacklist.objects;

import lombok.Data;

/**
 * @Author: Kacper 'DeeKaPPy' Horbacz
 * @Created: 29.03.2021
 * @Class: Blacklist
 */

@Data
public class Blacklist {

    private String username;
    private String ipAddress;
    private String uuid;
    private String admin;
    private String server;

    public Blacklist(String name, String ipAddress, String uuid, String admin, String server){
        this.username = name;
        this.ipAddress = ipAddress;
        this.uuid = uuid;
        this.admin = admin;
        this.server = server;
    }

}
