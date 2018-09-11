package nl.marido.deluxevouchers.vouchers;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import nl.marido.deluxevouchers.DeluxeVouchers;
import nl.marido.deluxevouchers.events.VoucherRedeemEvent;
import nl.marido.deluxevouchers.handlers.Connections;
import nl.marido.deluxevouchers.handlers.DataHandler;
import nl.marido.deluxevouchers.handlers.SoundHandler;
import nl.marido.deluxevouchers.liberaries.Bountiful;

public class VoucherExecutor {

	public static void redeemVoucher(Player player, String voucher, ItemStack item, boolean manual) {
		try {
			VoucherRedeemEvent event = new VoucherRedeemEvent(player, voucher, item, manual);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}
			String path = "vouchers." + voucher + ".";
			boolean duplication = false;
			if (!player.getItemInHand().isSimilar(item)) {
				duplication = true;
			}
			ConsoleCommandSender console = DeluxeVouchers.getConsole();
			if (!duplication) {
				if (manual) {
					Cooldowns.addCooldown(player.getUniqueId(), voucher);
					if (DataHandler.getBoolean(DataHandler.vouchers, path + "remove-item")) {
						ItemStack clone = player.getItemInHand().clone();
						if (clone.getAmount() <= 1) {
							clone.setType(Material.AIR);
						} else {
							clone.setAmount(clone.getAmount() - 1);
						}
						player.setItemInHand(clone);
						player.updateInventory();
					}
				}
				if (DataHandler.getBoolean(DataHandler.vouchers, path + "feed-player")) {
					player.setFoodLevel(20);
				}
				if (DataHandler.getBoolean(DataHandler.vouchers, path + "heal-player")) {
					player.setHealth(player.getMaxHealth());
				}
				if (DataHandler.getBoolean(DataHandler.vouchers, path + "smite-effect")) {
					player.getWorld().strikeLightningEffect(player.getLocation());
				}
				String name = player.getName();
				for (String broadcast : DataHandler.getStringList(DataHandler.vouchers, path + "broadcasts")) {
					broadcast = broadcast.replaceAll("%player%", name);
					broadcast = broadcast.replaceAll("%voucher%", voucher);
					for (Player everyone : Bukkit.getOnlinePlayers()) {
						everyone.sendMessage(broadcast);
					}
				}
				for (String message : DataHandler.getStringList(DataHandler.vouchers, path + "messages")) {
					message = message.replaceAll("%player%", name);
					message = message.replaceAll("%voucher%", voucher);
					player.sendMessage(message);
				}
				for (String command : DataHandler.getStringList(DataHandler.vouchers, path + "commands")) {
					command = command.replaceAll("%player%", name);
					command = command.replaceAll("%voucher%", voucher);
					if (command.startsWith("[player]")) {
						command = command.replace("[player]", "");
						player.performCommand(command);
					} else if (command.startsWith("[op]")) {
						command = command.replace("[op]", "");
						boolean wasop = player.isOp();
						player.setOp(true);
						player.performCommand(command);
						if (!wasop) {
							player.setOp(false);
						}
					} else if (command.startsWith("[chat]")) {
						command = command.replace("[chat]", "");
						player.chat(command);
					} else if (command.startsWith("[delay]")) {
						command = command.replace("[delay]", "");
						throw new UnsupportedOperationException("delay is not supported yet");
					} else {
						Bukkit.getServer().dispatchCommand(DeluxeVouchers.getConsole(), command);
					}
				}
				String actionbar = DataHandler.getString(DataHandler.vouchers, path + "actionbar");
				actionbar = actionbar.replaceAll("%player%", name);
				actionbar = actionbar.replaceAll("%voucher%", voucher);
				Bountiful.sendActionBar(player, actionbar);
				String title = DataHandler.getString(DataHandler.vouchers, path + "titles.title");
				title = title.replaceAll("%player%", name);
				title = title.replaceAll("%voucher%", voucher);
				String subtitle = DataHandler.getString(DataHandler.vouchers, path + "titles.subtitle");
				subtitle = subtitle.replaceAll("%player%", name);
				subtitle = subtitle.replaceAll("%voucher%", voucher);
				int fadein = DataHandler.getInt(DataHandler.vouchers, path + "titles.fade-in");
				int stay = DataHandler.getInt(DataHandler.vouchers, path + "titles.stay");
				int fadeout = DataHandler.getInt(DataHandler.vouchers, path + "titles.fade-out");
				Bountiful.sendTitle(player, fadein, stay, fadeout, title, subtitle);
				String sound = DataHandler.getString(DataHandler.vouchers, path + "sounds.sound");
				int pitch = DataHandler.getInt(DataHandler.vouchers, path + "sounds.pitch");
				SoundHandler.playSound(player, sound, pitch);
				String particle = DataHandler.getString(DataHandler.vouchers, path + "particles.particle");
				if (!particle.isEmpty()) {
					int amount = DataHandler.getInt(DataHandler.vouchers, path + "particles.amount");
					player.getWorld().playEffect(player.getLocation(), Effect.valueOf(particle), amount);
				}
				String effect = DataHandler.getString(DataHandler.vouchers, path + "effects.effect");
				if (!effect.isEmpty()) {
					int amplifier = DataHandler.getInt(DataHandler.vouchers, path + "particles.amplifier");
					int duration = DataHandler.getInt(DataHandler.vouchers, path + "particles.duration") * 20;
					player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), duration, amplifier));
				}
				console.sendMessage("§f" + player.getName() + " has successfully redeemed the voucher " + voucher + ".");
				Connections.saveRedeem(player, voucher);
			} else {
				console.sendMessage("§c" + player.getName() + " has failed to duplicate the voucher " + voucher + ".");
			}
		} catch (Exception error) {
			DeluxeVouchers.getConsole().sendMessage("§cFailed to redeem the voucher " + voucher + " for the player " + player.getName() + ".");
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

}