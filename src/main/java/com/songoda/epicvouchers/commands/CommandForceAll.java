package com.songoda.epicvouchers.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandForceAll extends AbstractCommand {
    final EpicVouchers instance;

    public CommandForceAll(EpicVouchers instance) {
        super(CommandType.CONSOLE_OK, "forceall");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 2) {
            return ReturnType.SYNTAX_ERROR;
        }

        Voucher voucher = this.instance.getVoucherManager().getVoucher(args[0]);
        if (voucher == null) {
            sender.sendMessage("Unknown voucher...");
            return ReturnType.FAILURE;
        }

        voucher.forceRedeem(sender, new ArrayList<>(Bukkit.getOnlinePlayers()), Integer.parseInt(args[1]));
        this.instance.getLocale().getMessage("command.force.send")
                .processPlaceholder("player", "everyone")
                .processPlaceholder("voucher", voucher.getName(true))
                .processPlaceholder("amount", args[1].trim())
                .sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (Voucher voucher : this.instance.getVoucherManager().getVouchers()) {
                result.add(voucher.getKey());
            }
        } else if (args.length == 2) {
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
        return "/ev forceall <voucher> <amount>";
    }

    @Override
    public String getDescription() {
        return "Force all online users to redeem voucher.";
    }
}
