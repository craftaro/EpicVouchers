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

public class EffectsMenu extends IconInv {
    public EffectsMenu(EpicVouchers instance, Voucher voucher) {
        super(9, "Titles");

        addIcon(1, new StringIcon(instance, "Effect", voucher.getEffect(), (player, editString) -> {
            voucher.setEffect(editString);
            new EffectsMenu(instance, voucher).open(player);
        }));

        addIcon(2, new IntegerIcon(instance, "Effect Amplifier", voucher.getEffectAmplifier(), (player, number) -> {
            voucher.setEffectAmplifier(number);
            new EffectsMenu(instance, voucher).open(player);
        }));

        addIcon(3, new IntegerIcon(instance, "Duration", voucher.getEffectDuration(), (player, number) -> {
            voucher.setEffectDuration(number);
            new EffectsMenu(instance, voucher).open(player);
        }));

        addIcon(0, new ItemBuilder(Material.BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .build(), event -> new VoucherEditorMenu(instance, voucher).open(event.getPlayer()));
    }
}
