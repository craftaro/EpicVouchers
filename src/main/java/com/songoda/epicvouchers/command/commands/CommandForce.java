package com.songoda.epicvouchers.command.commands;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.command.AbstractCommand;
import com.songoda.epicvouchers.events.ForceRedeemEvent;
import com.songoda.epicvouchers.voucher.Voucher;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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

        if (!instance.getVouchers().containsKey(args[2])) {
            sender.sendMessage(instance.getLocale().getMessage("command.error.novoucher"));
            return ReturnType.FAILURE;
        }

        Voucher voucher = instance.getVouchers().get(args[2]);

        if (!StringUtils.isNumeric(args[3])) {
            sender.sendMessage(instance.getLocale().getMessage("command.error.notnumber"));
            return ReturnType.SUCCESS;
        }

        Collection<Player> players;
        String output;
        int amount = Integer.parseInt(args[3]);

        if(args[1].equalsIgnoreCase("everyone")) {
            players = (Collection<Player>) Bukkit.getOnlinePlayers();
            output = "everyone";
        } else {
            players = Collections.singletonList(Bukkit.getPlayer(args[1]));
            output = Bukkit.getPlayer(args[1]).getName();
        }

        players.forEach(player -> {
            ForceRedeemEvent event = new ForceRedeemEvent(player, voucher.getName(true), amount, sender);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            for (int times = 0; times < amount; times++) {
                instance.getVoucherExecutor().redeemVoucher(player, voucher, player.getItemInHand(), false);
            }
        });
        String message = instance.getLocale().getMessage("command.force.send", output, voucher.getName(true), String.valueOf(amount));
        sender.sendMessage(message);
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