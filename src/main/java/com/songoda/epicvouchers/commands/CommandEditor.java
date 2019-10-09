package com.songoda.epicvouchers.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.menus.VoucherMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandEditor extends AbstractCommand {

    final EpicVouchers instance;

    public CommandEditor(EpicVouchers instance) {
        super(true, "editor");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        new VoucherMenu(instance).open((Player) sender);
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
        return "/ev editor";
    }

    @Override
    public String getDescription() {
        return "Opens the voucher editor.";
    }
}
