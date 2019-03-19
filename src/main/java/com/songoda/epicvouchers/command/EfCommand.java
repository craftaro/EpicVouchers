package com.songoda.epicvouchers.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.menus.VoucherMenu;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;

@CommandAlias("epicvouchers|ev")
public class EfCommand extends BaseCommand {

    @Dependency
    private EpicVouchers instance;

    @Subcommand("reload")
    @CommandPermission("epicvouchers.admin")
    @Description("Reload all configuration files.")
    public void onReload(CommandSender sender) {
        instance.reload();
        sender.sendMessage(instance.getLocale().getMessage("command.reload.success"));
    }

    @Subcommand("list")
    @CommandPermission("epicvouchers.admin")
    @Description("List all available vouchers.")
    public void onList(CommandSender sender) {
        String message = instance.getLocale().getMessage("command.list.list").replaceAll("%list%", String.join(", ", instance.getVouchers().keySet()));
        sender.sendMessage(message);
    }

    @Subcommand("give")
    @CommandPermission("epicvouchers.admin")
    @CommandCompletion("@players @vouchers @range:1-10")
    @Description("Give a voucher to a player.")
    public void onGivePlayer(CommandSender sender, @Flags("other") Player player, Voucher voucher, int amount) {
        voucher.give(sender, Collections.singletonList(player), amount);
    }

    @Subcommand("giveall")
    @CommandPermission("epicvouchers.admin")
    @CommandCompletion("@vouchers @range:1-10")
    @Description("Give everyone online a voucher.")
    public void onGiveAll(CommandSender sender, Voucher voucher, int amount) {
        voucher.giveAll(sender, amount);
    }

    @Subcommand("force")
    @CommandPermission("epicvouchers.admin")
    @CommandCompletion("@players @vouchers @range:1-10")
    @Description("Force user to redeem voucher.")
    public void onForce(CommandSender sender, @Flags("other") Player player, Voucher voucher, int amount) {
        voucher.forceRedeem(sender, Collections.singletonList(player), amount);
        sender.sendMessage(instance.getLocale().getMessage("command.force.send", player.getName(), voucher.getName(true), String.valueOf(amount)));
    }

    @Subcommand("forceall")
    @CommandPermission("epicvouchers.admin")
    @CommandCompletion("@vouchers @range:1-10")
    @Description("Force all online users to redeem voucher.")
    public void onForceAll(CommandSender sender, Voucher voucher, int amount) {
        voucher.forceRedeem(sender, new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
        sender.sendMessage(instance.getLocale().getMessage("command.force.send", "everyone", voucher.getName(true), String.valueOf(amount)));
    }

    @Subcommand("editor")
    @CommandPermission("epicvouchers.admin")
    @Description("Opens the voucher editor.")
    public void onEditor(Player player) {
        new VoucherMenu(instance).open(player);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
