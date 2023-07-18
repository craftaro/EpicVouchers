package com.craftaro.epicvouchers.voucher;

import com.craftaro.epicvouchers.EpicVouchers;
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
            this.entries.put(uuid, System.currentTimeMillis() + voucher.getCoolDown() * 1000L);
        } else {
            this.entries.put(uuid, System.currentTimeMillis() + this.instance.getConfig().getInt("Main.Cooldown Delay") * 1000L);
        }
    }

    public boolean isOnCoolDown(UUID uuid) {
        Long time = this.entries.get(uuid);

        if (time == null) {
            return false;
        }

        if (time >= System.currentTimeMillis()) {
            return true;
        }

        this.entries.remove(uuid);
        return false;
    }

    public long getTime(UUID uuid) {
        Long time = this.entries.get(uuid);

        if (time == null) {
            return 0L;
        }

        return (time - System.currentTimeMillis()) / 1000;
    }
}
