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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class EpicVouchers extends SongodaPlugin {

    private static EpicVouchers INSTANCE;

    private final GuiManager guiManager = new GuiManager(this);
    private Connections connections;
    private CoolDownManager coolDowns;
    private VoucherExecutor voucherExecutor;
    private CommandManager commandManager;
    private Config vouchersConfig = new Config(this, "vouchers.yml");
    private LinkedHashMap<String, Voucher> vouchers;

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

        this.vouchers = new LinkedHashMap<>();

        this.connections = new Connections(this);
        this.coolDowns = new CoolDownManager(this);
        this.voucherExecutor = new VoucherExecutor(this);

        PluginManager manager = Bukkit.getServer().getPluginManager();

        // Listeners
        guiManager.init();
        manager.registerEvents(new PlayerInteractListener(this), this);
        manager.registerEvents(new PlayerCommandListener(), this);

        File folder = getDataFolder();
        File voucherFile = new File(folder, "vouchers.yml");

        if (!voucherFile.exists()) {
            saveResource("vouchers.yml", true);
        }

        loadVouchersFromFile();

        connections.openMySQL();
    }

    private void loadVouchersFromFile() {
        vouchers.clear();
        vouchersConfig.load();

        if (vouchersConfig.contains("vouchers")) {
            for (String key : vouchersConfig.getConfigurationSection("vouchers").getKeys(false)) {
                key = key.toLowerCase();
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
                voucher.setCoolDown(cs.getInt("coolDown", 0));
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

    public LinkedHashMap<String, Voucher> getVouchers() {
        return this.vouchers;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }
}