package com.songoda.epicvouchers.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.voucher.Voucher;

import java.util.Optional;

public class CommandManager extends BukkitCommandManager {

    private EpicVouchers instance;

    public CommandManager(EpicVouchers instance) {
        super(instance);
        this.instance = instance;

        registerDependency(EpicVouchers.class, "instance", instance);

        getCommandContexts().registerContext(Voucher.class, c ->
                Optional.ofNullable(instance.getVouchers().get(c.popFirstArg().toLowerCase())).orElseThrow(() ->
                        new InvalidCommandArgument("Unknown voucher.")));

        getCommandCompletions().registerCompletion("vouchers", c -> instance.getVouchers().keySet());

        registerCommand(new EfCommand());

        enableUnstableAPI("help");
    }
}
