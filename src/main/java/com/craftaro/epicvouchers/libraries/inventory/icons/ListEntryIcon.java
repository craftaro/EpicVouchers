package com.craftaro.epicvouchers.libraries.inventory.icons;

import com.craftaro.core.input.ChatPrompt;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.BiConsumer;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.PAPER;

public class ListEntryIcon extends Icon {
    public ListEntryIcon(EpicVouchers instance, String entry, BiConsumer<Player, String> onRemove, BiConsumer<Player, Pair<String, String>> onEdit) {
        super(new ItemBuilder(PAPER)
                .name(YELLOW + entry)
                .lore(GRAY + "Right click to edit", GRAY + "Left click to remove")
                .build(), clickEvent -> {
            if (clickEvent.getClickType() == ClickType.LEFT) {
                onRemove.accept(clickEvent.getPlayer(), entry);
                return;
            }
            ChatPrompt.showPrompt(instance, clickEvent.getPlayer(),
                    pEvent -> Bukkit.getScheduler().runTaskLater(instance,
                            () -> onEdit.accept(clickEvent.getPlayer(), new Pair<>(entry, pEvent.getMessage().trim())), 1L));
        });
    }
}
