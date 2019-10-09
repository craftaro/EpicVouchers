package com.songoda.epicvouchers.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class VoucherReceiveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final String voucher;
    private final ItemStack item;
    private final int amount;
    private final CommandSender sender;
    private boolean cancelled;

    public VoucherReceiveEvent(Player player, String voucher, ItemStack item, int amount, CommandSender sender) {
        this.player = player;
        this.voucher = voucher;
        this.item = item;
        this.amount = amount;
        this.sender = sender;
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

    public int getAmount() {
        return amount;
    }

    public CommandSender getSender() {
        return sender;
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