package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.FastInv;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.utils.Methods;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.utils.SoundUtils;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VoucherMenu extends FastInv {

    public VoucherMenu(EpicVouchers instance) {
        super(Math.ceil(instance.getVoucherManager().getVouchers().size() / 9.0) > 81 ? 81 : (int) Math.ceil(instance.getVoucherManager().getVouchers().size() / 9.0) * 9,
                instance.getLocale().getMessage("interface.editor.title"));

        for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
            ItemBuilder item = new ItemBuilder(voucher.toItemStack());
            item.name(item.getName() + Methods.formatText(" &b&l[CLICK TO EDIT]"));

            if (getInventory().firstEmpty() != -1) {
                addItem(getInventory().firstEmpty(), item.build(), event -> {
                    SoundUtils.playSound(event.getPlayer(), "NOTE_PIANO", 1);
                    new EditorMenu(instance, voucher).open(event.getPlayer());
                });
            }
        }

        if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
            ItemStack fillItem = instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ? new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                    new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);

            fill(new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
        }
    }

}
