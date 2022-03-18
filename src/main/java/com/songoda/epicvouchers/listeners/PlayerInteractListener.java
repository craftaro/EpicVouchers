package com.songoda.epicvouchers.listeners;

import com.songoda.core.third_party.de.tr7zw.nbtapi.NBTItem;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.utils.CachedSet;
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

    private final CachedSet<ItemStack> checkedLegacyVouchers = new CachedSet<>(3 * 60);

    public PlayerInteractListener(EpicVouchers instance) {
        this.instance = instance;
    }

    @EventHandler
    public void voucherListener(PlayerInteractEvent e) {
        ItemStack item = e.getItem();

        if (item != null && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            NBTItem itemNbt = new NBTItem(item);

            boolean itemHasVoucher = itemNbt.hasKey("epicvouchers:voucher");
            String itemVoucherValue = itemNbt.getString("epicvouchers:voucher");

            boolean legacyChecked = checkedLegacyVouchers.contains(item);

            if (itemHasVoucher || !legacyChecked) {
                boolean shouldBeLegacyCached = !itemHasVoucher;

                for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
                    // Check voucher NBT.
                    if (itemHasVoucher && itemVoucherValue.equals(voucher.getKey())) {
                        e.setCancelled(true);
                        voucher.redeemVoucher(e);
                        break;
                    }

                    // TODO: eventually make the legacy check configurable as a lot of players (and vouchers) quickly cause lag
                    // Legacy crap.
                    // does the item they're holding match this voucher?
                    ItemStack voucherItem = voucher.toItemStack();

                    if ((voucherItem == null || voucherItem.isSimilar(item)) &&
                            item.getType() == voucher.getMaterial() &&
                            item.getDurability() == voucher.getData()) {
                        // material matches - verify the name + lore
                        ItemMeta meta = item.getItemMeta();

                        if (meta != null && meta.hasDisplayName()
                                && ChatColor.stripColor(meta.getDisplayName()).equals(ChatColor.stripColor(voucher.getName(true)))
                                && (!meta.hasLore() || meta.getLore().equals(voucher.getLore(true)))) {
                            e.setCancelled(true);
                            voucher.redeemVoucher(e);

                            shouldBeLegacyCached = false;
                            break;
                        }
                    }
                }

                if (shouldBeLegacyCached) {
                    this.checkedLegacyVouchers.add(item);
                }
            }
        }
    }
}
