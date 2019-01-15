package com.songoda.epicvouchers.command.commands;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.command.AbstractCommand;
import com.songoda.epicvouchers.menus.VoucherMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEditor extends AbstractCommand {

    public CommandEditor(AbstractCommand parent) {
        super("editor", parent, true);
    }

    @Override
    protected ReturnType runCommand(EpicVouchers instance, CommandSender sender, String... args) {
        if (args.length != 1) return ReturnType.SYNTAX_ERROR;

        instance.reload();
        new VoucherMenu(instance).open((Player) sender);
        return ReturnType.SUCCESS;
    }


    @Override
    public String getPermissionNode() {
        return "epicvouchers.admin";
    }

    @Override
    public String getSyntax() {
        return "/vouchers editor";
    }

    @Override
    public String getDescription() {
        return "Edit vouchers in a GUI.";
    }
}