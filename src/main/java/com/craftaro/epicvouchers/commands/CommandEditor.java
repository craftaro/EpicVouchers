package com.craftaro.epicvouchers.commands;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.menus.VoucherMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandEditor extends AbstractCommand {
    final EpicVouchers instance;

    public CommandEditor(EpicVouchers instance) {
        super(CommandType.PLAYER_ONLY, "editor");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        new VoucherMenu(this.instance).open((Player) sender);
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
