package com.craftaro.epicvouchers.menus.sub.action;

import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.inventory.PlayersMenu;
import com.craftaro.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;

public class GiveMenu extends PlayersMenu {
    public GiveMenu(EpicVouchers instance, Voucher voucher) {
        super(instance, voucher, "Give Menu", (who, player) -> voucher.give(who, Collections.singletonList(player), 1), new ArrayList<>(Bukkit.getOnlinePlayers()));
    }
}
