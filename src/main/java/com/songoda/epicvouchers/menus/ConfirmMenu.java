package com.songoda.epicvouchers.menus;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.FastInv;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;

public class ConfirmMenu extends FastInv {
    public ConfirmMenu(EpicVouchers instance, Runnable success, Runnable failure) {
        super(27, instance.getLocale().getMessage("interface.confirmsettings.title").getMessage());

        addItem(11, new ItemBuilder(Material.EMERALD)
                .name(instance.getLocale().getMessage("interface.confirmsettings.confirmitemname").getMessage())
                .lore(instance.getLocale().getMessage("interface.confirmsettings.confirmitemlore").getMessage())
                .addGlow().build(), event -> {
            event.getPlayer().closeInventory();
            success.run();
        });

        addItem(15, new ItemBuilder(Material.REDSTONE_BLOCK)
                .name(instance.getLocale().getMessage("interface.confirmsettings.cancelitemname").getMessage())
                .lore(instance.getLocale().getMessage("interface.confirmsettings.cancelitemlore").getMessage())
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> {
            failure.run();
            event.getPlayer().closeInventory();
        });

        if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
            ItemStack fillItem = CompatibleMaterial.GRAY_STAINED_GLASS_PANE.getItem();

            fill(new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
        }
    }
}
