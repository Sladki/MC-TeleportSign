# MC-TeleportSign
Minecraft Bukkit plugin for creating teleporting signs.

Place a sign and enter "[TP]" to the 4th line (first three lines can contain any text). You need to be an OP to place working signs.
When used, the sign will choose random coordinates in a rectangle with 2xMAX_X width and 2xMAX_Z height blocks with the sign in the center. Signs can have minimal distance to teleport, so they will not teleport a player in a radius of MIN_DISTANCE.
The config file will be created after the first initializing of the plugin in plugin/TeleportSign folder.
Use '/teleportreloadconfig' command in the server console to reload the plugin config.
The plugin have list of "banned" biomes and blocks to spawn. This list can be edited in the config file.
