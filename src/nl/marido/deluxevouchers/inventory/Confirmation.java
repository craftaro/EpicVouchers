package nl.marido.deluxevouchers.inventory;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.marido.deluxevouchers.handlers.DataHandler;
import nl.marido.deluxevouchers.handlers.SoundHandler;
import nl.marido.deluxevouchers.vouchers.VoucherExecutor;

public class Confirmation implements Listener {

	public static HashMap<UUID, String> vouchercache = new HashMap<UUID, String>();
	public static HashMap<UUID, ItemStack> itemcache = new HashMap<UUID, ItemStack>();
	public static HashMap<UUID, Integer> slotcache = new HashMap<UUID, Integer>();

	public static void confirmVoucher(Player player, String voucher, ItemStack item) {
		Inventory confirmmenu = Bukkit.createInventory(null, DataHandler.confirmslots, DataHandler.confirmtitle);
		SoundHandler.playSound(player, DataHandler.confirmsound, DataHandler.confirmpitch);
		ItemStack confirmitem = new ItemStack(Material.valueOf(DataHandler.confirmitemmaterial), 1, (short) DataHandler.confirmitemdata);
		ItemMeta confirmitemmeta = confirmitem.getItemMeta();
		confirmitemmeta.setDisplayName(DataHandler.confirmitemname);
		confirmitemmeta.setLore(DataHandler.confirmitemlore);
		if (DataHandler.confirmitemglow) {
			confirmitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
			confirmitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		if (DataHandler.confirmitemunbreakable) {
			confirmitemmeta.spigot().setUnbreakable(true);
		}
		if (DataHandler.confirmitemhideattributes) {
			confirmitemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			confirmitemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		confirmitem.setItemMeta(confirmitemmeta);
		confirmmenu.setItem(DataHandler.confirmitemslot, confirmitem);
		ItemStack cancelitem = new ItemStack(Material.valueOf(DataHandler.cancelitemmaterial), 1, (short) DataHandler.cancelitemdata);
		ItemMeta cancelitemmeta = cancelitem.getItemMeta();
		cancelitemmeta.setDisplayName(DataHandler.cancelitemname);
		cancelitemmeta.setLore(DataHandler.cancelitemlore);
		if (DataHandler.cancelitemglow) {
			cancelitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
			cancelitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		if (DataHandler.cancelitemunbreakable) {
			cancelitemmeta.spigot().setUnbreakable(true);
		}
		if (DataHandler.cancelitemhideattributes) {
			cancelitemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			cancelitemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		cancelitem.setItemMeta(cancelitemmeta);
		confirmmenu.setItem(DataHandler.cancelitemslot, cancelitem);
		if (DataHandler.confirmfill) {
			ItemStack fillitem;
			try {
				fillitem = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
			} catch (Exception error) {
				fillitem = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
			}
			ItemMeta fillitemmeta = fillitem.getItemMeta();
			fillitemmeta.setDisplayName("§r");
			fillitem.setItemMeta(fillitemmeta);
			for (int empty = 0; empty < confirmmenu.getSize(); empty++) {
				if (confirmmenu.getItem(empty) == null) {
					confirmmenu.setItem(empty, fillitem);
				}
			}
		}
		UUID uuid = player.getUniqueId();
		vouchercache.put(uuid, voucher);
		itemcache.put(uuid, item);
		player.openInventory(confirmmenu);
	}

	@EventHandler
	public void clickListener(InventoryClickEvent event) {
		try {
			if (event.getInventory().getType() == InventoryType.CHEST) {
				if (event.getInventory().getTitle().equals(DataHandler.confirmtitle)) {
					Player player = (Player) event.getWhoClicked();
					ItemStack clicked = event.getCurrentItem();
					if (clicked != null) {
						if (clicked.getItemMeta().hasDisplayName()) {
							if (clicked.getItemMeta().getDisplayName().equals(DataHandler.confirmitemname)) {
								SoundHandler.playSound(player, DataHandler.confirmitemsound, DataHandler.confirmitempitch);
								player.closeInventory();
								UUID uuid = player.getUniqueId();
								String voucher = vouchercache.get(uuid);
								ItemStack item = itemcache.get(uuid);
								VoucherExecutor.redeemVoucher(player, voucher, item, true);
							}
						}
						if (clicked.getItemMeta().hasDisplayName()) {
							if (clicked.getItemMeta().getDisplayName().equals(DataHandler.cancelitemname)) {
								UUID uuid = player.getUniqueId();
								vouchercache.remove(uuid);
								itemcache.remove(uuid);
								SoundHandler.playSound(player, DataHandler.cancelitemsound, DataHandler.cancelitempitch);
								player.closeInventory();
							}
						}
						event.setCancelled(true);
					}
				}
			}
		} catch (Exception error) {
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

}