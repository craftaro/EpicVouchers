package com.songoda.epicvouchers.command.commands;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.stream.Collectors;

public class CommandList extends AbstractCommand {

    public CommandList(AbstractCommand parent) {
        super("list", parent, false);
    }

    @Override
    protected ReturnType runCommand(EpicVouchers instance, CommandSender sender, String... args) {
        if (args.length != 1) {
            return ReturnType.SYNTAX_ERROR;
        }

        String list = String.join(", ", instance.getVouchers().keySet());
        String message = instance.getLocale().getMessage("command.list.list").replaceAll("%list%", list);
        sender.sendMessage(message);

        return ReturnType.SUCCESS;
    }


    @Override
    public String getPermissionNode() {
        return "epicvouchers.admin";
    }

    @Override
    public String getSyntax() {
        return "/vouchers list";
    }

    @Override
    public String getDescription() {
        return "Displays all vouchers";
    }
}
