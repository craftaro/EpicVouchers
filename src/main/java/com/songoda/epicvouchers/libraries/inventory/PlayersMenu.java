package com.songoda.epicvouchers.libraries.inventory;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.menus.ActionMenu;
import com.songoda.epicvouchers.utils.NMSUtil;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.*;

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
        final int startIndex = page * (players.size() - 1);

        IntStream.rangeClosed(0, SLOTS).forEach(slot -> {
            int index = startIndex + slot;

            if (index >= players.size()) {
                return;
            }

            Player player = players.get(index);

            ItemStack itemStack = new ItemStack(NMSUtil.getVersionNumber() > 12 ? PLAYER_HEAD : Material.valueOf("SKULL_ITEM"));

            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            try {
                skullMeta.setOwningPlayer(player);
            } catch (Throwable ignore) {
            }

            skullMeta.setDisplayName(YELLOW + player.getName());
            itemStack.setItemMeta(skullMeta);

            addItem(slot, itemStack, event -> playerConsumer.accept(event.getPlayer(), player));
        });

        if (players.size() / SLOTS > page) {
            addItem(26, new ItemBuilder(ARROW)
                    .name(YELLOW + "Next")
                    .lore(GRAY + "Click to go to the next page of players")
                    .build(), event -> {
                page++;
                refresh();
            });
        } else {
            addItem(26, null);
        }

        if (page > 0) {
            addItem(18, new ItemBuilder(ARROW)
                    .name(YELLOW + "Previous")
                    .lore(GRAY + "Click to go to the previous page of players")
                    .build(), event -> {
                page--;
                refresh();
            });
        } else {
            addItem(18, new ItemBuilder(BARRIER)
                    .name(YELLOW + "Return")
                    .lore(GRAY + "Return to the action menu")
                    .addGlow().build(), event -> new ActionMenu(instance, voucher).open(event.getPlayer()));
        }

        if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
            ItemStack fillItem = instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ? new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                    new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);

            IntStream.rangeClosed(SLOTS + 1, 26).forEach(slot -> {
                if (getInventory().getItem(slot) == null)
                    addItem(slot, new ItemBuilder(fillItem).name(ChatColor.RESET.toString()).build());
            });
        }
    }
}
