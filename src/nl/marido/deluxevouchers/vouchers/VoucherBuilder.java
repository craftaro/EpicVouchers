package nl.marido.deluxevouchers.vouchers;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.marido.deluxevouchers.DeluxeVouchers;
import nl.marido.deluxevouchers.handlers.DataHandler;

public class VoucherBuilder {

	public static ItemStack getVoucher(String voucher, int amount) {
		try {
			String path = "vouchers." + voucher + ".";
			String material = DataHandler.getString(DataHandler.vouchers, path + "material");
			String name = DataHandler.getString(DataHandler.vouchers, path + "name");
			ArrayList<String> lore = DataHandler.getStringList(DataHandler.vouchers, path + "lore");
			short data = (short) DataHandler.getInt(DataHandler.vouchers, path + "data");
			ItemStack item = new ItemStack(Material.valueOf(material), amount, data);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			if (DataHandler.getBoolean(DataHandler.vouchers, path + "glow")) {
				meta.addEnchant(Enchantment.DURABILITY, 1, false);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			if (DataHandler.getBoolean(DataHandler.vouchers, path + "unbreakable")) {
				meta.spigot().setUnbreakable(true);
			}
			if (DataHandler.getBoolean(DataHandler.vouchers, path + "hide-attributes")) {
				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}
			item.setItemMeta(meta);
			return item;
		} catch (Exception error) {
			ConsoleCommandSender console = DeluxeVouchers.getConsole();
			console.sendMessage("§cFailed to build and create the voucher " + voucher + ".");
			console.sendMessage("§cMake sure to update your voucher options or reset it.");
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
		return null;
	}

}