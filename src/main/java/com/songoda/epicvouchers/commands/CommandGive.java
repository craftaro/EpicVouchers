package com.songoda.epicvouchers.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGive extends AbstractCommand {
    final EpicVouchers instance;

    public CommandGive(EpicVouchers instance) {
        super(false, "give");
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

        voucher.give(sender, Collections.singletonList(player), Integer.parseInt(args[2]));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                result.add(online.getName());
            }
        } else if (args.length == 2) {
            for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
                result.add(voucher.getKey());
            }
        } else if (args.length == 3) {
            for (int i = 0; i < 10; ++i) {
                result.add(String.valueOf(i + 1));
            }
        }

        return result;
    }

    @Override
    public String getPermissionNode() {
        return "epicvouchers.admin";
    }

    @Override
    public String getSyntax() {
        return "/ev give <player> <voucher> <amount>";
    }

    @Override
    public String getDescription() {
        return "Give a voucher to a player.";
    }
}
