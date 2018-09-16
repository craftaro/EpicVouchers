package nl.marido.deluxevouchers.handlers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreventHacks implements Listener {

	public static HashMap<UUID, String> commands = new HashMap<UUID, String>();

	@EventHandler
	public void preventCommands(PlayerCommandPreprocessEvent event) {
		if (commands.containsKey(event.getPlayer().getUniqueId())) {
			if (!event.getMessage().equalsIgnoreCase(commands.get(event.getPlayer().getUniqueId()))) {
				event.setCancelled(true);
			}
		}
	}

}