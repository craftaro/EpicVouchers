package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.IconInv.IconClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.function.Consumer;

public class ToggleableIcon extends Icon {
    private Consumer<IconClickEvent> consumer;

    public ToggleableIcon(String displayname, Consumer<IconClickEvent> consumer, boolean state) {
        super(new ItemBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + displayname)
                .lore(state ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED")
                .build(), event -> {});
        this.consumer = consumer;
    }

    @Override
    public void run(IconClickEvent event) {
        consumer.accept(event);
    }
}
