package com.songoda.epicvouchers;

import com.songoda.epicvouchers.command.CommandManager;
import com.songoda.epicvouchers.handlers.Connections;
import com.songoda.epicvouchers.libraries.Bountiful;
import com.songoda.epicvouchers.libraries.inventory.FastInv;
import com.songoda.epicvouchers.libraries.inventory.IconInv;
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
import java.util.LinkedHashMap;

public class EpicVouchers extends JavaPlugin {

    @Getter private static EpicVouchers instance;
    @Getter private final ServerVersion serverVersion = ServerVersion.fromPackageName(Bukkit.getServer().getClass().getPackage().getName());
    @Getter private CommandManager commandManager;
    @Getter private Connections connections;
    @Getter private CoolDownManager cooldowns;
    @Getter private Locale locale;
    private SettingsManager settingsManager;
    @Getter private VoucherExecutor voucherExecutor;
    @Getter private ConfigWrapper vouchersFile = new ConfigWrapper(this, "", "vouchers.yml");
    @Getter private LinkedHashMap<String, Voucher> vouchers;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getConsoleSender().sendMessage(Methods.format("&a============================="));
        Bukkit.getConsoleSender().sendMessage(Methods.format("&7EpicVouchers " + this.getDescription().getVersion() + " by &5Songoda <3&7!"));
        Bukkit.getConsoleSender().sendMessage(Methods.format("&7Action: &aEnabling&7..."));

        // Locales
        Locale.init(this);
        Locale.saveDefaultLocale("en_US");
        this.locale = Locale.getLocale(getConfig().getString("Locale", "en_US"));

        FastInv.init(this);
        IconInv.init(this);
        Debugger.init(this);

        this.settingsManager = new SettingsManager(this);
        this.settingsManager.updateSettings();
        this.vouchers = new LinkedHashMap<>();
        getConfig().options().copyDefaults(true);
        saveConfig();

        this.commandManager = new CommandManager(this);
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

        Bountiful.findVersion();
        connections.openMySQL();
        Bukkit.getConsoleSender().sendMessage(Methods.format("&a============================="));
    }

    private void loadVouchersFromFile() {
        vouchers.clear();
        /*
         * Register Vouchers into VoucherManger from configuration
         */
        if (vouchersFile.getConfig().contains("vouchers")) {
            for (String key : vouchersFile.getConfig().getConfigurationSection("vouchers").getKeys(false)) {

                Voucher voucher = new Voucher(key);
                ConfigurationSection cs = vouchersFile.getConfig().getConfigurationSection("vouchers." + key);
                Material material = cs.getString("material") == null || cs.getString("material").equals("") ? Material.PAPER :
                        Material.matchMaterial(cs.getString("material")) == null ? Material.PAPER : Material.matchMaterial(cs.getString("material"));

                voucher.setPermission(cs.getString("permission", ""));
                voucher.setMaterial(material);
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
                voucher.setEffectAmplifier(cs.getInt("effects.amplifier"));
                voucher.setItemStack(cs.getItemStack("itemstack", null));

                vouchers.put(key, voucher);
            }
        }
    }

    public void reload() {
        vouchersFile.reloadConfig();
        loadVouchersFromFile();

        reloadConfig();
        saveConfig();
        locale.reloadMessages();
    }

    @Override
    public void onDisable() {
        connections.closeMySQL();
        Bukkit.getConsoleSender().sendMessage(Methods.format("&a============================="));
        Bukkit.getConsoleSender().sendMessage(Methods.format("&7EpicVouchers " + this.getDescription().getVersion() + " by &5Songoda <3&7!"));
        Bukkit.getConsoleSender().sendMessage(Methods.format("&7Action: &cDisabling&7..."));
        Bukkit.getConsoleSender().sendMessage(Methods.format("&a============================="));
    }

}