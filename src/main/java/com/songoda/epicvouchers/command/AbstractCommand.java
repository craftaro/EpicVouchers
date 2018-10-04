package com.songoda.epicvouchers.command;

import com.songoda.epicvouchers.EpicVouchers;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {

    public enum ReturnType { SUCCESS, FAILURE, SYNTAX_ERROR, NO_CONSOLE }

    private final AbstractCommand parent;

    private final String command;

    private final boolean noConsole;

    protected AbstractCommand(String command, AbstractCommand parent, boolean noConsole) {
        this.command = command;
        this.parent = parent;
        this.noConsole = noConsole;
    }

    public AbstractCommand getParent() {
        return parent;
    }

    public String getCommand() {
        return command;
    }

    public boolean isNoConsole() {
        return noConsole;
    }

    protected abstract ReturnType runCommand(EpicVouchers instance, CommandSender sender, String... args);

    public abstract String getPermissionNode();

    public abstract String getSyntax();

    public abstract String getDescription();
}
