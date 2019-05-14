package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.AbstractAnvilGUI;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.utils.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.BiConsumer;

import static com.songoda.epicvouchers.libraries.AbstractAnvilGUI.AnvilSlot.INPUT_LEFT;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.PAPER;

public class ListEntryIcon extends Icon {
    public ListEntryIcon(EpicVouchers instance, String entry, BiConsumer<Player, String> onRemove, BiConsumer<Player, Pair<String, String>> onEdit) {
        super(new ItemBuilder(PAPER).name(YELLOW + entry).lore(GRAY + "Right click to edit",
                GRAY + "Left click to remove").build(), event -> {
            if (event.getClickType() == ClickType.LEFT) {
                onRemove.accept(event.getPlayer(), entry);
                return;
            }

            AbstractAnvilGUI anvilGUI = new AbstractAnvilGUI(instance, event.getPlayer(), anvilEvent -> onEdit.accept(event.getPlayer(), new Pair<String, String>(entry, anvilEvent.getName())));

            anvilGUI.setSlot(INPUT_LEFT, new ItemBuilder(PAPER).name(entry).build());
            anvilGUI.open();
        });


    }
}
