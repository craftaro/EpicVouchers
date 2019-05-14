package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.FastInv;
import com.songoda.epicvouchers.menus.sub.action.ForceMenu;
import com.songoda.epicvouchers.menus.sub.action.GiveMenu;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.songoda.epicvouchers.utils.Methods.format;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.BARRIER;

public class ActionMenu extends FastInv {

    public ActionMenu(EpicVouchers instance, Voucher voucher) {
        super(27, "Give menu");

        addItem(10, new ItemBuilder(Material.FEATHER)
                .name(format("&6Give voucher"))
                .lore(format("&eGive the voucher to a"),
                        format("&especific player."))
                .build(), event -> new GiveMenu(instance, voucher).open(event.getPlayer()));

        addItem(14, new ItemBuilder(Material.ANVIL)
                .name(format("&6Force voucher"))
                .lore(format("&eForce the redeeming of the"),
                        format("&evoucher on a specific player."))
                .build(), event -> new ForceMenu(instance, voucher).open(event.getPlayer()));

        addItem(12, new ItemBuilder(Material.FEATHER)
                .name(format("&6Give all voucher"))
                .lore(format("&eGive the voucher to all"),
                        format("&eonline players."))
                .build(), event -> new ConfirmMenu(instance,
                () -> {
                    voucher.giveAll(event.getPlayer(), 1);
                    open(event.getPlayer());
                }, () -> open(event.getPlayer())).open(event.getPlayer()));

        addItem(16, new ItemBuilder(Material.ANVIL)
                .name(format("&6Force all voucher"))
                .lore(format("&eForce the redeeming of the"),
                        format("&evoucher on all online players."))
                .build(), event -> new ConfirmMenu(instance,
                () -> {
                    voucher.forceRedeem(event.getPlayer(), new ArrayList<>(Bukkit.getOnlinePlayers()), 1);
                    open(event.getPlayer());
                }, () -> open(event.getPlayer())).open(event.getPlayer()));

        addItem(18, new ItemBuilder(BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .addGlow().build(), event -> new VoucherMenu(instance).open(event.getPlayer()));

        if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
            ItemStack fillItem = instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ? new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                    new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);

            fill(new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
        }
    }
}
