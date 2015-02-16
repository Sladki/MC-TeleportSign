package sladki.tpsign;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

	private final Teleport plugin;

	public ConfigHandler(Teleport instance) {
		plugin = instance;
		plugin.getConfig();
		plugin.saveDefaultConfig();
	}

	public void ReloadConfig() {
		plugin.reloadConfig();
	}

	public Object get(String value) {
		FileConfiguration config = plugin.getConfig();
		return config.get("Teleport." + value);
	}

}
