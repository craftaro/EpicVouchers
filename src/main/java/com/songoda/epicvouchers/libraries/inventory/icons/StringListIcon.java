package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.menus.sub.editor.StringListMenu;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class StringListIcon extends Icon {
    public StringListIcon(EpicVouchers instance, List<String> list, String name, Voucher voucher) {
        super(new ItemBuilder(Material.BOOK)
                .name(ChatColor.YELLOW + name)
                .lore(ChatColor.GRAY + "Click to view")
                .build(), event -> new StringListMenu(instance, name, list, name, voucher).open(event.getPlayer()));
    }
}
