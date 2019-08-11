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
        instance.getLocale().getMessage("command.reload.success").sendPrefixedMessage(sender);
    }

    @Subcommand("list")
    @CommandPermission("epicvouchers.admin")
    @Description("List all available vouchers.")
    public void onList(CommandSender sender) {
        instance.getLocale().getMessage("command.list.list")
                .processPlaceholder("list", String.join(", ", instance.getVouchers().keySet()))
                .sendPrefixedMessage(sender);
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
        instance.getLocale().getMessage("command.force.send")
                .processPlaceholder("player", player.getName())
                .processPlaceholder("voucher", voucher.getName(true))
                .processPlaceholder("amount", String.valueOf(amount))
                .sendPrefixedMessage(sender);
    }

    @Subcommand("forceall")
    @CommandPermission("epicvouchers.admin")
    @CommandCompletion("@vouchers @range:1-10")
    @Description("Force all online users to redeem voucher.")
    public void onForceAll(CommandSender sender, Voucher voucher, int amount) {
        voucher.forceRedeem(sender, new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
        instance.getLocale().getMessage("command.force.send")
                .processPlaceholder("player", "everyone")
                .processPlaceholder("voucher", voucher.getName(true))
                .processPlaceholder("amount", String.valueOf(amount))
                .sendPrefixedMessage(sender);
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
