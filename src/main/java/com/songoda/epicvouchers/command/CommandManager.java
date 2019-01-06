package com.songoda.epicvouchers.command;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.command.commands.*;
import com.songoda.epicvouchers.utils.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private final EpicVouchers instance;

    private final List<AbstractCommand> commands = new ArrayList<>();

    public CommandManager(EpicVouchers instance) {
        this.instance = instance;

        instance.getCommand("EpicVouchers").setExecutor(this);

        AbstractCommand commandEpicVouchers = addCommand(new CommandEpicVouchers());

        addCommand(new CommandReload(commandEpicVouchers));
        addCommand(new CommandEditor(commandEpicVouchers));
        addCommand(new CommandList(commandEpicVouchers));
        addCommand(new CommandGive(commandEpicVouchers));
        addCommand(new CommandForce(commandEpicVouchers));
    }

    private AbstractCommand addCommand(AbstractCommand abstractCommand) {
        commands.add(abstractCommand);
        return abstractCommand;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (AbstractCommand abstractCommand : commands) {
            if (abstractCommand.getCommand().equalsIgnoreCase(command.getName())) {
                if (strings.length == 0) {
                    processRequirements(abstractCommand, commandSender, strings);
                    return true;
                }
            } else if (strings.length != 0 && abstractCommand.getParent() != null && abstractCommand.getParent().getCommand().equalsIgnoreCase(command.getName())) {
                String cmd = strings[0];
                if (cmd.equalsIgnoreCase(abstractCommand.getCommand())) {
                    processRequirements(abstractCommand, commandSender, strings);
                    return true;
                }
            }
        }
        commandSender.sendMessage(instance.getLocale().getMessage("general.nametag.prefix") + Methods.formatText(instance.getLocale().getMessage("command.error.notexist")));
        return true;
    }

    private void processRequirements(AbstractCommand command, CommandSender sender, String[] strings) {
        if (!(sender instanceof Player) && command.isNoConsole() ) {
            sender.sendMessage(instance.getLocale().getMessage("command.error.noconsole"));
            return;
        }
        if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
             AbstractCommand.ReturnType returnType = command.runCommand(instance, sender, strings);
            if (returnType == AbstractCommand.ReturnType.NO_CONSOLE) {
                sender.sendMessage(instance.getLocale().getMessage("command.error.noconsole"));
                return;
            }
             if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                 sender.sendMessage(instance.getLocale().getMessage("general.nametag.prefix") + Methods.formatText("&cInvalid Syntax!"));
                 sender.sendMessage(instance.getLocale().getMessage("general.nametag.prefix") + Methods.formatText("&7The valid syntax is: &6" + command.getSyntax() + "&7."));
             }
            return;
        }
        sender.sendMessage(instance.getLocale().getMessage("general.nametag.prefix") + instance.getLocale().getMessage("event.general.nopermission"));
    }

    public List<AbstractCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }
}
