package com.songoda.epicvouchers.listeners;

import com.songoda.core.third_party.de.tr7zw.nbtapi.NBTItem;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.settings.Settings;
import com.songoda.epicvouchers.utils.CachedSet;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;

public class PlayerInteractListener implements Listener {
    private final EpicVouchers instance;

    private final CachedSet<ItemStack> itemsThatGotLegacyChecked = new CachedSet<>(3 * 60);

    public PlayerInteractListener(EpicVouchers instance) {
        this.instance = instance;
    }

    @EventHandler
    public void voucherListener(PlayerInteractEvent e) {
        boolean legacyCheckEnabled = Settings.CHECK_FOR_LEGACY_ITEMS.getBoolean();

        ItemStack item = e.getItem();
        if (item == null || !isRightClickAction(e.getAction())) {
            return;
        }

        NBTItem nbtItem = new NBTItem(item);

        boolean itemHasVoucher = nbtItem.hasTag("epicvouchers:voucher");
        boolean itemHasBeenLegacyChecked = !legacyCheckEnabled || this.itemsThatGotLegacyChecked.contains(item);
        if (!itemHasVoucher && itemHasBeenLegacyChecked) {
            return;
        }

        Voucher voucher;
        Collection<Voucher> allVouchers = this.instance.getVoucherManager().getVouchers();

        if (itemHasVoucher) {
            String voucherKey = nbtItem.getString("epicvouchers:voucher");
            voucher = findVoucherForKey(voucherKey, allVouchers);
            if (voucher != null) {
                e.setCancelled(true);
                voucher.redeemVoucher(e);
            }

            return;
        }

        if (!Settings.CHECK_FOR_LEGACY_ITEMS.getBoolean()) {
            return;
        }

        voucher = findVoucherForLegacyItem(item, allVouchers);
        if (voucher == null) {
            this.itemsThatGotLegacyChecked.add(item);
            return;
        }

        e.setCancelled(true);
        voucher.redeemVoucher(e);
    }

    private boolean isRightClickAction(Action action) {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    private Voucher findVoucherForKey(String voucherKey, Collection<Voucher> allVouchers) {
        for (Voucher voucher : allVouchers) {
            if (voucherKey.equals(voucher.getKey())) {
                return voucher;
            }
        }

        return null;
    }

    /**
     * @deprecated This is a legacy method that is only used for backwards compatibility
     * with vouchers that were created before the voucher key was stored in NBT.
     * Some checks in here don't even look like they make sense or look redundant... Hard to touch this.
     */
    @Deprecated
    private Voucher findVoucherForLegacyItem(ItemStack item, Collection<Voucher> allVouchers) {
        for (Voucher voucher : allVouchers) {
            ItemStack voucherItem = voucher.toItemStack();

            if (voucherItem != null && !voucherItem.isSimilar(item)) {
                continue;
            }
            if (item.getType() != voucher.getMaterial() || item.getDurability() != voucher.getData()) {
                continue;
            }

            // material matches - verify the name + lore
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName() && ChatColor.stripColor(meta.getDisplayName()).equals(ChatColor.stripColor(voucher.getName(true))) && (!meta.hasLore() || meta.getLore().equals(voucher.getLore(true)))) {
                return voucher;
            }
        }

        return null;
    }
}
