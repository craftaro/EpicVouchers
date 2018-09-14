package nl.marido.deluxevouchers.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import nl.marido.deluxevouchers.DeluxeVouchers;

public class UpdateHandler implements Listener {

	public static boolean oldversion = false;

	public static void checker() {
		if (DataHandler.checkupdates) {
			new BukkitRunnable() {
				public void run() {
					try {
						URL checkurl = new URL("https://api.spigotmc.org/legacy/update.php?resource=52480");
						URLConnection connection = checkurl.openConnection();
						String latestversion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
						String currentversion = DeluxeVouchers.getInstance().getDescription().getVersion();
						if (latestversion.equals(currentversion)) {
							DeluxeVouchers.printConsole("§fLatest version of DeluxeVouchers detected (" + currentversion + ").");
							oldversion = false;
						} else {
							DeluxeVouchers.printConsole("§cOutdated version of DeluxeVouchers detected (" + currentversion + ").");
							DeluxeVouchers.printConsole("§cDownload " + latestversion + ": https://spigotmc.org/resources/52480");
							oldversion = true;
						}
					} catch (Exception error) {
						DeluxeVouchers.printConsole("§cFailed to create a connection with the updater host.");
						if (DataHandler.debugerrors) {
							error.printStackTrace();
						}
					}
				}
			}.runTaskAsynchronously(DeluxeVouchers.getInstance());
		}
	}

	@EventHandler
	public void updateWarning(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("deluxevouchers.admin")) {
			if (oldversion) {
				SoundHandler.playSound(player, DataHandler.oldversionsound, DataHandler.oldversionpitch);
				for (String message : DataHandler.oldversionmessage) {
					message = message.replaceAll("%version%", DeluxeVouchers.getInstance().getDescription().getVersion());
					player.sendMessage(message);
				}
			}
		}
	}

}