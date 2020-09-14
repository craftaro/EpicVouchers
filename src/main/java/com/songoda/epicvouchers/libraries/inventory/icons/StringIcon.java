package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.WHITE;

public class StringIcon extends Icon {

    public StringIcon(EpicVouchers instance, String string, String current, BiConsumer<Player, String> consumer) {
        this(instance, string, current, consumer, s -> true);
    }

    public StringIcon(EpicVouchers instance, Material material, String string, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate) {
        this(instance, new ItemBuilder(material).name(ChatColor.YELLOW + string)
                .lore(GRAY + "Current: " + WHITE + current, GRAY + "Right click to edit", GRAY + "Left click to clear").build(), current, consumer, predicate, false);
    }

    public StringIcon(EpicVouchers instance, String string, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate) {
        this(instance, new ItemBuilder(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_14) ? Material.OAK_SIGN : Material.valueOf("SIGN")).name(ChatColor.YELLOW + string)
                .lore(GRAY + "Current: " + WHITE + current, GRAY + "Right click to edit", GRAY + "Left click to clear").build(), current, consumer, predicate, false);
    }

    public StringIcon(EpicVouchers instance, String string, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate, boolean noLeft) {
        this(instance, new ItemBuilder(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_14) ? Material.OAK_SIGN : Material.valueOf("SIGN")).name(ChatColor.YELLOW + string)
                .lore(GRAY + "Current: " + WHITE + current, GRAY + "Right click to edit", GRAY + "Left click to clear").build(), current, consumer, predicate, noLeft);
    }

    public StringIcon(EpicVouchers instance, ItemStack itemStack, String current, BiConsumer<Player, String> consumer) {
        this(instance, itemStack, current, consumer, s -> true, false);
    }

    public StringIcon(EpicVouchers instance, ItemStack itemStack, String current, BiConsumer<Player, String> consumer, Predicate<String> predicate, boolean noLeft) {
        super(itemStack, event -> {
            if (!noLeft && event.getClickType() == ClickType.LEFT) {
                consumer.accept(event.getPlayer(), "");
                event.getPlayer().sendMessage(TextUtils.formatText("&7Successfully cleared&7."));
            } else {
                ChatPrompt.showPrompt(instance, event.getPlayer(), TextUtils.formatText("&7Enter a new value. Current: &r" + current), aevent -> {
                    final String msg = aevent.getMessage().trim();
                    if (!predicate.test(msg)) {
                        event.getPlayer().sendMessage(TextUtils.formatText("&cFailed to set value to: " + msg));
                        return;
                    }
                    event.getPlayer().sendMessage(TextUtils.formatText("&7Successfully set to &r" + msg + "&7."));
                    Bukkit.getScheduler().runTaskLater(instance, () -> consumer.accept(event.getPlayer(), msg), 1L);
            });
            }

        });
    }

}
