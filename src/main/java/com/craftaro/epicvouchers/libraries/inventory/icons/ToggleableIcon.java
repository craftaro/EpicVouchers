package com.craftaro.epicvouchers.libraries.inventory.icons;

import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.libraries.inventory.IconInv;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.function.Consumer;

public class ToggleableIcon extends Icon {
    private final Consumer<IconInv.IconClickEvent> consumer;

    public ToggleableIcon(String displayname, Consumer<IconInv.IconClickEvent> consumer, boolean state) {
        super(new ItemBuilder(Material.PAPER)
                .name(ChatColor.YELLOW + displayname)
                .lore(state ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED")
                .build(), event -> {
        });
        this.consumer = consumer;
    }

    @Override
    public void run(IconInv.IconClickEvent e) {
        this.consumer.accept(e);
    }
}
