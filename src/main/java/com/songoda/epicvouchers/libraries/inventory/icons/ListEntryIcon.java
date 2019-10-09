package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.core.gui.AnvilGui;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.utils.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.BiConsumer;

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

            AnvilGui gui = new AnvilGui(event.getPlayer());
            gui.setTitle("Current: " + entry);
            gui.setAction(aevent -> onEdit.accept(event.getPlayer(), new Pair<>(entry, gui.getInputText().trim())));
                instance.getGuiManager().showGUI(event.getPlayer(), gui);
            });


        }
    }
