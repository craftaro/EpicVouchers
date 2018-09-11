package nl.marido.deluxevouchers.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ForceRedeemEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private String voucher;
	private int amount;
	private CommandSender sender;
	private boolean cancelled;

	public ForceRedeemEvent(Player player, String voucher, int amount, CommandSender sender) {
		this.player = player;
		this.voucher = voucher;
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

}