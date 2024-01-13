package com.craftaro.epicvouchers.libraries.inventory.icons;

import com.craftaro.epicvouchers.libraries.inventory.IconInv;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Icon {
    private final ItemStack itemStack;
    private final Consumer<IconInv.IconClickEvent> consumer;

    public Icon(ItemStack item) {
        this(item, event -> {
        });
    }

    public Icon(ItemStack item, Consumer<IconInv.IconClickEvent> consumer) {
        this.itemStack = item;
        this.consumer = consumer;
    }

    public void run(IconInv.IconClickEvent e) {
        this.consumer.accept(e);
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
