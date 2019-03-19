package com.songoda.epicvouchers.menus.sub.action;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.inventory.PlayersMenu;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;

public class GiveMenu extends PlayersMenu {
    public GiveMenu(EpicVouchers instance, Voucher voucher) {
        super(instance, voucher, "Give Menu", (who, player) -> voucher.give(who, Collections.singletonList(player), 1), new ArrayList<>(Bukkit.getOnlinePlayers()));
    }
}