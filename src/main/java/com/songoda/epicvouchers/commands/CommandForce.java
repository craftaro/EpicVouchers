package com.songoda.epicvouchers.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandForce extends AbstractCommand {

    final EpicVouchers instance;

    public CommandForce(EpicVouchers instance) {
        super(false, "force");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 3)
            return ReturnType.SYNTAX_ERROR;

        Player player = Bukkit.getPlayer(args[0]);
        if (Bukkit.getPlayer(args[0]) == null) {
            instance.getLocale().newMessage("&cThat player does not exist or is currently offline.").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        Voucher voucher = instance.getVoucherManager().getVoucher(args[1]);
        if (voucher == null) {
            sender.sendMessage("Unknown voucher...");
            return ReturnType.FAILURE;
        }

        voucher.forceRedeem(sender, Collections.singletonList(player), Integer.parseInt(args[2]));
        instance.getLocale().getMessage("command.force.send")
                .processPlaceholder("player", player.getName())
                .processPlaceholder("voucher", voucher.getName(true))
                .processPlaceholder("amount", args[2].trim())
                .sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        } else if (args.length == 2) {
            return instance.getVoucherManager().getVouchers().stream().map(Voucher::getKey).collect(Collectors.toList());
        } else if (args.length == 3) {
            return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicvouchers.admin";
    }

    @Override
    public String getSyntax() {
        return "/ev force <player> <voucher> <amount>";
    }

    @Override
    public String getDescription() {
        return "Force user to redeem voucher.";
    }
}
