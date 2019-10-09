package com.songoda.epicvouchers.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.menus.VoucherMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandList extends AbstractCommand {

    final EpicVouchers instance;

    public CommandList(EpicVouchers instance) {
        super(false, "list");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        instance.getLocale().getMessage("command.list.list")
                .processPlaceholder("list", String.join(", ", instance.getVouchers().keySet()))
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
