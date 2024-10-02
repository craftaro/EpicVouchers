package com.craftaro.epicvouchers.libraries.inventory;

import com.craftaro.core.utils.SkullItemCreator;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.menus.ActionMenu;
import com.craftaro.epicvouchers.voucher.Voucher;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.ARROW;
import static org.bukkit.Material.BARRIER;

public abstract class PlayersMenu extends FastInv {
    private final int SLOTS = 17;
    private final EpicVouchers instance;
    private final Voucher voucher;
    private final BiConsumer<CommandSender, Player> playerConsumer;
    private final List<Player> players;
    private int page;

    public PlayersMenu(EpicVouchers instance, Voucher voucher, String menu, BiConsumer<CommandSender, Player> playerConsumer, List<Player> players) {
        super(27, menu);
        this.instance = instance;
        this.voucher = voucher;
        this.playerConsumer = playerConsumer;
        this.players = players;

        refresh();
    }

    @Override
    public void refresh() {
        fill(null);
        final int startIndex = this.page * (this.players.size() - 1);

        IntStream.rangeClosed(0, this.SLOTS).forEach(slot -> {
            int index = startIndex + slot;

            if (index >= this.players.size()) {
                return;
            }

            Player player = this.players.get(index);

            ItemStack itemStack = SkullItemCreator.byPlayer(player);

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(YELLOW + player.getName());
            itemStack.setItemMeta(itemMeta);

            addItem(slot, itemStack, event -> this.playerConsumer.accept(event.getPlayer(), player));
        });

        if (this.players.size() / this.SLOTS > this.page) {
            addItem(26, new ItemBuilder(ARROW)
                    .name(YELLOW + "Next")
                    .lore(GRAY + "Click to go to the next page of players")
                    .build(), event -> {
                this.page++;
                refresh();
            });
        } else {
            addItem(26, null);
        }

        if (this.page > 0) {
            addItem(18, new ItemBuilder(ARROW)
                    .name(YELLOW + "Previous")
                    .lore(GRAY + "Click to go to the previous page of players")
                    .build(), event -> {
                this.page--;
                refresh();
            });
        } else {
            addItem(18, new ItemBuilder(BARRIER)
                    .name(YELLOW + "Return")
                    .lore(GRAY + "Return to the action menu")
                    .addGlow().build(), event -> new ActionMenu(this.instance, this.voucher).open(event.getPlayer()));
        }

        if (this.instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
            ItemStack fillItem = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();

            IntStream.rangeClosed(this.SLOTS + 1, 26).forEach(slot -> {
                if (getInventory().getItem(slot) == null) {
                    addItem(slot, new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
                }
            });
        }
    }
}
