package com.songoda.epicvouchers.menus.sub.editor;

import com.songoda.core.gui.AnvilGui;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.IconInv;
import com.songoda.epicvouchers.libraries.inventory.icons.ListEntryIcon;
import com.songoda.epicvouchers.menus.VoucherEditorMenu;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Material.PAPER;

public class StringListMenu extends IconInv {

    public StringListMenu(EpicVouchers instance, String key, List<String> list, String toEdit, Voucher voucher) {
        super((int) ((list.isEmpty() ? 9 : Math.ceil(list.size() / 9.0) * 9) + 9), key);
        int size = getInventory().getSize();

        addIcon(size - 9, new ItemBuilder(Material.BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .build(), event -> new VoucherEditorMenu(instance, voucher).open(event.getPlayer()));

        addIcon(size - 5, new ItemBuilder(Material.BOOK)
                .name(YELLOW + "Editing: " + GRAY + toEdit)
                .lore(list.stream().map(s -> DARK_GRAY + "- " + GRAY + s).collect(Collectors.toList()))
                .build());

        addIcon(size - 1, new ItemBuilder(PAPER).name(GREEN + "Add to list").build(), event -> {
            AnvilGui gui = new AnvilGui(event.getPlayer());
            gui.setAction(aevent -> {
                list.add(gui.getInputText().trim());
                voucher.saveSetting(key.toLowerCase(), list);
                new StringListMenu(instance, key, list, toEdit, voucher).open(event.getPlayer());
            });
            instance.getGuiManager().showGUI(event.getPlayer(), gui);
        });

        for (int i = 0; i < list.size(); i++) {
            String entry = list.get(i);
            addIcon(new ListEntryIcon(instance, entry, (player, removeString) -> {
                list.remove(removeString);
                voucher.saveSetting(key.toLowerCase(), list);
                new StringListMenu(instance, key, list, toEdit, voucher).open(player);
            }, (player, tuple) -> {
                list.set(list.indexOf(tuple.getKey()), tuple.getValue());
                voucher.saveSetting(key.toLowerCase(), list);
                new StringListMenu(instance, key, list, toEdit, voucher).open(player);
            }));
        }

    }
}
