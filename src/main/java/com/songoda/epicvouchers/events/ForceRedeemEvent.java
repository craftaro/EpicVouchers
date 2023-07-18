package com.songoda.epicvouchers.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ForceRedeemEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Player player;
    private final String voucher;
    private final int amount;
    private final CommandSender sender;
    private boolean cancelled;

    public ForceRedeemEvent(Player player, String voucher, int amount, CommandSender sender) {
        this.player = player;
        this.voucher = voucher;
        this.amount = amount;
        this.sender = sender;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getVoucher() {
        return this.voucher;
    }

    public int getAmount() {
        return this.amount;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
