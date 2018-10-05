package com.songoda.epicvouchers.inventory;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.utils.Debugger;
import com.songoda.epicvouchers.utils.Methods;
import com.songoda.epicvouchers.utils.SoundUtils;
import com.songoda.epicvouchers.voucher.Voucher;
import com.songoda.epicvouchers.voucher.VoucherExecutor;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Confirmation implements Listener {

    private EpicVouchers instance;

	private static HashMap<UUID, Voucher> vouchercache = new HashMap<>();
	private static HashMap<UUID, ItemStack> itemcache = new HashMap<>();

	public Confirmation(EpicVouchers instance) {
	    this.instance = instance;
    }

	public void confirmVoucher(Player player, Voucher voucher, ItemStack item) {
		Inventory confirmmenu = Bukkit.createInventory(null, 27, instance.getLocale().getMessage("interface.confirmsettings.title"));
		SoundUtils.playSound(player, "NOTE_PIANO", 1);
		ItemStack confirmitem = new ItemStack(Material.EMERALD, 1, (short) 0);
		ItemMeta confirmitemmeta = confirmitem.getItemMeta();
		confirmitemmeta.setDisplayName(instance.getLocale().getMessage("interface.confirmsettings.confirmitemname"));
		confirmitemmeta.setLore(Arrays.asList(instance.getLocale().getMessage("interface.confirmsettings.confirmitemlore")));

		confirmitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
		confirmitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		confirmitem.setItemMeta(confirmitemmeta);
		confirmmenu.setItem(11, confirmitem);
		ItemStack cancelitem = new ItemStack(Material.REDSTONE_BLOCK, 1, (short) 0);
		ItemMeta cancelitemmeta = cancelitem.getItemMeta();
		cancelitemmeta.setDisplayName(instance.getLocale().getMessage("interface.confirmsettings.cancelitemname"));
		cancelitemmeta.setLore(Arrays.asList(instance.getLocale().getMessage("interface.confirmsettings.cancelitemlore")));

		cancelitemmeta.addEnchant(Enchantment.DURABILITY, 1, false);
		cancelitemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		cancelitem.setItemMeta(cancelitemmeta);
		confirmmenu.setItem(15, cancelitem);
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
			if (event.getInventory().getType() != InventoryType.CHEST || !event.getInventory().getTitle().equals(instance.getLocale().getMessage("interface.confirmsettings.title"))) {
			    return;
            }

            Player player = (Player) event.getWhoClicked();
			ItemStack clicked = event.getCurrentItem();
			if (clicked == null) return;
            event.setCancelled(true);
			if (!clicked.getItemMeta().hasDisplayName())
			    return;
			if (clicked.getItemMeta().getDisplayName().equals(instance.getLocale().getMessage("interface.confirmsettings.confirmitemname"))) {
                SoundUtils.playSound(player, "LEVEL_UP", 1);
                player.closeInventory();
                UUID uuid = player.getUniqueId();
                Voucher voucher = vouchercache.get(uuid);
                ItemStack item = itemcache.get(uuid);
                VoucherExecutor.redeemVoucher(player, voucher, item, true);
            } else if (clicked.getItemMeta().getDisplayName().equals(instance.getLocale().getMessage("interface.confirmsettings.cancelitemname"))) {
				UUID uuid = player.getUniqueId();
				vouchercache.remove(uuid);
				itemcache.remove(uuid);
				SoundUtils.playSound(player, "CLICK", 1);
				player.closeInventory();
			}
		} catch (Exception error) {
            Debugger.runReport(error);
		}
	}

}