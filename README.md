# MC-TeleportSign
Minecraft Bukkit plugin for creating teleporting signs.

https://github.com/Sladki/MC-TeleportSign/raw/master/jar/TeleportSign-1.0.jar

Place a sign and enter "[TP]" into the 4th line (first three lines can contain any text). You need to be an OP to place tp signs.
When used, the sign will choose random coordinates in a rectangle with 2xMAX_X width and 2xMAX_Z height blocks with the sign in the center. Signs can have minimal distance to teleport, so they will not teleport a player in a radius of MIN_DISTANCE.
The config file will be created in plugin/TeleportSign folder after the first launch of server with installed plugin.
Use '/teleportreloadconfig' command in server console to reload the plugin config.
The plugin have a list of "banned" biomes and blocks to spawn at. This list can be edited in the config file.
Change COOLDOWN value in the config to add cooldown.