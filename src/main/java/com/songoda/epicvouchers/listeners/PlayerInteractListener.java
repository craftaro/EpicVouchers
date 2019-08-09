package com.songoda.epicvouchers.listeners;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.menus.ConfirmMenu;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private final EpicVouchers instance;

    public PlayerInteractListener(EpicVouchers instance) {
        this.instance = instance;
    }

    @EventHandler
    public void voucherListener(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item == null || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK))
            return;
        final Player player = event.getPlayer();

        for (Voucher voucher : instance.getVouchers().values()) {
            final ItemStack voucherItem = voucher.getItemStack();

            // does the item they're holding match this voucher?
            if (voucherItem != null && !voucher.getItemStack().isSimilar(item))
                continue;
            else if (item.getType() != voucher.getMaterial() || item.getDurability() != voucher.getData())
                continue;
            else {
                // material matches - verify the name + lore
                final ItemMeta meta = item.getItemMeta();
                if (meta == null || !meta.hasDisplayName() || !meta.hasLore() || !meta.getDisplayName().equals(voucher.getName(true)) || !meta.getLore().equals(voucher.getLore(true)))
                    continue;
            }

            event.setCancelled(true);

            // does the player have permission to redeem this voucher?
            if (!voucher.getPermission().isEmpty() && !player.hasPermission(voucher.getPermission())) {
                // todo: probably should send a message to the player...
                return;
            }

            UUID uuid = player.getUniqueId();

            if (instance.getCoolDowns().isOnCoolDown(uuid)) {
                instance.getLocale().getMessage("event.general.cooldown")
                        .processPlaceholder("time", instance.getCoolDowns().getTime(uuid))
                        .processPlaceholder("voucher", voucher.getName(true))
                        .sendPrefixedMessage(player);
                return;
            }

            if (voucher.isConfirm()) {
                new ConfirmMenu(instance,
                        () -> instance.getVoucherExecutor().redeemVoucher(player, voucher, item, true, event),
                        () -> {
                        })
                        .open(player);
            } else {
                instance.getVoucherExecutor().redeemVoucher(player, voucher, item, true, event);
            }
        }
    }
}