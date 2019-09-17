package com.songoda.epicvouchers.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class VoucherRedeemEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

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
        return player;
    }

    public String getVoucher() {
        return voucher;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean getManual() {
        return manual;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}