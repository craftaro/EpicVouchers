package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.FastInv;
import com.songoda.epicvouchers.menus.sub.editor.SetItemMenu;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import static com.songoda.epicvouchers.utils.Methods.format;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.*;

public class OptionMenu extends FastInv {

    public OptionMenu(EpicVouchers instance, Voucher voucher) {
        super(27, "Options: " + voucher.getKey());

        addItem(13, new ItemBuilder(voucher.toItemStack()).name(format(voucher.getName(true))).build());

        addItem(18, new ItemBuilder(BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .addGlow().build(), event -> new VoucherMenu(instance).open(event.getPlayer()));

        addItem(0, new ItemBuilder(FEATHER)
                .name(format("&6Voucher actions"))
                .lore(format("&eGive or redeem your voucher"))
                .addGlow()
                .build(), event -> new ActionMenu(instance, voucher).open(event.getPlayer()));

        addItem(26, new ItemBuilder(STONE)
                .name(format("&6Set item"))
                .lore(format("&eSet the item of your voucher."))
                .addGlow().build(), event -> new SetItemMenu(instance, voucher).open(event.getPlayer()));

        addItem(8, new ItemBuilder(BOOK)
                .name(format("&6Change all options"))
                .lore(format("&eSet the options of your voucher."))
                .build(), event -> new VoucherEditorMenu(instance, voucher).open(event.getPlayer()));

        fill(new ItemBuilder(instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ?
                new ItemStack(valueOf("GRAY_STAINED_GLASS_PANE")) :
                new ItemStack(valueOf("STAINED_GLASS_PANE"), 1, (short) 7)).name(ChatColor.RESET.toString()).build());
    }

}
