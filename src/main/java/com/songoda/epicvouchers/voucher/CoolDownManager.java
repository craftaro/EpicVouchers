package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.EpicVouchers;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class CoolDownManager {

    private final HashMap<UUID, Long> entries = new HashMap<>();
    private final EpicVouchers instance;

    public CoolDownManager(EpicVouchers instance) {
        this.instance = instance;
    }

    void addCooldown(final UUID uuid, Voucher voucher) {
        if (Bukkit.getPlayer(uuid).hasPermission("epicvouchers.bypass")) {
            return;
        }

        if (voucher.getCooldown() != 0) {
            entries.put(uuid, System.currentTimeMillis() + voucher.getCooldown() * 1000);
        } else {
            entries.put(uuid, System.currentTimeMillis() + instance.getConfig().getInt("Main.Cooldown Delay") * 1000);
        }
    }

    public boolean isOnCoolDown(UUID uuid) {
        if(!entries.containsKey(uuid)) {
            return false;
        }

        if(entries.get(uuid) >= System.currentTimeMillis()) {
            return true;
        }

        entries.remove(uuid);
        return false;
    }

    public long getTime(UUID uuid) {
        if(!entries.containsKey(uuid)) {
            return 0L;
        }

        return (entries.get(uuid) - System.currentTimeMillis()) / 1000;
    }
}