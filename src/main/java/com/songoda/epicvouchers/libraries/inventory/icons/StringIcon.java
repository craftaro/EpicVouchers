package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.AbstractAnvilGUI;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static com.songoda.epicvouchers.libraries.AbstractAnvilGUI.AnvilSlot.INPUT_LEFT;
import static com.songoda.epicvouchers.utils.Methods.format;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.WHITE;
import static org.bukkit.Material.SIGN;

public class StringIcon extends Icon {

    public StringIcon(EpicVouchers instance, String string, String current, BiConsumer<Player, String> consumer) {
        this(instance, string, current, consumer, s -> true);
    }

    public StringIcon(EpicVouchers instance, Material material, String string, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate) {
        this(instance, new ItemBuilder(material).name(ChatColor.YELLOW + string)
                .lore(GRAY + "Current: " + WHITE + current, GRAY + "Right click to edit", GRAY + "Left click to clear").build(), current, consumer, predicate, false);
    }

    public StringIcon(EpicVouchers instance, String string, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate) {
        this(instance, new ItemBuilder(SIGN).name(ChatColor.YELLOW + string)
                .lore(GRAY + "Current: " + WHITE + current, GRAY + "Right click to edit", GRAY + "Left click to clear").build(), current, consumer, predicate, false);
    }

    public StringIcon(EpicVouchers instance, String string, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate, boolean noLeft) {
        this(instance, new ItemBuilder(SIGN).name(ChatColor.YELLOW + string)
                .lore(GRAY + "Current: " + WHITE + current, GRAY + "Right click to edit", GRAY + "Left click to clear").build(), current, consumer, predicate, noLeft);
    }

    public StringIcon(EpicVouchers instance, ItemStack itemStack, String current, BiConsumer<Player, String> consumer) {
        this(instance, itemStack, current, consumer, s -> true, false);
    }

    public StringIcon(EpicVouchers instance, ItemStack itemStack, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate, boolean noLeft) {
        super(itemStack, event -> {
            if (!noLeft && event.getClickType() == ClickType.LEFT) {
                consumer.accept(event.getPlayer(), "");
                event.getPlayer().sendMessage(format("&7Successfully cleared&7."));
            } else {
                AbstractAnvilGUI anvilGUI = new AbstractAnvilGUI(instance, event.getPlayer(), anvilEvent -> {
                    if (!predicate.test(anvilEvent.getName())) {
                        event.getPlayer().sendMessage(format("&cFailed to set value to: " + anvilEvent.getName()));
                        return;
                    }
                    consumer.accept(event.getPlayer(), anvilEvent.getName());
                    event.getPlayer().sendMessage(format("&7Successfully set to &r{changed}&7.", "{changed}", anvilEvent.getName()));
                });
                anvilGUI.setSlot(INPUT_LEFT, new ItemBuilder(SIGN).name(current).build());
                anvilGUI.open();
            }

        });
    }

}
