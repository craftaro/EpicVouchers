package com.songoda.epicvouchers.listeners;

import com.songoda.core.nms.NmsManager;
import com.songoda.core.nms.nbt.NBTItem;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        final NBTItem itemNbt = NmsManager.getNbt().of(item);

        for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
            final ItemStack voucherItem = voucher.toItemStack();

            // Check voucher NBT.
            if (itemNbt.has("epicvouchers:voucher") && itemNbt.getNBTObject("epicvouchers:voucher").asString().equals(voucher.getKey())) {
                event.setCancelled(true);
                voucher.redeemVoucher(event);
                continue;
            }

            // Legacy crap.
            // does the item they're holding match this voucher?

            if (voucherItem != null && !voucherItem.isSimilar(item)) continue;
            else if (item.getType() != voucher.getMaterial() || item.getDurability() != voucher.getData()) continue;
            else {
                // material matches - verify the name + lore
                final ItemMeta meta = item.getItemMeta();
                if (meta == null || !meta.hasDisplayName()
                        || !ChatColor.stripColor(meta.getDisplayName()).equals(ChatColor.stripColor(voucher.getName(true)))
                        || (meta.hasLore() && !meta.getLore().equals(voucher.getLore(true))))
                    continue;
            }

            event.setCancelled(true);
            voucher.redeemVoucher(event);
        }
    }
}
