package com.songoda.epicvouchers.command.commands;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.command.AbstractCommand;
import com.songoda.epicvouchers.events.VoucherReceiveEvent;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGive extends AbstractCommand {

    public CommandGive(AbstractCommand parent) {
        super("give", parent, false);
    }

    @Override
    protected ReturnType runCommand(EpicVouchers instance, CommandSender sender, String... args) {
        if (args.length != 4) return ReturnType.SYNTAX_ERROR;
        if (!args[1].equalsIgnoreCase("everyone") && Bukkit.getPlayer(args[1]) == null) {
            sender.sendMessage(instance.getLocale().getMessage("command.error.noplayer"));
            return ReturnType.FAILURE;
        }
        Voucher voucher = instance.getVoucherManager().getVoucher(args[2]);
        if (voucher == null) {
            sender.sendMessage(instance.getLocale().getMessage("command.error.novoucher"));
            return ReturnType.FAILURE;
        }
        try {
            String givemessage = instance.getLocale().getMessage("command.give.send");
            String receivemessage = instance.getLocale().getMessage("command.give.receive");
            int amount = Integer.parseInt(args[3]);
            ItemStack item = voucher.toItemStack(amount);
            String output;
            receivemessage = receivemessage.replaceAll("%voucher%", voucher.getName(true));
            receivemessage = receivemessage.replaceAll("%amount%", String.valueOf(amount));
            if (args[1].equalsIgnoreCase("everyone")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player != sender) {
                        VoucherReceiveEvent event = new VoucherReceiveEvent(player, voucher.getName(true), item, amount, sender);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return ReturnType.FAILURE;
                        }
                        player.sendMessage(receivemessage);
                        player.getInventory().addItem(item);
                        player.updateInventory();
                    }
                }
                output = "everyone";
            } else {
                Player player = Bukkit.getPlayer(args[1]);
                receivemessage = receivemessage.replaceAll("%player%", player.getName());
                VoucherReceiveEvent event = new VoucherReceiveEvent(player, voucher.getName(true), item, amount, sender);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return ReturnType.FAILURE;
                }
                player.sendMessage(receivemessage);
                player.getInventory().addItem(item);
                player.updateInventory();
                output = player.getName();
            }
            givemessage = givemessage.replaceAll("%player%", output);
            givemessage = givemessage.replaceAll("%voucher%", voucher.getName(true));
            givemessage = givemessage.replaceAll("%amount%", String.valueOf(amount));
            sender.sendMessage(givemessage);
        } catch (Exception error) {
            sender.sendMessage(instance.getLocale().getMessage("command.error.notnumber"));
        }

        return ReturnType.SUCCESS;
    }


    @Override
    public String getPermissionNode() {
        return "epicvouchers.admin";
    }

    @Override
    public String getSyntax() {
        return "/epicvouchers give [player/everyone] [section] [amount]";
    }

    @Override
    public String getDescription() {
        return "Give someone or everyone a voucher from your parameters.";
    }
}