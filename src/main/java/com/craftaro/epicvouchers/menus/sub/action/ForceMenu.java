package com.craftaro.epicvouchers.menus.sub.action;

import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.inventory.PlayersMenu;
import com.craftaro.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;

public class ForceMenu extends PlayersMenu {
    public ForceMenu(EpicVouchers instance, Voucher voucher) {
        super(instance, voucher, "Force Menu", (who, player) -> voucher.forceRedeem(who, Collections.singletonList(player), 1), new ArrayList<>(Bukkit.getOnlinePlayers()));
    }
}
