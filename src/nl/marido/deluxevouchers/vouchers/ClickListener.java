package nl.marido.deluxevouchers.vouchers;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.marido.deluxevouchers.handlers.DataHandler;
import nl.marido.deluxevouchers.inventory.Confirmation;

public class ClickListener implements Listener {

	@EventHandler
	public void voucherListener(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			for (String voucher : DataHandler.getSection(DataHandler.vouchers, "vouchers")) {
				String path = "vouchers." + voucher + ".";
				Player player = event.getPlayer();
				if (player.hasPermission(DataHandler.getString(DataHandler.vouchers, path + "permission"))) {
					ItemStack item = event.getPlayer().getItemInHand();
					if (item.getType() == Material.valueOf(DataHandler.getString(DataHandler.vouchers, path + "material"))) {
						if (item.getDurability() == (short) DataHandler.getInt(DataHandler.vouchers, path + "data")) {
							ItemMeta meta = item.getItemMeta();
							if (meta.hasDisplayName()) {
								if (meta.getDisplayName().equals(DataHandler.getString(DataHandler.vouchers, path + "name"))) {
									if (meta.hasLore()) {
										if (meta.getLore().equals(DataHandler.getStringList(DataHandler.vouchers, path + "lore"))) {
											UUID uuid = player.getUniqueId();
											if (!Cooldowns.entries.containsKey(uuid)) {
												if (DataHandler.getBoolean(DataHandler.vouchers, path + "confirm")) {
													Confirmation.confirmVoucher(player, voucher, item);
												} else {
													VoucherExecutor.redeemVoucher(player, voucher, item, true);
												}
												event.setCancelled(true);
											} else {
												String message = DataHandler.cooldownmessage;
												message = message.replaceAll("%time%", String.valueOf(Cooldowns.entries.get(uuid) + 1));
												message = message.replaceAll("%voucher%", voucher);
												player.sendMessage(message);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

}