package sladki.tpsign;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Teleport extends JavaPlugin {

	public final Teleport plugin = this;
	public final ConfigHandler configHandler = new ConfigHandler(this);
	public final TeleportMapHandler teleportMapHandler = new TeleportMapHandler(this);

	private final TeleportListener teleportListener = new TeleportListener(this);

	@Override
	public void onEnable() {
		plugin.getLogger().info("Use /teleportreloadconfig to reload the config.");
		getServer().getPluginManager().registerEvents(teleportListener, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("teleportreloadconfig")) {
			if (!(sender instanceof Player)) {
				plugin.reloadConfig();
				plugin.getLogger().info("Config has reloaded");
			}
			return true;
		}
		return false;
	}

}