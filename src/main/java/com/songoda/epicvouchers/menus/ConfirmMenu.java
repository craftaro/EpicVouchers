package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.inventory.FastInv;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.utils.SoundUtils;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;

public class ConfirmMenu extends FastInv {
    public ConfirmMenu(EpicVouchers instance, Voucher voucher) {
        super(27, instance.getLocale().getMessage("interface.confirmsettings.title"));

        addItem(11, new ItemBuilder(Material.EMERALD)
                .name(instance.getLocale().getMessage("interface.confirmsettings.confirmitemname"))
                .lore(instance.getLocale().getMessage("interface.confirmsettings.confirmitemlore"))
                .addGlow().build(), event -> {
            event.getPlayer().closeInventory();
            instance.getVoucherExecutor().redeemVoucher(event.getPlayer(), voucher, event.getPlayer().getItemInHand(), true);
        });

        addItem(15, new ItemBuilder(Material.REDSTONE_BLOCK)
                .name(instance.getLocale().getMessage("interface.confirmsettings.cancelitemname"))
                .lore(instance.getLocale().getMessage("interface.confirmsettings.cancelitemlore"))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> {
            SoundUtils.playSound(event.getPlayer(), "CLICK", 1);
            event.getPlayer().closeInventory();
        });

        if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
            ItemStack fillItem = instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ? new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                    new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);

            fill(new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
        }
    }
}
