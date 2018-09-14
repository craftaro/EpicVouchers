package nl.marido.deluxevouchers.handlers;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import nl.marido.deluxevouchers.DeluxeVouchers;
import nl.marido.deluxevouchers.events.ForceRedeemEvent;
import nl.marido.deluxevouchers.events.VoucherReceiveEvent;
import nl.marido.deluxevouchers.inventory.VoucherEditor;
import nl.marido.deluxevouchers.vouchers.VoucherBuilder;
import nl.marido.deluxevouchers.vouchers.VoucherExecutor;

public class Commandos implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command alias, String command, String[] args) {
		try {
			if (sender.hasPermission("deluxevouchers.admin") || sender.isOp()) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						DataHandler.cacheData();
						sender.sendMessage(DataHandler.commandreload);
						return true;
					}
					if (args[0].equalsIgnoreCase("editor")) {
						if (sender instanceof Player) {
							VoucherEditor.openMenu((Player) sender);
							return true;
						}
						DeluxeVouchers.printConsole("§cYou can not use this command as a console.");
						return true;
					}
					if (args[0].equalsIgnoreCase("disable")) {
						sender.sendMessage(DataHandler.commanddisable);
						DeluxeVouchers.getInstance().getPluginLoader().disablePlugin(DeluxeVouchers.getInstance());
						return true;
					}
					if (args[0].equalsIgnoreCase("list")) {
						String message = DataHandler.commandlist;
						String list = DataHandler.getSection(DataHandler.vouchers, "vouchers").toString();
						list = list.replaceAll("[()\\[\\]]", "");
						message = message.replaceAll("%list%", list);
						sender.sendMessage(message);
						return true;
					}
					if (args[0].equalsIgnoreCase("reset")) {
						DeluxeVouchers.getInstance().saveResource("config.yml", true);
						DeluxeVouchers.getInstance().saveResource("vouchers.yml", true);
						DeluxeVouchers.getInstance().saveResource("mysql.yml", true);
						DataHandler.cacheData();
						sender.sendMessage(DataHandler.commandreset);
						return true;
					}
					if (args[0].equalsIgnoreCase("backup")) {
						try {
							String folder = String.valueOf(DeluxeVouchers.getInstance().getDataFolder()) + "/" + System.currentTimeMillis();
							new File(folder).mkdir();
							File configfile = new File(folder, "config.yml");
							File vouchersfile = new File(folder, "vouchers.yml");
							File mysqlfile = new File(folder, "mysql.yml");
							configfile.createNewFile();
							vouchersfile.createNewFile();
							mysqlfile.createNewFile();
							DataHandler.config.save(configfile);
							DataHandler.vouchers.save(vouchersfile);
							DataHandler.mysql.save(mysqlfile);
							sender.sendMessage(DataHandler.commandbackup);
						} catch (Exception error) {
							error.printStackTrace();
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("license")) {
						String message = DataHandler.commandlicense;
						message = message.replaceAll("%license%", "DV" + DeluxeVouchers.user);
						sender.sendMessage(message);
						return true;
					}
					if (args[0].equalsIgnoreCase("help")) {
						for (String message : DataHandler.commandhelp) {
							sender.sendMessage(message);
						}
						return true;
					}
					sender.sendMessage(DataHandler.commandinvalid);
					return true;
				}
				if (args.length == 4) {
					if (args[0].equalsIgnoreCase("give")) {
						if (args[1].equalsIgnoreCase("everyone") || Bukkit.getPlayer(args[1]) != null) {
							if (DataHandler.stringExist(DataHandler.vouchers, "vouchers." + args[2])) {
								String voucher = args[2];
								try {
									String givemessage = DataHandler.commandgive;
									String receivemessage = DataHandler.commandreceive;
									int amount = Integer.parseInt(args[3]);
									ItemStack item = VoucherBuilder.getVoucher(voucher, amount);
									String output;
									receivemessage = receivemessage.replaceAll("%voucher%", voucher);
									receivemessage = receivemessage.replaceAll("%amount%", String.valueOf(amount));
									if (args[1].equalsIgnoreCase("everyone")) {
										for (Player player : Bukkit.getOnlinePlayers()) {
											if (player != sender) {
												VoucherReceiveEvent event = new VoucherReceiveEvent(player, voucher, item, amount, sender);
												Bukkit.getServer().getPluginManager().callEvent(event);
												if (event.isCancelled()) {
													return true;
												}
												player.sendMessage(receivemessage);
												player.getInventory().addItem(item);
												player.updateInventory();
											}
										}
										output = "everyone";
									} else {
										Player player = Bukkit.getPlayer(args[1]);
										VoucherReceiveEvent event = new VoucherReceiveEvent(player, voucher, item, amount, sender);
										Bukkit.getServer().getPluginManager().callEvent(event);
										if (event.isCancelled()) {
											return true;
										}
										player.sendMessage(receivemessage);
										player.getInventory().addItem(item);
										player.updateInventory();
										output = player.getName();
									}
									givemessage = givemessage.replaceAll("%player%", output);
									givemessage = givemessage.replaceAll("%voucher%", voucher);
									givemessage = givemessage.replaceAll("%amount%", String.valueOf(amount));
									sender.sendMessage(givemessage);
								} catch (Exception error) {
									sender.sendMessage(DataHandler.commandnonumber);
								}
								return true;
							}
							sender.sendMessage(DataHandler.commandnovoucher);
							return true;
						}
						sender.sendMessage(DataHandler.commandnoplayer);
						return true;
					}
					if (args[0].equalsIgnoreCase("force")) {
						if (args[1].equalsIgnoreCase("everyone") || Bukkit.getPlayer(args[1]) != null) {
							if (DataHandler.stringExist(DataHandler.vouchers, "vouchers." + args[2])) {
								String voucher = args[2];
								try {
									int amount = Integer.parseInt(args[3]);
									String output;
									if (args[1].equalsIgnoreCase("everyone")) {
										output = "everyone";
										for (Player player : Bukkit.getOnlinePlayers()) {
											if (player != sender) {
												ForceRedeemEvent event = new ForceRedeemEvent(player, voucher, amount, sender);
												Bukkit.getServer().getPluginManager().callEvent(event);
												if (event.isCancelled()) {
													return true;
												}
												for (int times = 0; times < amount; times++) {
													VoucherExecutor.redeemVoucher(player, voucher, player.getItemInHand(), false);
												}
											}
										}
									} else {
										Player player = Bukkit.getPlayer(args[1]);
										ForceRedeemEvent event = new ForceRedeemEvent(player, voucher, amount, sender);
										Bukkit.getServer().getPluginManager().callEvent(event);
										if (event.isCancelled()) {
											return true;
										}
										output = player.getName();
										for (int times = 0; times < amount; times++) {
											VoucherExecutor.redeemVoucher(player, voucher, player.getItemInHand(), false);
										}
									}
									String message = DataHandler.commandforce;
									message = message.replaceAll("%player%", output);
									message = message.replaceAll("%voucher%", voucher);
									message = message.replaceAll("%amount%", String.valueOf(amount));
									sender.sendMessage(message);
								} catch (Exception error) {
									sender.sendMessage(DataHandler.commandnonumber);
									if (DataHandler.debugerrors) {
										error.printStackTrace();
									}
								}
								return true;
							}
							sender.sendMessage(DataHandler.commandnovoucher);
							return true;
						}
						sender.sendMessage(DataHandler.commandnoplayer);
						return true;
					}
					sender.sendMessage(DataHandler.commandinvalid);
					return true;
				}
				sender.sendMessage(DataHandler.commandinvalid);
				return true;
			}
			sender.sendMessage(DataHandler.commandpermission);
			return true;
		} catch (Exception error) {
			String fullcommand = "/deluxevouchers";
			for (int argint = 0; argint < args.length; argint++) {
				fullcommand = fullcommand + " " + args[argint];
			}
			DeluxeVouchers.printConsole("§cFailed to execute the command " + fullcommand + " by " + sender.getName() + ".");
			if (DataHandler.debugerrors) {
				error.printStackTrace();
			}
		}
		return false;
	}

}