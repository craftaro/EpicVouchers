package nl.marido.deluxevouchers.vouchers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import nl.marido.deluxevouchers.DeluxeVouchers;
import nl.marido.deluxevouchers.handlers.DataHandler;

public class Cooldowns {

	public static HashMap<UUID, Integer> entries = new HashMap<UUID, Integer>();

	public static void addCooldown(final UUID uuid, String voucher) {
		try {
			if (Bukkit.getPlayer(uuid).hasPermission("deluxevouchers.bypass")) {
				return;
			}
			if (DataHandler.getString(DataHandler.vouchers, "vouchers." + voucher + ".cooldown") != null) {
				entries.put(uuid, DataHandler.getInt(DataHandler.vouchers, "vouchers." + voucher + ".cooldown"));
			} else {
				entries.put(uuid, DataHandler.cooldowndelay);
			}
			new BukkitRunnable() {
				public void run() {
					if (entries.get(uuid) <= 0) {
						entries.remove(uuid);
						cancel();
					} else {
						entries.put(uuid, entries.get(uuid) - 1);
					}
				}
			}.runTaskTimer(DeluxeVouchers.getInstance(), 0, 20);
		} catch (Exception error) {
			DeluxeVouchers.getConsole().sendMessage("§cFailed to add cooldown to the UUID " + uuid + ".");
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

}