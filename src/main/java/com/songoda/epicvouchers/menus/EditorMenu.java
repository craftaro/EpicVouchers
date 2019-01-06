package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.AbstractAnvilGUI;
import com.songoda.epicvouchers.libraries.FastInv;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.utils.Methods;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.utils.SoundUtils;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static com.songoda.epicvouchers.libraries.AbstractAnvilGUI.AnvilSlot.INPUT_LEFT;
import static org.bukkit.Material.NAME_TAG;
import static org.bukkit.Material.PAPER;
import static org.bukkit.Material.STONE;
import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;

public class EditorMenu extends FastInv {

    public EditorMenu(EpicVouchers instance, Voucher voucher) {
        super(27, instance.getLocale().getMessage("interface.editvoucher.title").replaceAll("%voucher%", voucher.getKey()));

        addItem(13, new ItemBuilder(voucher.toItemStack()).name(Methods.formatText(voucher.getName(true))).build());

        addItem(18, new ItemBuilder(Material.BARRIER)
                .name(instance.getLocale().getMessage("interface.editvoucher.backtitle"))
                .lore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.backlore")))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> new VoucherMenu(instance).open(event.getPlayer()));

        addItem(0, new ItemBuilder(Material.FEATHER)
                .name(instance.getLocale().getMessage("interface.editvoucher.recivetitle"))
                .lore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.recivelore")))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> {
                    SoundUtils.playSound(event.getPlayer(), "LEVEL_UP", 1);
                    event.getPlayer().getInventory().addItem(voucher.toItemStack());
                    String message = instance.getLocale().getMessage("interface.editvoucher.recivemessage").replaceAll("%voucher%", voucher.getName(true));
                    event.getPlayer().sendMessage(message);
        });

        addItem(8, new ItemBuilder(NAME_TAG)
                .name(instance.getLocale().getMessage("interface.editvoucher.renametitle"))
                .lore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.renamelore")))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> {
            SoundUtils.playSound(event.getPlayer(), "NOTE_BASS", 1);
            event.getPlayer().sendMessage(instance.getLocale().getMessage("interface.editvoucher.renamemessage"));
            AbstractAnvilGUI gui = new AbstractAnvilGUI(instance, event.getPlayer(), anvilEvent -> {
                voucher.setName(anvilEvent.getName());
                event.getPlayer().sendMessage(Methods.formatText(instance.getLocale().getMessage("interface.editvoucher.renamefinish", anvilEvent.getName())));
                new EditorMenu(instance, voucher).open(event.getPlayer());
                instance.saveToFile(voucher);
            });

            gui.setOnClose((player, inv) -> new EditorMenu(instance, voucher).open(player));
            gui.setSlot(INPUT_LEFT, voucher.toItemStack());
            gui.open();
        });

        addItem(2, new ItemBuilder(STONE)
                .name(instance.getLocale().getMessage("interface.editvoucher.itemtitle"))
                .lore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.itemlore")))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> new SetItemMenu(instance, voucher).open(event.getPlayer()));

        addItem(6, new ItemBuilder(PAPER)
                .name(instance.getLocale().getMessage("interface.boolean.itemtitle"))
                .lore(Collections.singletonList(instance.getLocale().getMessage("interface.boolean.itemlore")))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> new BooleanEditor(instance, voucher).open(event.getPlayer()));


        if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
            ItemStack fillItem = instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ? new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                    new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);

            fill(new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
        }

    }

}
