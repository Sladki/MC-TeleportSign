package sladki.tpsign;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

	private static Teleport plugin;

	public ConfigHandler(Teleport instance) {
		plugin = instance;
		plugin.getConfig();
		plugin.saveDefaultConfig();
	}

	public static void reloadConfig() {
		plugin.reloadConfig();
	}

	public static Object get(String value) {
		FileConfiguration config = plugin.getConfig();
		return config.get("Teleport." + value);
	}

}
