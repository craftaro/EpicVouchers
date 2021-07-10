package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.EpicVouchers;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CoolDownManager {
    private final Map<UUID, Long> entries = new HashMap<>();
    private final EpicVouchers instance;

    public CoolDownManager(EpicVouchers instance) {
        this.instance = instance;
    }

    void addCoolDown(final UUID uuid, Voucher voucher) {
        if (Bukkit.getPlayer(uuid).hasPermission("epicvouchers.bypass")) {
            return;
        }

        if (voucher.getCoolDown() != 0) {
            entries.put(uuid, System.currentTimeMillis() + voucher.getCoolDown() * 1000L);
        } else {
            entries.put(uuid, System.currentTimeMillis() + instance.getConfig().getInt("Main.Cooldown Delay") * 1000L);
        }
    }

    public boolean isOnCoolDown(UUID uuid) {
        Long time = entries.get(uuid);

        if (time == null) {
            return false;
        }

        if (time >= System.currentTimeMillis()) {
            return true;
        }

        entries.remove(uuid);
        return false;
    }

    public long getTime(UUID uuid) {
        Long time = entries.get(uuid);

        if (time == null) {
            return 0L;
        }

        return (time - System.currentTimeMillis()) / 1000;
    }
}
