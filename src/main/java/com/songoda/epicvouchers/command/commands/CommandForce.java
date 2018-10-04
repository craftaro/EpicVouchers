package com.songoda.epicvouchers.command.commands;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.command.AbstractCommand;
import com.songoda.epicvouchers.events.ForceRedeemEvent;
import com.songoda.epicvouchers.utils.Debugger;
import com.songoda.epicvouchers.voucher.Voucher;
import com.songoda.epicvouchers.voucher.VoucherExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandForce extends AbstractCommand {

    public CommandForce(AbstractCommand parent) {
        super("force", parent, true);
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
            int amount = Integer.parseInt(args[3]);
            String output;
            if (args[1].equalsIgnoreCase("everyone")) {
                output = "everyone";
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player != sender) {
                        ForceRedeemEvent event = new ForceRedeemEvent(player, voucher.getName(), amount, sender);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return ReturnType.FAILURE;
                        }
                        for (int times = 0; times < amount; times++) {
                            VoucherExecutor.redeemVoucher(player, voucher, player.getItemInHand(), false);
                        }
                    }
                }
            } else {
                Player player = Bukkit.getPlayer(args[1]);
                ForceRedeemEvent event = new ForceRedeemEvent(player, voucher.getName(), amount, sender);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return ReturnType.FAILURE;
                }
                output = player.getName();
                for (int times = 0; times < amount; times++) {
                    VoucherExecutor.redeemVoucher(player, voucher, player.getItemInHand(), false);
                }
            }
            String message = instance.getLocale().getMessage("command.force.send", output, voucher.getName(), String.valueOf(amount));
            sender.sendMessage(message);
        } catch (Exception error) {
            sender.sendMessage(instance.getLocale().getMessage("command.error.notnumber"));
            Debugger.runReport(error);
        }
        return ReturnType.SUCCESS;
    }


    @Override
    public String getPermissionNode() {
        return "epicvouchers.admin";
    }

    @Override
    public String getSyntax() {
        return "/epicvouchers force [player/everyone] [section] [amount]";
    }

    @Override
    public String getDescription() {
        return "Force someone or everyone to redeem a specific voucher.";
    }
}