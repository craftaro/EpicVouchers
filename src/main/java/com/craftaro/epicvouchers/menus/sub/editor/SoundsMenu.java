package com.craftaro.epicvouchers.menus.sub.editor;

import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.libraries.inventory.IconInv;
import com.craftaro.epicvouchers.libraries.inventory.icons.IntegerIcon;
import com.craftaro.epicvouchers.libraries.inventory.icons.StringIcon;
import com.craftaro.epicvouchers.menus.VoucherEditorMenu;
import com.craftaro.epicvouchers.voucher.Voucher;
import com.google.common.base.Enums;
import org.bukkit.Material;
import org.bukkit.Sound;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;

public class SoundsMenu extends IconInv {
    public SoundsMenu(EpicVouchers instance, Voucher voucher) {
        super(9, "Sound");

        addIcon(1, new StringIcon(instance, "Sound", voucher.getSound(), (player, editString) -> {
            voucher.setSound(editString);
            new SoundsMenu(instance, voucher).open(player);
        }, string -> Enums.getIfPresent(Sound.class, string).isPresent()));

        addIcon(2, new IntegerIcon(instance, "Pitch", voucher.getSoundPitch(), (player, number) -> {
            voucher.setSoundPitch(number);
            new SoundsMenu(instance, voucher).open(player);
        }));

        addIcon(0, new ItemBuilder(Material.BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .build(), event -> new VoucherEditorMenu(instance, voucher).open(event.getPlayer()));
    }
}
