package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.utils.Debugger;
import com.songoda.epicvouchers.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Cooldowns {

	private HashMap<UUID, Integer> entries = new HashMap<UUID, Integer>();

	void addCooldown(final UUID uuid, Voucher voucher) {
		try {
			if (Bukkit.getPlayer(uuid).hasPermission("epicvouchers.bypass")) {
				return;
			}
			if (voucher.getCooldown() != 0) {
				entries.put(uuid, voucher.getCooldown());
			} else {
				entries.put(uuid, EpicVouchers.getInstance().getConfig().getInt("Main.Cooldown Delay"));
			}
			new BukkitRunnable() {
				public void run() {
					if (entries.get(uuid) <= 0 && entries.containsKey(uuid)) {
						entries.remove(uuid);
						cancel();
					} else {
						entries.put(uuid, entries.get(uuid) - 1);
					}
				}
			}.runTaskTimer(EpicVouchers.getInstance(), 0, 20);
		} catch (Exception error) {
			System.out.println(Methods.formatText("&cFailed to add cooldown to the UUID " + uuid + "."));
			Debugger.runReport(error);
		}
	}

    HashMap<UUID, Integer> getEntries() {
        return new HashMap<>(entries);
    }
}