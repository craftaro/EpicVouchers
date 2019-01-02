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
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
            Player player = event.getPlayer();
            if (!player.hasPermission(voucher.getPermission())) {
                continue;
            }

            ItemStack item = event.getPlayer().getItemInHand();

            if (item.getType() != voucher.getMaterial() || item.getDurability() != voucher.getData()) {
                continue;
            }

            ItemMeta meta = item.getItemMeta();

            if (!meta.hasDisplayName() || !meta.getDisplayName().equals(voucher.getName(true)) || !meta.getLore().equals(voucher.getLore(true))) {
                continue;
            }

            UUID uuid = player.getUniqueId();

            if (instance.getCooldowns().isOnCoolDown(uuid)) {
                player.sendMessage(instance.getLocale().getMessage("event.general.cooldown", instance.getCooldowns().getTime(uuid), voucher.getName(true)));
                return;
            }

            event.setCancelled(true);

            if (voucher.isConfirm()) {
                new ConfirmMenu(instance, voucher).open(player);
                return;
            }

            instance.getVoucherExecutor().redeemVoucher(player, voucher, item, true);
        }
    }
}