package sladki.tpsign;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class TeleportMapHandler {

	private static Teleport plugin;
	private static Map playerMap = new HashMap();

	public TeleportMapHandler(Teleport instance) {
		plugin = instance;
	}

	public static void playerAdd(Player player) {
		String playerId = player.getUniqueId().toString();

		if (!playerMap.containsKey(playerId)) {
			playerMap.put(playerId, true);
		}
	}

	public static void playerRemove(Player player) {
		String playerId = player.getUniqueId().toString();

		if (!playerMap.isEmpty()) {
			if (playerMap.containsKey(playerId)) {
				playerMap.remove(playerId);
			}
		}
	}

	public static boolean playerCheck(Player player) {
		String playerId = player.getUniqueId().toString();

		if (!playerMap.isEmpty()) {
			if (playerMap.containsKey(playerId)) {
				return true;
			}
		}
		return false;
	}
}
