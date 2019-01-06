package com.songoda.epicvouchers;

import com.songoda.epicvouchers.command.CommandManager;
import com.songoda.epicvouchers.handlers.Connections;
import com.songoda.epicvouchers.libraries.Bountiful;
import com.songoda.epicvouchers.libraries.FastInv;
import com.songoda.epicvouchers.listeners.PlayerCommandListener;
import com.songoda.epicvouchers.listeners.PlayerInteractListener;
import com.songoda.epicvouchers.utils.*;
import com.songoda.epicvouchers.voucher.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EpicVouchers extends JavaPlugin {

    @Getter private static EpicVouchers instance;
    @Getter private final ServerVersion serverVersion = ServerVersion.fromPackageName(Bukkit.getServer().getClass().getPackage().getName());
    @Getter private CommandManager commandManager;
    @Getter private Connections connections;
    private ConsoleCommandSender console;
    @Getter private CoolDownManager cooldowns;
    @Getter private Locale locale;
    private SettingsManager settingsManager;
    @Getter private VoucherExecutor voucherExecutor;
    @Getter private VoucherManager voucherManager;
    @Getter private ConfigWrapper vouchersFile = new ConfigWrapper(this, "", "vouchers.yml");

    @Override
    public void onEnable() {
        instance = this;
        console = getServer().getConsoleSender();
        console.sendMessage(Methods.formatText("&a============================="));
        console.sendMessage(Methods.formatText("&7EpicVouchers " + this.getDescription().getVersion() + " by &5Brianna <3&7!"));
        console.sendMessage(Methods.formatText("&7Action: &aEnabling&7..."));

        // Locales
        Locale.init(this);
        Locale.saveDefaultLocale("en_US");
        this.locale = Locale.getLocale(getConfig().getString("Locale", "en_US"));

        FastInv.init(this);
        Debugger.init(this);

        this.settingsManager = new SettingsManager(this);
        this.settingsManager.updateSettings();
        getConfig().options().copyDefaults(true);
        saveConfig();

        this.commandManager = new CommandManager(this);
        this.voucherManager = new VoucherManager();
        this.connections = new Connections(this);
        this.cooldowns = new CoolDownManager(this);
        this.voucherExecutor = new VoucherExecutor(this);

        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new PlayerInteractListener(this), this);
        manager.registerEvents(new PlayerCommandListener(), this);

        File folder = getDataFolder();
        File voucherFile = new File(folder, "vouchers.yml");
        if (!voucherFile.exists()) {
            saveResource("vouchers.yml", true);
        }

        loadVouchersFromFile();

//        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::saveToFile, 6000, 6000);

        Bountiful.findVersion();
        connections.openMySQL();
        console.sendMessage(Methods.formatText("&a============================="));
    }

    private void loadVouchersFromFile() {
        /*
         * Register Vouchers into VoucherManger from configuration
         */
        if (vouchersFile.getConfig().contains("vouchers")) {
            for (String key : vouchersFile.getConfig().getConfigurationSection("vouchers").getKeys(false)) {

                Voucher voucher = new Voucher(key);
                ConfigurationSection cs = vouchersFile.getConfig().getConfigurationSection("vouchers." + key);

                voucher.setPermission(cs.getString("permission", ""));
                voucher.setMaterial(Material.valueOf(cs.getString("material", "PAPER")));
                voucher.setData((short) cs.getInt("data", 0));
                voucher.setName(cs.getString("name", "default"));
                voucher.setLore(cs.getStringList("lore"));
                voucher.setGlow(cs.getBoolean("glow", false));
                voucher.setConfirm(cs.getBoolean("confirm", true));
                voucher.setUnbreakable(cs.getBoolean("unbreakable", false));
                voucher.setHideAttributes(cs.getBoolean("hide-attributes", false));
                voucher.setRemoveItem(cs.getBoolean("remove-item", true));
                voucher.setHealPlayer(cs.getBoolean("heal-player", false));
                voucher.setSmiteEffect(cs.getBoolean("smite-effect", false));
                voucher.setCooldown(cs.getInt("cooldown", 0));
                voucher.setBroadcasts(cs.getStringList("broadcasts"));
                voucher.setMessages(cs.getStringList("messages"));
                voucher.setCommands(cs.getStringList("commands"));
                voucher.setActionBar(cs.getString("actionbar"));
                voucher.setTitle(cs.getString("titles.title"));
                voucher.setSubTitle(cs.getString("titles.subtitle"));
                voucher.setTitleFadeIn(cs.getInt("titles.fade-in", 0));
                voucher.setTitleStay(cs.getInt("titles.stay", 0));
                voucher.setTitleFadeOut(cs.getInt("titles.fade-out", 0));
                voucher.setSound(cs.getString("sounds.sound"));
                voucher.setSoundPitch(cs.getInt("sounds.pitch", 0));
                voucher.setParticle(cs.getString("particles.particle"));
                voucher.setParticleAmount(cs.getInt("particles.amount", 0));
                voucher.setEffect(cs.getString("effects.effect"));
                voucher.setEffectAmplifer(cs.getInt("effects.amplifier"));

                voucherManager.addVoucher(key, voucher);
            }
        }
    }

    public void saveToFile(Voucher voucher) {
        ConfigurationSection cs = vouchersFile.getConfig().getConfigurationSection("vouchers." + voucher.getKey());

        cs.set("material", voucher.getMaterial().name());
        cs.set("name", voucher.getName(false));
        cs.set("lore", voucher.getLore(false));

        if (voucher.getData() != 0)
            cs.set("data", voucher.getData());

        vouchersFile.saveConfig();
    }

    public void reload() {
        vouchersFile.reloadConfig();
        getVoucherManager().getVouchers().forEach(this::saveToFile);
        loadVouchersFromFile();

        reloadConfig();
        saveConfig();
        locale.reloadMessages();
    }

    @Override
    public void onDisable() {
        getVoucherManager().getVouchers().forEach(this::saveToFile);
        connections.closeMySQL();
        console.sendMessage(Methods.formatText("&a============================="));
        console.sendMessage(Methods.formatText("&7EpicVouchers " + this.getDescription().getVersion() + " by &5Brianna <3&7!"));
        console.sendMessage(Methods.formatText("&7Action: &cDisabling&7..."));
        console.sendMessage(Methods.formatText("&a============================="));
    }

}