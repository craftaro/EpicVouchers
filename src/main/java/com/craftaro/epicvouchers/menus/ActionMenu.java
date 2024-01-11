package com.craftaro.epicvouchers.menus;

import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.core.utils.TextUtils;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.libraries.inventory.FastInv;
import com.craftaro.epicvouchers.menus.sub.action.ForceMenu;
import com.craftaro.epicvouchers.menus.sub.action.GiveMenu;
import com.craftaro.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.BARRIER;

public class ActionMenu extends FastInv {
    public ActionMenu(EpicVouchers instance, Voucher voucher) {
        super(27, "Give menu");

        addItem(10, new ItemBuilder(Material.FEATHER)
                .name(TextUtils.formatText("&6Give voucher"))
                .lore(TextUtils.formatText("&eGive the voucher to a"),
                        TextUtils.formatText("&especific player."))
                .build(), event -> new GiveMenu(instance, voucher).open(event.getPlayer()));

        addItem(14, new ItemBuilder(Material.ANVIL)
                .name(TextUtils.formatText("&6Force voucher"))
                .lore(TextUtils.formatText("&eForce the redeeming of the"),
                        TextUtils.formatText("&evoucher on a specific player."))
                .build(), event -> new ForceMenu(instance, voucher).open(event.getPlayer()));

        addItem(12, new ItemBuilder(Material.FEATHER)
                .name(TextUtils.formatText("&6Give all voucher"))
                .lore(TextUtils.formatText("&eGive the voucher to all"),
                        TextUtils.formatText("&eonline players."))
                .build(), event -> new ConfirmMenu(instance,
                () -> {
                    voucher.giveAll(event.getPlayer(), 1);
                    open(event.getPlayer());
                }, () -> open(event.getPlayer())).open(event.getPlayer()));

        addItem(16, new ItemBuilder(Material.ANVIL)
                .name(TextUtils.formatText("&6Force all voucher"))
                .lore(TextUtils.formatText("&eForce the redeeming of the"),
                        TextUtils.formatText("&evoucher on all online players."))
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
            ItemStack fillItem = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();

            fill(new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
        }
    }
}
