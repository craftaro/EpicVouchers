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

public class CommandGiveAll extends AbstractCommand {

    final EpicVouchers instance;

    public CommandGiveAll(EpicVouchers instance) {
        super(false, "giveall");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 2)
            return ReturnType.SYNTAX_ERROR;

        Voucher voucher = instance.getVoucherManager().getVoucher(args[0]);
        if (voucher == null) {
            sender.sendMessage("Unknown voucher...");
            return ReturnType.FAILURE;
        }

        voucher.giveAll(sender, Integer.parseInt(args[1]));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) {
            return instance.getVoucherManager().getVouchers().stream().map(Voucher::getKey).collect(Collectors.toList());
        } else if (args.length == 2) {
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
        return "/ev giveall <voucher> <amount>";
    }

    @Override
    public String getDescription() {
        return "Give everyone online a voucher.";
    }
}
