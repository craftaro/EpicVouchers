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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.marido.deluxevouchers.DeluxeVouchers;
import nl.marido.deluxevouchers.handlers.DataHandler;
import nl.marido.deluxevouchers.handlers.SoundHandler;
import nl.marido.deluxevouchers.vouchers.VoucherBuilder;

public class VoucherEditor implements Listener {

	public static HashMap<UUID, String> editor = new HashMap<UUID, String>();
	public static HashMap<UUID, String> type = new HashMap<UUID, String>();

	public static void openMenu(Player player) {
		Inventory editormenu = Bukkit.createInventory(null, DataHandler.editorslots, DataHandler.editortitle);
		SoundHandler.playSound(player, DataHandler.editorsound, DataHandler.editorpitch);
		for (String voucher : DataHandler.getSection(DataHandler.vouchers, "vouchers")) {
			ItemStack item = VoucherBuilder.getVoucher(voucher, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(meta.getDisplayName() + " §b§l[CLICK TO EDIT]");
			item.setItemMeta(meta);
			editormenu.setItem(editormenu.firstEmpty(), item);
		}
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
			for (int empty = 0; empty < editormenu.getSize(); empty++) {
				if (editormenu.getItem(empty) == null) {
					editormenu.setItem(empty, fillitem);
				}
			}
		}
		player.closeInventory();
		player.openInventory(editormenu);
	}

	public static void editVoucher(Player player, String voucher, ItemStack item) {
		String title = DataHandler.editorvouchertitle;
		title = title.replaceAll("%voucher%", voucher);
		Inventory editormenu = Bukkit.createInventory(null, DataHandler.editorvoucherslots, title);
		ItemMeta meta = item.getItemMeta();
		String name = meta.getDisplayName();
		name = DataHandler.getString(DataHandler.vouchers, "vouchers." + voucher + ".name");
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		editormenu.setItem(DataHandler.editorvoucherslot, item);
		ItemStack backitem = new ItemStack(Material.valueOf(DataHandler.editorbackitemmaterial), 1, (short) DataHandler.editorbackitemdata);
		ItemMeta backitemmeta = backitem.getItemMeta();
		backitemmeta.setDisplayName(DataHandler.editorbackitemname);
		backitemmeta.setLore(DataHandler.editorbackitemlore);
		if (DataHandler.editorbackitemglow) {
			backitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
			backitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		if (DataHandler.editorbackitemunbreakable) {
			backitemmeta.spigot().setUnbreakable(true);
		}
		if (DataHandler.editorbackitemhideattributes) {
			backitemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			backitemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		backitem.setItemMeta(backitemmeta);
		editormenu.setItem(DataHandler.editorbackitemslot, backitem);
		ItemStack cloneitem = new ItemStack(Material.valueOf(DataHandler.editorcloneitemmaterial), 1, (short) DataHandler.editorcloneitemdata);
		ItemMeta cloneitemmeta = cloneitem.getItemMeta();
		cloneitemmeta.setDisplayName(DataHandler.editorcloneitemname);
		cloneitemmeta.setLore(DataHandler.editorcloneitemlore);
		if (DataHandler.editorcloneitemglow) {
			cloneitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
			cloneitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		if (DataHandler.editorcloneitemunbreakable) {
			cloneitemmeta.spigot().setUnbreakable(true);
		}
		if (DataHandler.editorcloneitemhideattributes) {
			cloneitemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			cloneitemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		cloneitem.setItemMeta(cloneitemmeta);
		editormenu.setItem(DataHandler.editorcloneitemslot, cloneitem);
		ItemStack renameitem = new ItemStack(Material.valueOf(DataHandler.editorrenameitemmaterial), 1, (short) DataHandler.editorrenameitemdata);
		ItemMeta renameitemmeta = renameitem.getItemMeta();
		renameitemmeta.setDisplayName(DataHandler.editorrenameitemname);
		renameitemmeta.setLore(DataHandler.editorrenameitemlore);

		if (DataHandler.editorrenameitemglow) {
			renameitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
			renameitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		if (DataHandler.editorrenameitemunbreakable) {
			renameitemmeta.spigot().setUnbreakable(true);
		}
		if (DataHandler.editorrenameitemhideattributes) {
			renameitemmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			renameitemmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		}
		renameitem.setItemMeta(renameitemmeta);
		editormenu.setItem(DataHandler.editorrenameitemslot, renameitem);
		if (DataHandler.editorvoucherfill) {
			ItemStack fillitem;
			try {
				fillitem = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
			} catch (Exception error) {
				fillitem = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
			}
			ItemMeta fillitemmeta = fillitem.getItemMeta();
			fillitemmeta.setDisplayName("§r");
			fillitem.setItemMeta(fillitemmeta);
			for (int empty = 0; empty < editormenu.getSize(); empty++) {
				if (editormenu.getItem(empty) == null) {
					editormenu.setItem(empty, fillitem);
				}
			}
		}
		player.closeInventory();
		editor.put(player.getUniqueId(), voucher);
		player.openInventory(editormenu);
	}

	@EventHandler
	public void clickListener(InventoryClickEvent event) {
		try {
			if (event.getInventory().getType() == InventoryType.CHEST) {
				if (event.getInventory().getTitle().equals(DataHandler.editortitle)) {
					for (String voucher : DataHandler.getSection(DataHandler.vouchers, "vouchers")) {
						String path = "vouchers." + voucher + ".";
						ItemStack item = event.getCurrentItem();
						if (item.getType() == Material.valueOf(DataHandler.getString(DataHandler.vouchers, path + "material"))) {
							if (item.getDurability() == (short) DataHandler.getInt(DataHandler.vouchers, path + "data")) {
								ItemMeta meta = item.getItemMeta();
								if (meta.hasLore()) {
									if (meta.getLore().equals(DataHandler.getStringList(DataHandler.vouchers, path + "lore"))) {
										Player player = (Player) event.getWhoClicked();
										SoundHandler.playSound(player, DataHandler.editorvouchersound, DataHandler.editorvoucherpitch);
										event.getWhoClicked().closeInventory();
										editVoucher(player, voucher, item);
									}
								}
							}
						}
					}
					event.setCancelled(true);
				}
			}
		} catch (Exception error) {
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

	@EventHandler
	public void editListener(InventoryClickEvent event) {
		try {
			if (event.getInventory().getType() == InventoryType.CHEST) {
				if (editor.containsKey(event.getWhoClicked().getUniqueId())) {
					event.setCancelled(true);
				}
			}
		} catch (Exception error) {
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

	@EventHandler
	public void renameListener(InventoryClickEvent event) {
		try {
			if (event.getInventory().getType() == InventoryType.CHEST) {
				if (editor.containsKey(event.getWhoClicked().getUniqueId())) {
					ItemStack item = event.getCurrentItem();
					if (item.getType() == Material.valueOf(DataHandler.editorrenameitemmaterial)) {
						if (item.getDurability() == DataHandler.editorrenameitemdata) {
							ItemMeta meta = item.getItemMeta();
							if (meta.hasDisplayName()) {
								if (meta.getDisplayName().equals(DataHandler.editorrenameitemname)) {
									if (meta.hasLore()) {
										if (meta.getLore().equals(DataHandler.editorrenameitemlore)) {
											SoundHandler.playSound((Player) event.getWhoClicked(), DataHandler.editorrenameitemsound, DataHandler.editorrenameitempitch);
											String cache = editor.get(event.getWhoClicked().getUniqueId());
											event.getWhoClicked().closeInventory();
											editor.put(event.getWhoClicked().getUniqueId(), cache);
											type.put(event.getWhoClicked().getUniqueId(), "rename");
											event.getWhoClicked().sendMessage(DataHandler.editorrenamestart);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception error) {
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

	@EventHandler
	public void cloneListener(InventoryClickEvent event) {
		try {
			if (event.getInventory().getType() == InventoryType.CHEST) {
				if (editor.containsKey(event.getWhoClicked().getUniqueId())) {
					ItemStack item = event.getCurrentItem();
					if (item.getType() == Material.valueOf(DataHandler.editorcloneitemmaterial)) {
						if (item.getDurability() == DataHandler.editorcloneitemdata) {
							ItemMeta meta = item.getItemMeta();
							if (meta.hasDisplayName()) {
								if (meta.getDisplayName().equals(DataHandler.editorcloneitemname)) {
									if (meta.hasLore()) {
										if (meta.getLore().equals(DataHandler.editorcloneitemlore)) {
											SoundHandler.playSound((Player) event.getWhoClicked(), DataHandler.editorcloneitemsound, DataHandler.editorcloneitempitch);
											event.getWhoClicked().getInventory().addItem(VoucherBuilder.getVoucher(editor.get(event.getWhoClicked().getUniqueId()), 1));
											((Player) event.getWhoClicked()).updateInventory();
											String message = DataHandler.editorreceiveitem;
											message = message.replaceAll("%voucher%", editor.get(event.getWhoClicked().getUniqueId()));
											event.getWhoClicked().sendMessage(message);
											event.getWhoClicked().closeInventory();
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception error) {
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

	@EventHandler
	public void chatListener(AsyncPlayerChatEvent event) {
		if (editor.containsKey(event.getPlayer().getUniqueId())) {
			if (type.containsKey(event.getPlayer().getUniqueId())) {
				if (type.get(event.getPlayer().getUniqueId()).equals("rename")) {
					type.remove(event.getPlayer().getUniqueId());
					String renamed = "&r" + event.getMessage();
					String path = "vouchers." + editor.get(event.getPlayer().getUniqueId()) + ".name";
					DataHandler.vouchers.set(path, renamed);
					try {
						DataHandler.config.save(DataHandler.configfile);
						DataHandler.vouchers.save(DataHandler.vouchersfile);
						DataHandler.mysql.save(DataHandler.mysqlfile);
					} catch (Exception error) {
						error.printStackTrace();
					}
					DataHandler.cacheData();
					String message = DataHandler.editorrenamedone;
					message = DeluxeVouchers.applyColor(message);
					event.getPlayer().sendMessage(message);
					editVoucher(event.getPlayer(), editor.get(event.getPlayer().getUniqueId()), VoucherBuilder.getVoucher(editor.get(event.getPlayer().getUniqueId()), 1));
					SoundHandler.playSound(event.getPlayer(), DataHandler.editorvouchersound, DataHandler.editorvoucherpitch);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void returnListener(InventoryClickEvent event) {
		try {
			if (event.getInventory().getType() == InventoryType.CHEST) {
				if (editor.containsKey(event.getWhoClicked().getUniqueId())) {
					ItemStack item = event.getCurrentItem();
					if (item.getType() == Material.valueOf(DataHandler.editorbackitemmaterial)) {
						if (item.getDurability() == DataHandler.editorbackitemdata) {
							ItemMeta meta = item.getItemMeta();
							if (meta.hasDisplayName()) {
								if (meta.getDisplayName().equals(DataHandler.editorbackitemname)) {
									if (meta.hasLore()) {
										if (meta.getLore().equals(DataHandler.editorbackitemlore)) {
											SoundHandler.playSound((Player) event.getWhoClicked(), DataHandler.editorbackitemsound, DataHandler.editorbackitempitch);
											event.getWhoClicked().closeInventory();
											openMenu((Player) event.getWhoClicked());
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception error) {
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
	}

	@EventHandler
	public void closeListener(InventoryCloseEvent event) {
		if (editor.containsKey(event.getPlayer().getUniqueId())) {
			editor.remove(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void leaveListener(PlayerQuitEvent event) {
		if (editor.containsKey(event.getPlayer().getUniqueId())) {
			editor.remove(event.getPlayer().getUniqueId());
		}
	}

}