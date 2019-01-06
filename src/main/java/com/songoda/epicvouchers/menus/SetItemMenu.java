package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.FastInv;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;

public class SetItemMenu extends FastInv {
    private final EpicVouchers instance;

    public SetItemMenu(EpicVouchers instance, Voucher voucher) {
        super(27, instance.getLocale().getMessage("interface.setitem.title"));
        this.instance = instance;

        setDefaultCancel(false);

        ItemStack fillItem = instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ? new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
        fill(fillItem, event -> event.setCancelled(true));

        addItem(13, null);

        addItem(18, new ItemBuilder(Material.BARRIER)
                .name(instance.getLocale().getMessage("interface.editvoucher.backtitle"))
                .lore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.backlore")))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> {
            event.setCancelled(true);
            new EditorMenu(instance, voucher).open(event.getPlayer());
        });

        addItem(26, new ItemBuilder(Material.ANVIL).name(ChatColor.GREEN + "Confirm").build(), event -> {
            event.setCancelled(true);

            if (event.getInventory().getInventory().getItem(13) == null) {
                new EditorMenu(instance, voucher).open(event.getPlayer());
                return;
            }

            ItemStack itemStack = event.getInventory().getInventory().getItem(13);

            voucher.setMaterial(itemStack.getType());

            if (itemStack.getDurability() != 0) {
                voucher.setData(itemStack.getDurability());
            }

            if (!itemStack.hasItemMeta()) {
                new EditorMenu(instance, voucher).open(event.getPlayer());
                return;
            }

            if (itemStack.getItemMeta().hasDisplayName()) {
                voucher.setName(itemStack.getItemMeta().getDisplayName());
            }

            if (itemStack.getItemMeta().hasLore()) {
                voucher.setLore(itemStack.getItemMeta().getLore());
            }

            new EditorMenu(instance, voucher).open(event.getPlayer());
            instance.saveToFile(voucher);

        });

    }
}
