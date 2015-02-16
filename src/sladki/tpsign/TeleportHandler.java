package sladki.tpsign;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportHandler {

	private Teleport plugin;

	private Player player = null;
	private Location tpLocation = null;
	private String biome = null;
	private int attempts = 0;

	public TeleportHandler(Teleport pluginArg, Player playerArg, World world, int x, int z) {
		plugin = pluginArg;
		player = playerArg;
		tpLocation = new Location(world, 0, 0, 0);

		boolean result = setLocation(x, z);

		if (!result) {
			TeleportMapHandler.playerRemove(player);
			return;
		}

		// Scheduling teleporting
		// A client need to load a chunk first for not to stuck, so teleporting
		// is looped until a player will not have a "not air" block under his
		// feets
		new BukkitRunnable() {
			@Override
			public void run() {
				if (player.isOnline()) {
					player.teleport(tpLocation);
					TeleportMapHandler.playerRemove(player);
					if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
						if (player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
							plugin.getLogger().info(
									player.getDisplayName() + " has teleported.\n			X:" + tpLocation.getX() + " Y:"
											+ tpLocation.getY() + " Z:" + tpLocation.getZ() + "\n			Attempts: "
											+ attempts + " Biome:" + biome);
							this.cancel();
						}
					}
				} else {
					TeleportMapHandler.playerRemove(player);
					this.cancel();
				}
			}
		}.runTaskTimer(pluginArg, 20, 1);
	}

	// Finding a new location to teleport.
	// Randomizing new x-coordinate and z-coordinate first, calculating new
	// y-coordinate and comparing it with MIN_ and MAX_ heights.
	// After that we are getting new biome and the upper block, looking for
	// their suitability (banned biomes and blocks to spawn).
	// If new location is not suitable, trying to get a new one.
	// Returns true if success
	protected boolean setLocation(int x, int z) {
		int minDistance = (int) plugin.configHandler.get("MIN_DISTANCE");
		List bannedBiomes = (List) plugin.configHandler.get("BannedBiomes");
		List bannedBlocks = (List) plugin.configHandler.get("BannedBlocks");
		int counter = 0;

		Random random = new Random();

		if (minDistance >= (int) plugin.configHandler.get("MAX_X")
				|| minDistance >= (int) plugin.configHandler.get("MAX_Z")) {
			plugin.getLogger()
					.info("MIN_DISTANCE must be lesser than MAX_X or MAX_Z. Change the values in the config.");
			return false;
		}

		while (true) {
			counter++;

			if (counter > 30) {
				plugin.getLogger().info("30 attempts! Something is going wrong. Enable debug or check the config.");
				tpLocation = player.getLocation();
				return false;
			}

			int newZ = 0;
			int newX = random.nextInt((int) plugin.configHandler.get("MAX_X") + 1);
			if (newX <= minDistance) {
				newZ = random.nextInt((int) plugin.configHandler.get("MAX_Z") - minDistance) + minDistance;
			} else {
				newZ = random.nextInt((int) plugin.configHandler.get("MAX_Z") + 1);
			}

			boolean upperHalf = random.nextBoolean();
			boolean rightHalf = random.nextBoolean();
			if (!upperHalf) {
				newZ = -newZ;
			}
			if (!rightHalf) {
				newX = -newX;
			}

			newX = newX + x;
			newZ = newZ + z;

			int newY = tpLocation.getWorld().getHighestBlockYAt(newX, newZ);

			if ((Boolean) plugin.configHandler.get("DEBUG")) {
				plugin.getLogger().info("New location X: " + newX + " Y: " + newY + " Z: " + newZ);
			}

			if (newY < (int) plugin.configHandler.get("MIN_HEIGHT")
					|| newY > (int) plugin.configHandler.get("MAX_HEIGHT")) {
				continue;
			}

			tpLocation.setX(newX + 0.5);
			tpLocation.setZ(newZ + 0.5);
			tpLocation.setY(newY);

			biome = tpLocation.getBlock().getBiome().name();
			if (bannedBiomes.contains(biome)) {
				if ((Boolean) plugin.configHandler.get("DEBUG")) {
					plugin.getLogger().info("Biome: " + biome);
				}
				continue;
			}

			String block = tpLocation.getWorld().getHighestBlockAt(newX, newZ).getRelative(BlockFace.DOWN).getType()
					.name();
			if (bannedBlocks.contains(block)) {
				if ((Boolean) plugin.configHandler.get("DEBUG")) {
					plugin.getLogger().info("Block name: " + block);
				}
				continue;
			}

			if ((Boolean) plugin.configHandler.get("DEBUG")) {
				plugin.getLogger().info("Teleport location has set with " + counter + " attempts.");
				plugin.getLogger().info("Block: "+block+" biome: "+biome);
			}
			attempts = counter;
			return true;
		}
	}
}
