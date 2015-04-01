package sladki.tpsign;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeleportListener implements Listener {

	private final Teleport plugin;
	private long lastUsedTime;

	public TeleportListener(Teleport instance) {
		plugin = instance;
	}

	// Only OP can place a TP sign
	// A TP sign must contain only "[TP]" in the 4th line to work
	@EventHandler
	public void onSignCreate(SignChangeEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (block.getType().equals(Material.SIGN) || block.getType().equals(Material.SIGN_POST)
				|| block.getType().equals(Material.WALL_SIGN)) {
			if (!event.getLine(3).equalsIgnoreCase("[TP]")) {
				return;
			}

			if (!player.hasPermission("teleport.sign.create")) {
				player.sendMessage(ChatColor.RED + "You don't have permission to create TP signs");
				event.setCancelled(true);
				block.breakNaturally();
				return;
			}

			player.sendMessage(ChatColor.GREEN + "You have made a TP sign");
			plugin.getLogger().info("A TP sign has created");
		}
	}

	// Checking is player using a TP sign
	// If he is, adds his ID to HashMap and make him teleport
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.isCancelled() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			if (block.getType().equals(Material.SIGN) || block.getType().equals(Material.SIGN_POST)
					|| block.getType().equals(Material.WALL_SIGN)) {
				Sign signState = (Sign) block.getState();

				if (!signState.getLine(3).equalsIgnoreCase("[TP]")) {
					return;
				}
				if (TeleportMapHandler.playerCheck(player)) {
					return;
				}
				
				if((int)ConfigHandler.get("COOLDOWN") > 0) {
					int cooldownSecondsLeft = -(int)((System.currentTimeMillis() - lastUsedTime - (int)ConfigHandler.get("COOLDOWN") * 1000) * 0.001);
					if(cooldownSecondsLeft > 0) {
						player.sendMessage(ChatColor.GRAY + "Please wait " + cooldownSecondsLeft + " sec before you'll be able to teleport");
						return;
					}
					lastUsedTime = System.currentTimeMillis();
				}

				player.sendMessage(ChatColor.GREEN + "Teleporting...");
				TeleportMapHandler.playerAdd(player);
				new TeleportHandler(plugin, player, block.getWorld(), block.getX(),
						block.getZ());
			}
		}
	}

}