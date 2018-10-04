package com.songoda.epicvouchers.inventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.utils.Debugger;
import com.songoda.epicvouchers.utils.Methods;
import com.songoda.epicvouchers.voucher.Voucher;
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

import com.songoda.epicvouchers.utils.SoundUtils;

public class VoucherEditor implements Listener {

	private HashMap<UUID, Voucher> editor = new HashMap<>();
	private HashMap<UUID, String> type = new HashMap<>();

	public EpicVouchers instance;

	public VoucherEditor(EpicVouchers instance) {
	    this.instance = instance;
    }

	public void openMenu(Player player) {
		Inventory editormenu = Bukkit.createInventory(null, 27, instance.getLocale().getMessage("interface.editor.title"));
		SoundUtils.playSound(player, "NOTE_BASS", 1);
		for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
			ItemStack item = voucher.toItemStack();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(meta.getDisplayName() + Methods.formatText(" &b&l[CLICK TO EDIT]"));
			item.setItemMeta(meta);
			editormenu.setItem(editormenu.firstEmpty(), item);
		}
		if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
			ItemStack fillitem;
			try {
				fillitem = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
			} catch (Exception error) {
				fillitem = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
			}
			ItemMeta fillitemmeta = fillitem.getItemMeta();
			fillitemmeta.setDisplayName(Methods.formatText("&r"));
			fillitem.setItemMeta(fillitemmeta);
			for (int empty = 0; empty < editormenu.getSize(); empty++) {
				if (editormenu.getItem(empty) == null) {
					editormenu.setItem(empty, fillitem);
				}
			}
		}

		player.openInventory(editormenu);
	}

	private void editVoucher(Player player, Voucher voucher, ItemStack item) {
		String title = instance.getLocale().getMessage("interface.editvoucher.title");
		title = title.replaceAll("%voucher%", voucher.getKey());
		Inventory editormenu = Bukkit.createInventory(null, 27, title);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Methods.formatText(voucher.getName()));
		item.setItemMeta(meta);
		editormenu.setItem(13, item);

		ItemStack backitem = new ItemStack(Material.BARRIER, 1, (short) 0);
		ItemMeta backitemmeta = backitem.getItemMeta();
		backitemmeta.setDisplayName(instance.getLocale().getMessage("interface.editvoucher.backtitle"));
		backitemmeta.setLore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.backlore")));
		backitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
		backitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		backitem.setItemMeta(backitemmeta);
		editormenu.setItem(18, backitem);

		ItemStack cloneitem = new ItemStack(Material.FEATHER, 1, (short) 0);
		ItemMeta cloneitemmeta = cloneitem.getItemMeta();
		cloneitemmeta.setDisplayName(instance.getLocale().getMessage("interface.editvoucher.recivetitle"));
		cloneitemmeta.setLore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.recivelore")));
		cloneitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
		cloneitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		cloneitem.setItemMeta(cloneitemmeta);
		editormenu.setItem(0, cloneitem);
		ItemStack renameitem = new ItemStack(Material.NAME_TAG, 1, (short) 0);
		ItemMeta renameitemmeta = renameitem.getItemMeta();
		renameitemmeta.setDisplayName(instance.getLocale().getMessage("interface.editvoucher.renametitle"));
		renameitemmeta.setLore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.renamelore")));
		renameitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
		renameitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		renameitem.setItemMeta(renameitemmeta);
		editormenu.setItem(8, renameitem);
		if (instance.getConfig().getBoolean("Interface.Fill Interfaces With Glass")) {
			ItemStack fillitem;
			try {
				fillitem = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
			} catch (Exception error) {
				fillitem = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
			}
			ItemMeta fillitemmeta = fillitem.getItemMeta();
			fillitemmeta.setDisplayName(Methods.formatText("&r"));
			fillitem.setItemMeta(fillitemmeta);
			for (int empty = 0; empty < editormenu.getSize(); empty++) {
				if (editormenu.getItem(empty) == null) {
					editormenu.setItem(empty, fillitem);
				}
			}
		}
		player.openInventory(editormenu);
        editor.put(player.getUniqueId(), voucher);
	}

	@EventHandler
	public void click2Listener(InventoryClickEvent event) {
		try {
			if (event.getInventory().getType() != InventoryType.CHEST || !event.getInventory().getTitle().equals(instance.getLocale().getMessage("interface.editor.title"))) {
                return;
            }
            event.setCancelled(true);
			for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
				ItemStack item = event.getCurrentItem();

				//ToDO: this is awful
                if (item == null) return;
				if (item.getType() != voucher.getMaterial() ||
                    item.getDurability() != voucher.getData()) {
                    continue;
                }
				ItemMeta meta = item.getItemMeta();
				if (!meta.hasLore() || !meta.getLore().equals(voucher.getLore())) {
                    continue;
                }
				Player player = (Player) event.getWhoClicked();
				SoundUtils.playSound(player, "NOTE_PIANO", 1);
				editVoucher(player, voucher, item);
			}
		} catch (Exception error) {
            Debugger.runReport(error);
		}
	}

	@EventHandler
	public void clickListener(InventoryClickEvent event) {
		try {
			Player player = (Player) event.getWhoClicked();
			if (event.getInventory().getType() != InventoryType.CHEST || !editor.containsKey(player.getUniqueId())) {
				return;
			}
			event.setCancelled(true);

			ItemStack item = event.getCurrentItem();
			if (item == null || !item.hasItemMeta()) return;
			ItemMeta meta = item.getItemMeta();

			if (!meta.hasDisplayName() || !meta.hasLore()) {
				return;
			}

			if (meta.getDisplayName().equals(instance.getLocale().getMessage("interface.editvoucher.recivetitle"))) {
				SoundUtils.playSound(player, "LEVEL_UP", 1);
				player.getInventory().addItem(editor.get(player.getUniqueId()).toItemStack());
				player.updateInventory();
				String message = instance.getLocale().getMessage("interface.editvoucher.recivemessage");
				message = message.replaceAll("%voucher%", editor.get(player.getUniqueId()).getName());
				player.sendMessage(message);
			} else if (meta.getDisplayName().equals(instance.getLocale().getMessage("interface.editvoucher.backtitle"))) {
				openMenu(player);
			} else if (meta.getDisplayName().equals(instance.getLocale().getMessage("interface.editvoucher.renametitle"))) {
				SoundUtils.playSound((Player) event.getWhoClicked(), "NOTE_BASS", 1);
				Voucher cache = editor.get(event.getWhoClicked().getUniqueId());
				event.getWhoClicked().closeInventory();
				editor.put(event.getWhoClicked().getUniqueId(), cache);
				type.put(event.getWhoClicked().getUniqueId(), "rename");
				event.getWhoClicked().sendMessage(instance.getLocale().getMessage("interface.editvoucher.renamemessage"));
			}
		} catch (Exception error) {
		    Debugger.runReport(error);
		}
	}

	@EventHandler
	public void chatListener(AsyncPlayerChatEvent event) {
		if (!editor.containsKey(event.getPlayer().getUniqueId())
				|| !type.containsKey(event.getPlayer().getUniqueId())
				|| !type.get(event.getPlayer().getUniqueId()).equals("rename")) {
			return;
		}
		type.remove(event.getPlayer().getUniqueId());
		Voucher voucher = editor.get(event.getPlayer().getUniqueId());
		voucher.setName(event.getMessage());
		String message = instance.getLocale().getMessage("interface.editvoucher.renamefinish", event.getMessage());
		message = Methods.formatText(message);
		event.getPlayer().sendMessage(message);
		editVoucher(event.getPlayer(), editor.get(event.getPlayer().getUniqueId()), voucher.toItemStack());
		SoundUtils.playSound(event.getPlayer(), "NOTE_PIANO", 1);
		event.setCancelled(true);
	}

	@EventHandler
	public void closeListener(InventoryCloseEvent event) {
        editor.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void leaveListener(PlayerQuitEvent event) {
        editor.remove(event.getPlayer().getUniqueId());
	}

}