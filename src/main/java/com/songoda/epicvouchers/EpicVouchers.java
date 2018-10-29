package com.songoda.epicvouchers;

import com.songoda.epicvouchers.command.CommandManager;
import com.songoda.epicvouchers.handlers.Connections;
import com.songoda.epicvouchers.handlers.PreventHacks;
import com.songoda.epicvouchers.inventory.Confirmation;
import com.songoda.epicvouchers.inventory.VoucherEditor;
import com.songoda.epicvouchers.liberaries.Bountiful;
import com.songoda.epicvouchers.utils.ConfigWrapper;
import com.songoda.epicvouchers.utils.Methods;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.utils.SettingsManager;
import com.songoda.epicvouchers.voucher.ClickListener;
import com.songoda.epicvouchers.voucher.Cooldowns;
import com.songoda.epicvouchers.voucher.Voucher;
import com.songoda.epicvouchers.voucher.VoucherManager;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EpicVouchers extends JavaPlugin {

    private static EpicVouchers INSTANCE;
    private ConsoleCommandSender console;
    private CommandManager commandManager;
    private References references;
    private Locale locale;
    private VoucherEditor voucherEditor;
    private Confirmation confirmation;
    private VoucherManager voucherManager;
    private Connections connections;
    private Cooldowns cooldowns;
    private SettingsManager settingsManager;

    private ConfigWrapper vouchersFile = new ConfigWrapper(this, "", "vouchers.yml");

    private ServerVersion serverVersion = ServerVersion.fromPackageName(Bukkit.getServer().getClass().getPackage().getName());

    public static EpicVouchers getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        console = this.getServer().getConsoleSender();
        console.sendMessage(Methods.formatText("&a============================="));
        console.sendMessage(Methods.formatText("&7EpicVouchers " + this.getDescription().getVersion() + " by &5Brianna <3&7!"));
        console.sendMessage(Methods.formatText("&7Action: &aEnabling&7..."));
        console = getServer().getConsoleSender();

        // Locales
        Locale.init(this);
        Locale.saveDefaultLocale("en_US");
        this.locale = Locale.getLocale(getConfig().getString("Locale", "en_US"));


        this.settingsManager = new SettingsManager(this);
        this.settingsManager.updateSettings();
        getConfig().options().copyDefaults(true);
        saveConfig();

        this.references = new References();
        this.voucherEditor = new VoucherEditor(this);
        this.commandManager = new CommandManager(this);
        this.confirmation = new Confirmation(this);
        this.voucherManager = new VoucherManager();
        this.connections = new Connections(this);
        this.cooldowns = new Cooldowns();

        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new ClickListener(this), EpicVouchers.getInstance());
        manager.registerEvents(voucherEditor, EpicVouchers.getInstance());
        manager.registerEvents(new Confirmation(this), EpicVouchers.getInstance());
        manager.registerEvents(new PreventHacks(), EpicVouchers.getInstance());

        File folder = getDataFolder();
        File voucherfile = new File(folder, "vouchers.yml");
        if (!voucherfile.exists()) {
            saveResource("vouchers.yml", true);
        }

        loadVouchersFromFile();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::saveToFile, 6000, 6000);

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

                voucher.setPermission(cs.getString("permission"));
                voucher.setMaterial(Material.valueOf(cs.getString("material")));
                voucher.setData((short) cs.getInt("data"));
                voucher.setName(cs.getString("name"));
                voucher.setLore(cs.getStringList("lore"));
                voucher.setGlow(cs.getBoolean("glow"));
                voucher.setConfirm(cs.getBoolean("confirm"));
                voucher.setUnbreakable(cs.getBoolean("unbreakable"));
                voucher.setHideAttributes(cs.getBoolean("hide-attributes"));
                voucher.setRemoveItem(cs.getBoolean("remove-item"));
                voucher.setHealPlayer(cs.getBoolean("heal-player"));
                voucher.setSmiteEffect(cs.getBoolean("smite-effect"));
                voucher.setCooldown(cs.getInt("cooldown"));
                voucher.setBroadcasts(cs.getStringList("broadcasts"));
                voucher.setMessages(cs.getStringList("messages"));
                voucher.setCommands(cs.getStringList("commands"));
                voucher.setActionBar(cs.getString("actionbar"));
                voucher.setTitle(cs.getString("titles.title"));
                voucher.setSubTitle(cs.getString("titles.subtitle"));
                voucher.setTitleFadeIn(cs.getInt("titles.fade-in"));
                voucher.setTitleStay(cs.getInt("titles.stay"));
                voucher.setTitleFadeOut(cs.getInt("titles.fade-out"));
                voucher.setSound(cs.getString("sounds.sound"));
                voucher.setSoundPitch(cs.getInt("sounds.pitch"));
                voucher.setParticle(cs.getString("particles.particle"));
                voucher.setParticleAmount(cs.getInt("particles.amount"));
                voucher.setEffect(cs.getString("effects.effect"));
                voucher.setEffectAmplifer(cs.getInt("effects.amplifier"));

                voucherManager.addVoucher(key, voucher);
            }
        }
    }

    private void saveToFile() {

        vouchersFile.getConfig().set("vouchers", null);

        for (Voucher voucher : voucherManager.getVouchers()) {
            ConfigurationSection cs = vouchersFile.getConfig().createSection("vouchers." + voucher.getKey());

            cs.set("permission", voucher.getPermission());
            cs.set("material", voucher.getMaterial().name());
            cs.set("data", voucher.getData());
            cs.set("name", voucher.getName(false));
            cs.set("lore", voucher.getLore(false));
            cs.set("glow", voucher.isGlow());
            cs.set("confirm", voucher.isConfirm());
            cs.set("unbreakable", voucher.isUnbreakable());
            cs.set("hide-attributes", voucher.isHideAttributes());
            cs.set("remove-item", voucher.isRemoveItem());
            cs.set("heal-player", voucher.isHealPlayer());
            cs.set("smite-effect", voucher.isSmiteEffect());
            cs.set("cooldown", voucher.getCooldown());
            cs.set("broadcasts", voucher.getBroadcasts(false));
            cs.set("messages", voucher.getMessages(false));
            cs.set("commands", voucher.getCommands());
            cs.set("actionbar", voucher.getActionBar());
            cs.set("titles.title", voucher.getTitle());
            cs.set("titles.subtitle", voucher.getSubTitle());
            cs.set("titles.fade-in", voucher.getTitleFadeIn());
            cs.set("titles.stay", voucher.getTitleStay());
            cs.set("titles.fade-out", voucher.getTitleFadeOut());
            cs.set("sounds.sound", voucher.getSound());
            cs.set("sounds.pitch", voucher.getSoundPitch());
            cs.set("particles.particles", voucher.getParticle());
            cs.set("particles.amount", voucher.getParticleAmount());
            cs.set("effects.effect", voucher.getEffect());
            cs.set("effects.amplifier", voucher.getEffectAmplifer());
        }

        this.vouchersFile.saveConfig();
    }

    public void reload() {
        this.vouchersFile = new ConfigWrapper(this, "", "vouchers.yml");
        loadVouchersFromFile();
        locale.reloadMessages();
        references = new References();
        reloadConfig();
        saveConfig();
    }

    @Override
    public void onDisable() {
        this.saveToFile();
        connections.closeMySQL();
        vouchersFile.saveConfig();
        console.sendMessage(Methods.formatText("&a============================="));
        console.sendMessage(Methods.formatText("&7EpicVouchers " + this.getDescription().getVersion() + " by &5Brianna <3&7!"));
        console.sendMessage(Methods.formatText("&7Action: &cDisabling&7..."));
        console.sendMessage(Methods.formatText("&a============================="));

    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public boolean isServerVersion(ServerVersion version) {
        return serverVersion == version;
    }

    public boolean isServerVersion(ServerVersion... versions) {
        return ArrayUtils.contains(versions, serverVersion);
    }

    public boolean isServerVersionAtLeast(ServerVersion version) {
        return serverVersion.ordinal() >= version.ordinal();
    }

    public Locale getLocale() {
        return locale;
    }

    public References getReferences() {
        return references;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public VoucherEditor getVoucherEditor() {
        return voucherEditor;
    }

    public VoucherManager getVoucherManager() {
        return voucherManager;
    }

    public Cooldowns getCooldowns() {
        return cooldowns;
    }

    public Connections getConnections() {
        return connections;
    }

}