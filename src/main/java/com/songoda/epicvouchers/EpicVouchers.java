package com.songoda.epicvouchers;

import com.songoda.core.SongodaCore;
import com.songoda.core.SongodaPlugin;
import com.songoda.core.commands.CommandManager;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.configuration.Config;
import com.songoda.core.gui.GuiManager;
import com.songoda.epicvouchers.commands.*;
import com.songoda.epicvouchers.handlers.Connections;
import com.songoda.epicvouchers.libraries.BountifulAPI;
import com.songoda.epicvouchers.libraries.inventory.FastInv;
import com.songoda.epicvouchers.libraries.inventory.IconInv;
import com.songoda.epicvouchers.listeners.PlayerCommandListener;
import com.songoda.epicvouchers.listeners.PlayerInteractListener;
import com.songoda.epicvouchers.settings.Settings;
import com.songoda.epicvouchers.voucher.CoolDownManager;
import com.songoda.epicvouchers.voucher.Voucher;
import com.songoda.epicvouchers.voucher.VoucherExecutor;
import com.songoda.epicvouchers.voucher.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import java.util.Collections;
import java.util.List;

public class EpicVouchers extends SongodaPlugin {

    private static EpicVouchers INSTANCE;

    private final GuiManager guiManager = new GuiManager(this);
    private CommandManager commandManager;
    private VoucherManager voucherManager;

    private Connections connections;
    private CoolDownManager coolDowns;
    private VoucherExecutor voucherExecutor;
    private Config vouchersConfig = new Config(this, "vouchers.yml");

    public static EpicVouchers getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPluginLoad() {
        INSTANCE = this;
    }

    @Override
    public void onPluginDisable() {
        connections.closeMySQL();
        saveVouchers();
    }

    @Override
    public void onPluginEnable() {
        // Run Songoda Updater
        SongodaCore.registerPlugin(this, 25, CompatibleMaterial.EMERALD);

        // Setup Config
        Settings.setupConfig();
        this.setLocale(Settings.LANGUGE_MODE.getString(), false);

        // Register commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addCommand(new CommandEpicVouchers(this))
                .addSubCommands(
                        new CommandEditor(this),
                        new CommandForce(this),
                        new CommandForceAll(this),
                        new CommandGive(this),
                        new CommandGiveAll(this),
                        new CommandList(this),
                        new CommandReload(this)
                );

        FastInv.init(this);
        IconInv.init(this);
        BountifulAPI.init(this);

        this.connections = new Connections(this);
        this.coolDowns = new CoolDownManager(this);
        this.voucherExecutor = new VoucherExecutor(this);
        this.voucherManager = new VoucherManager();

        PluginManager manager = Bukkit.getServer().getPluginManager();

        // Listeners
        guiManager.init();
        manager.registerEvents(new PlayerInteractListener(this), this);
        manager.registerEvents(new PlayerCommandListener(), this);

        saveResource("vouchers.yml", false);
        vouchersConfig.load();

        loadVouchersFromFile();

        connections.openMySQL();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::saveVouchers, 6000, 6000);
    }

    private void loadVouchersFromFile() {
        voucherManager.clearVouchers();

        if (vouchersConfig.contains("vouchers")) {
            for (String key : vouchersConfig.getConfigurationSection("vouchers").getKeys(false)) {
                Voucher voucher = new Voucher(key, this);
                ConfigurationSection cs = vouchersConfig.getConfigurationSection("vouchers." + key);

                Material material;
                String stringMaterial = cs.getString("material");

                if (stringMaterial == null || stringMaterial.isEmpty()) {
                    material = Material.PAPER;
                } else {
                    material = Material.matchMaterial(stringMaterial);
                    if (material == null) material = Material.PAPER;
                }

                voucher.setPermission(cs.getString("permission", ""))
                        .setMaterial(material)
                        .setData((short) cs.getInt("data", 0))
                        .setName(cs.getString("name", "default"))
                        .setLore(cs.getStringList("lore"))
                        .setGlow(cs.getBoolean("glow", false))
                        .setConfirm(cs.getBoolean("confirm", true))
                        .setUnbreakable(cs.getBoolean("unbreakable", false))
                        .setHideAttributes(cs.getBoolean("hide-attributes", false))
                        .setRemoveItem(cs.getBoolean("remove-item", true))
                        .setHealPlayer(cs.getBoolean("heal-player", false))
                        .setSmiteEffect(cs.getBoolean("smite-effect", false))
                        .setCoolDown(cs.getInt("coolDown", 0))
                        .setBroadcasts(cs.getStringList("broadcasts"))
                        .setMessages(cs.getStringList("messages"))
                        .setCommands(cs.getStringList("commands"))
                        .setActionBar(cs.getString("actionbar"))
                        .setTitle(cs.getString("titles.title"))
                        .setSubTitle(cs.getString("titles.subtitle"))
                        .setTitleFadeIn(cs.getInt("titles.fade-in", 0))
                        .setTitleStay(cs.getInt("titles.stay", 0))
                        .setTitleFadeOut(cs.getInt("titles.fade-out", 0))
                        .setSound(cs.getString("sounds.sound"))
                        .setSoundPitch(cs.getInt("sounds.pitch", 0))
                        .setParticle(cs.getString("particles.particle"))
                        .setParticleAmount(cs.getInt("particles.amount", 0))
                        .setEffect(cs.getString("effects.effect"))
                        .setEffectAmplifier(cs.getInt("effects.amplifier"))
                        .setItemStack(cs.getItemStack("itemstack", null));

                voucherManager.addVoucher(voucher);
            }
        }
    }

    private void saveVouchers() {
        for (String voucherName : vouchersConfig.getConfigurationSection("vouchers").getKeys(false)) {
            if (voucherManager.getVouchers().stream().noneMatch(voucher -> voucher.getKey().equals(voucherName)))
                vouchersConfig.set("vouchers." + voucherName, null);
        }

        for (Voucher voucher : voucherManager.getVouchers()) {
            String prefix = "vouchers." + voucher.getKey() + ".";
            vouchersConfig.set(prefix + "permission", voucher.getPermission());
            vouchersConfig.set(prefix + "material", voucher.getMaterial().name());
            vouchersConfig.set(prefix + "data", voucher.getData());
            vouchersConfig.set(prefix + "name", voucher.getName());
            vouchersConfig.set(prefix + "lore", voucher.getLore());
            vouchersConfig.set(prefix + "glow", voucher.isGlow());
            vouchersConfig.set(prefix + "confirm", voucher.isConfirm());
            vouchersConfig.set(prefix + "unbreakable", voucher.isUnbreakable());
            vouchersConfig.set(prefix + "hide-attributes", voucher.isHideAttributes());
            vouchersConfig.set(prefix + "remove-item", voucher.isRemoveItem());
            vouchersConfig.set(prefix + "heal-player", voucher.isHealPlayer());
            vouchersConfig.set(prefix + "smite-effect", voucher.isSmiteEffect());
            vouchersConfig.set(prefix + "coolDown", voucher.getCoolDown());
            vouchersConfig.set(prefix + "broadcasts", voucher.getBroadcasts());
            vouchersConfig.set(prefix + "messages", voucher.getMessages());
            vouchersConfig.set(prefix + "commands", voucher.getCommands());
            vouchersConfig.set(prefix + "actionbar", voucher.getActionBar());
            vouchersConfig.set(prefix + "titles.title", voucher.getTitle());
            vouchersConfig.set(prefix + "titles.subtitle", voucher.getSubTitle());
            vouchersConfig.set(prefix + "titles.fade-in", voucher.getTitleFadeIn());
            vouchersConfig.set(prefix + "titles.stay", voucher.getTitleStay());
            vouchersConfig.set(prefix + "titles.fade-out", voucher.getTitleFadeOut());
            vouchersConfig.set(prefix + "sounds.sound", voucher.getSound());
            vouchersConfig.set(prefix + "sounds.pitch", voucher.getSoundPitch());
            vouchersConfig.set(prefix + "particles.particle", voucher.getParticle());
            vouchersConfig.set(prefix + "particles.amount", voucher.getParticleAmount());
            vouchersConfig.set(prefix + "effects.effect", voucher.getEffect());
            vouchersConfig.set(prefix + "effects.amplifier", voucher.getEffectAmplifier());
            vouchersConfig.set(prefix + "itemstack", voucher.getItemStack());
        }
        vouchersConfig.saveChanges();
    }

    @Override
    public void onConfigReload() {
        vouchersConfig.load();
        loadVouchersFromFile();
        this.setLocale(getConfig().getString("System.Language Mode"), true);
        this.locale.reloadMessages();
    }

    @Override
    public List<Config> getExtraConfig() {
        return Collections.singletonList(vouchersConfig);
    }

    public Connections getConnections() {
        return this.connections;
    }

    public CoolDownManager getCoolDowns() {
        return this.coolDowns;
    }

    public VoucherExecutor getVoucherExecutor() {
        return this.voucherExecutor;
    }

    public Config getVouchersConfig() {
        return this.vouchersConfig;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public VoucherManager getVoucherManager() {
        return voucherManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }
}