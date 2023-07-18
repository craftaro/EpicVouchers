package com.craftaro.epicvouchers.menus.sub.editor;

import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.libraries.inventory.IconInv;
import com.craftaro.epicvouchers.libraries.inventory.icons.IntegerIcon;
import com.craftaro.epicvouchers.libraries.inventory.icons.StringIcon;
import com.craftaro.epicvouchers.menus.VoucherEditorMenu;
import com.craftaro.epicvouchers.voucher.Voucher;
import org.bukkit.Material;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;

public class TitlesMenu extends IconInv {
    public TitlesMenu(EpicVouchers instance, Voucher voucher) {
        super(9, "Titles");

        addIcon(1, new StringIcon(instance, "Title", voucher.getTitle(), (player, editString) -> {
            voucher.setTitle(editString);
            new TitlesMenu(instance, voucher).open(player);
        }));

        addIcon(2, new StringIcon(instance, "Subtitle", voucher.getSubTitle(), (player, editString) -> {
            voucher.setSubTitle(editString);
            new TitlesMenu(instance, voucher).open(player);
        }));

        addIcon(3, new IntegerIcon(instance, "Fade in", voucher.getTitleFadeIn(), (player, number) -> {
            voucher.setTitleFadeIn(number);
            new TitlesMenu(instance, voucher).open(player);
        }));

        addIcon(4, new IntegerIcon(instance, "Fade out", voucher.getTitleFadeOut(), (player, number) -> {
            voucher.setTitleFadeOut(number);
            new TitlesMenu(instance, voucher).open(player);
        }));

        addIcon(5, new IntegerIcon(instance, "Stay", voucher.getTitleStay(), (player, number) -> {
            voucher.setTitleStay(number);
            new TitlesMenu(instance, voucher).open(player);
        }));

        addIcon(0, new ItemBuilder(Material.BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .build(), event -> new VoucherEditorMenu(instance, voucher).open(event.getPlayer()));
    }
}
