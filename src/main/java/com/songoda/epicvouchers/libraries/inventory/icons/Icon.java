package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.epicvouchers.libraries.inventory.IconInv.IconClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Icon {
    private ItemStack itemStack;
    private Consumer<IconClickEvent> consumer;

    public Icon(ItemStack itemStack) {
        this(itemStack, event -> {});
    }

    public Icon(ItemStack item, Consumer<IconClickEvent> consumer) {
        this.itemStack = item;
        this.consumer = consumer;
    }

    public void run(IconClickEvent e) { consumer.accept(e); }

    public ItemStack getItemStack() { return itemStack; }
}
