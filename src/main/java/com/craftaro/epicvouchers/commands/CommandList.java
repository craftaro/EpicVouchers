package com.craftaro.epicvouchers.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.voucher.Voucher;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.StringJoiner;

public class CommandList extends AbstractCommand {
    final EpicVouchers instance;

    public CommandList(EpicVouchers instance) {
        super(CommandType.CONSOLE_OK, "list");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Voucher voucher : this.instance.getVoucherManager().getVouchers()) {
            joiner.add(voucher.getKey());
        }

        this.instance.getLocale().getMessage("command.list.list")
                .processPlaceholder("list", joiner.toString())
                .sendPrefixedMessage(sender);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicvouchers.admin";
    }

    @Override
    public String getSyntax() {
        return "/ev list";
    }

    @Override
    public String getDescription() {
        return "List all available vouchers.";
    }
}
