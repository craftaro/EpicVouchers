package com.songoda.epicvouchers.command.commands;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.command.AbstractCommand;
import com.songoda.epicvouchers.utils.Methods;
import org.bukkit.command.CommandSender;

public class CommandEpicVouchers extends AbstractCommand {

    public CommandEpicVouchers() {
        super("EpicVouchers", null, false);
    }

    @Override
    protected ReturnType runCommand(EpicVouchers instance, CommandSender sender, String... args) {
        sender.sendMessage("");
        sender.sendMessage(Methods.formatText(instance.getLocale().getMessage("general.nametag.prefix") + "&7Version " + instance.getDescription().getVersion() + " Created with <3 by &5&l&oBrianna"));

        for (AbstractCommand command : instance.getCommandManager().getCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(Methods.formatText("&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }
        sender.sendMessage("");

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/EpicVouchers";
    }

    @Override
    public String getDescription() {
        return "Displays this page.";
    }
}
