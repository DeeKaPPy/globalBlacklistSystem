# Global blacklist system.

A global minecraft blacklist system using a java and node Rest API to 
check/ add/ remove blacklist, depending on server privileges. 

## Usage

You can use this API in two different ways: 
- use the Minecraft [Java API](#java-api)
- create your own Rest client based on the [Rest API](#rest-api) and only use our server.

## TODO List:
- [X] MongoDB connection,
- [X] Create node router to get blacklists list,
- [X] Create node router to add blacklist,
- [X] Create node router to remove blacklist,
- [X] Create node router to get blacklists by server,
- [X] Create authentication system,
- [X] Create java plugin to check is a player is blacklisted.

## Starting the server
To start the server you'll need a mongodb database, nodejs and npm.

Temp start: ```npm run start```
Screen start ```screen -dmS <screen_name> npm run start```

## Rest API

 **Get blacklists** - ```GET /api/blacklists``` \
 Return list of blacklist objects. 
 ```
 [
     {
         "username":"TestUser", # Clients nickname
         "ipAddress: "11.22.33.44", # Clients ip address
         "uuid": "abasdabqwbefsdbfsewbfwea@1", # Clients uuid
         "admin": "DeeKaPPy", # administrator that the client banned
         "server": "dkcode.pl" # Server that blacklist was given on
     }
 ]
 ```  
 **Get blacklist details** - ```GET /api/blacklists/:username``` \
 Returns blacklist object.
 ```
 {
    "username":"TestUser", # Clients nickname
    "ipAddress: "11.22.33.44", # Clients ip address
    "uuid": "abasdabqwbefsdbfsewbfwea@1", # Clients uuid
    "admin": "DeeKaPPy", # administrator that the client banned
    "server": "dkcode.pl" # Server that blacklist was given on
}
 ```
 **Get blacklist by server** - ```GET /api/blacklists/server/:server``` \
 Return list of blacklist objects. 
 ```
 [
    {
        "username":"TestUser", # Clients nickname
        "ipAddress: "11.22.33.44", # Clients ip address
        "uuid": "abasdabqwbefsdbfsewbfwea@1", # Clients uuid
        "admin": "DeeKaPPy", # administrator that the client banned
        "server": "dkcode.pl" # Server that blacklist was given on
    }
 ]
 ```
 **Add blacklist** - ```POST /api/blacklist/add```\
  Parameters:
```
{
    "secret": "SUPER_SECRET_CODE",
    "username": "testUsername",
    "ipAddress": "11.22.33.44",
    "uuid": "abasdabqwbefsdbfsewbfwea@1",
    "admin": "DeeKaPPy",
    "server": "dkcode.pl"
}
```
 Returns:
 ```
 # SUCCESS
 {
     "code": 200,
     "message": "Blacklist added"
 }

 # ERROR
 {
     "code": 500,
     "message": "Contact server administrator"
 }
 ```
 **Remove blacklist** - ```POST /api/blacklist/remove``` \
 Parameters: 
```
{
    "username": "testUsername"
}
```
 Returns:
```
 # SUCCESS
 {
     "code": 200,
     "message": "Blacklist removed"
 }

 # ERROR
 {
     "code": 500,
     "message": "Contact server administrator"
 }
 ```

 ## Java API
 To use the Java API build the dkBlacklist plugin from the java source and put it in your plugins folder, add it to your plugin as a dependancy.
 
In your main class init the **BlacklistAPI**
```
@Override
public void onEnable() {
    BlacklistAPI blacklistHandler = new BlacklistAPI();
}
 ``` 

 Creating blacklist:
```
blacklistHandler.create("name","ip","uuid","admin","server")
```

 Removing blacklist:
```
blacklistHandler.remove("name")
```
 Checking if player is blacklisted
```
Blacklist blacklist = blacklistHandler.get("name","ip","uuid");
if(blacklist != null){
    // Player is blacklisted
}
```
