package com.craftaro.epicvouchers.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class VoucherRedeemEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final String voucher;
    private final ItemStack item;
    private final boolean manual;
    private boolean cancelled;

    public VoucherRedeemEvent(Player player, String voucher, ItemStack item, boolean manual) {
        this.player = player;
        this.voucher = voucher;
        this.item = item;
        this.manual = manual;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getVoucher() {
        return this.voucher;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public boolean getManual() {
        return this.manual;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
