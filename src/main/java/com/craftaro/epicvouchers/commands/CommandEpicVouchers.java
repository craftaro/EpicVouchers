package com.craftaro.epicvouchers.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.epicvouchers.EpicVouchers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandEpicVouchers extends AbstractCommand {
    final EpicVouchers instance;

    public CommandEpicVouchers(EpicVouchers instance) {
        super(CommandType.CONSOLE_OK, "EpicVouchers");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        sender.sendMessage("");
        this.instance.getLocale().newMessage("&7Version " + this.instance.getDescription().getVersion()
                + " Created with <3 by &5&l&oCraftaro").sendPrefixedMessage(sender);

        for (AbstractCommand command : this.instance.getCommandManager().getAllCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }
        sender.sendMessage("");

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
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
