package com.craftaro.epicvouchers;

import com.craftaro.core.SongodaCore;
import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.commands.CommandManager;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.dependency.Dependency;
import com.craftaro.core.gui.GuiManager;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicvouchers.commands.CommandEditor;
import com.craftaro.epicvouchers.commands.CommandEpicVouchers;
import com.craftaro.epicvouchers.commands.CommandForce;
import com.craftaro.epicvouchers.commands.CommandForceAll;
import com.craftaro.epicvouchers.commands.CommandGive;
import com.craftaro.epicvouchers.commands.CommandGiveAll;
import com.craftaro.epicvouchers.commands.CommandList;
import com.craftaro.epicvouchers.commands.CommandReload;
import com.craftaro.epicvouchers.handlers.Connections;
import com.craftaro.epicvouchers.libraries.inventory.FastInv;
import com.craftaro.epicvouchers.libraries.inventory.IconInv;
import com.craftaro.epicvouchers.listeners.PlayerCommandListener;
import com.craftaro.epicvouchers.listeners.PlayerInteractListener;
import com.craftaro.epicvouchers.settings.Settings;
import com.craftaro.epicvouchers.utils.Callback;
import com.craftaro.epicvouchers.utils.ThreadSync;
import com.craftaro.epicvouchers.voucher.CoolDownManager;
import com.craftaro.epicvouchers.voucher.Voucher;
import com.craftaro.epicvouchers.voucher.VoucherExecutor;
import com.craftaro.epicvouchers.voucher.VoucherManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EpicVouchers extends SongodaPlugin {
    private final GuiManager guiManager = new GuiManager(this);
    private CommandManager commandManager;
    private VoucherManager voucherManager;

    private Connections connections;
    private CoolDownManager coolDowns;
    private VoucherExecutor voucherExecutor;
    private final Config vouchersConfig = new Config(this, "vouchers.yml");

    /**
     * @deprecated Use {@link org.bukkit.plugin.java.JavaPlugin#getPlugin(Class)} instead
     */
    @Deprecated
    public static EpicVouchers getInstance() {
        return getPlugin(EpicVouchers.class);
    }

    @Override
    protected Set<Dependency> getDependencies() {
        return new HashSet<>();
    }

    @Override
    public void onPluginLoad() {
    }

    @Override
    public void onPluginDisable() {
        this.connections.closeMySQL();
        saveVouchers();
    }

    @Override
    public void onPluginEnable() {
        // Run Songoda Updater
        SongodaCore.registerPlugin(this, 25, XMaterial.EMERALD);

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

        this.connections = new Connections(this);
        this.coolDowns = new CoolDownManager(this);
        this.voucherExecutor = new VoucherExecutor(this);
        this.voucherManager = new VoucherManager();

        PluginManager manager = Bukkit.getServer().getPluginManager();

        // Listeners
        this.guiManager.init();
        manager.registerEvents(new PlayerInteractListener(this), this);
        manager.registerEvents(new PlayerCommandListener(), this);
    }

    @Override
    public void onDataLoad() {
        if (!new File(this.getDataFolder(), "vouchers.yml").exists()) {
            saveResource("vouchers.yml", false);
        }

        synchronized (this.vouchersConfig) {
            this.vouchersConfig.load();
        }

        loadVouchersFromFile();

        this.connections.openMySQL();

        // FIXME: Config system needs to be greatly redone and only write changes when changes were made - Maybe even split it into multiple smaler files
        //        Issue https://support.songoda.com/browse/SD-8155 has been hotfixed by writing changes to the file async and blocking the main thread when needed. This requires the use of `synchronized`
        //        and expects every modifying code to use it (thread-safety)
        //        Large vouchers.yml files cause huge performance problems otherwise...
        //        Example file for testing: https://support.songoda.com/secure/attachment/17258/17258_vouchers.yml
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,
                () -> saveVouchersAsync(ex -> {
                    if (ex != null) {
                        ex.printStackTrace();
                    }
                }), 5 * 60 * 20, 5 * 60 * 20);   // 5 minutes
    }

    private void loadVouchersFromFile() {
        synchronized (this.vouchersConfig) {
            this.voucherManager.clearVouchers();

            if (this.vouchersConfig.contains("vouchers")) {
                for (String key : this.vouchersConfig.getConfigurationSection("vouchers").getKeys(false)) {
                    Voucher voucher = new Voucher(key, this);
                    ConfigurationSection cs = this.vouchersConfig.getConfigurationSection("vouchers." + key);

                    Material material;
                    String stringMaterial = cs.getString("material");

                    if (stringMaterial == null || stringMaterial.isEmpty()) {
                        material = Material.PAPER;
                    } else {
                        material = Material.matchMaterial(stringMaterial);
                        if (material == null) {
                            material = Material.PAPER;
                        }
                    }

                    voucher.setPermission(cs.getString("permission", ""))
                            .setMaterial(material)
                            .setData((short) cs.getInt("data", 0))
                            .setName(cs.getString("name", "default"))
                            .setLore(cs.getStringList("lore"))
                            .setTexture(cs.getString("texture", ""))
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

                    this.voucherManager.addVoucher(voucher);
                }
            }
        }
    }

    private void saveVouchers() {
        ThreadSync tSync = new ThreadSync();

        saveVouchersAsync(ex -> {
            if (ex != null) {
                ex.printStackTrace();
            }

            tSync.release();
        });

        tSync.waitForRelease();
    }

    private void saveVouchersAsync(Callback callback) {
        new Thread(() -> {
            try {
                synchronized (this.vouchersConfig) {
                    Collection<Voucher> voucherList = this.voucherManager.getVouchers();

                    ConfigurationSection cfgSec = this.vouchersConfig.getConfigurationSection("vouchers");
                    if (cfgSec != null) {
                        for (String voucherName : cfgSec.getKeys(false)) {
                            if (voucherList.stream().noneMatch(voucher -> voucher.getKey().equals(voucherName))) {
                                this.vouchersConfig.set("vouchers." + voucherName, null);
                            }
                        }
                    }

                    for (Voucher voucher : voucherList) {
                        String prefix = "vouchers." + voucher.getKey() + ".";

                        this.vouchersConfig.set(prefix + "permission", voucher.getPermission());
                        this.vouchersConfig.set(prefix + "material", voucher.getMaterial().name());
                        this.vouchersConfig.set(prefix + "data", voucher.getData());
                        this.vouchersConfig.set(prefix + "name", voucher.getName());
                        this.vouchersConfig.set(prefix + "lore", voucher.getLore());
                        this.vouchersConfig.set(prefix + "texture", voucher.getTexture());
                        this.vouchersConfig.set(prefix + "glow", voucher.isGlow());
                        this.vouchersConfig.set(prefix + "confirm", voucher.isConfirm());
                        this.vouchersConfig.set(prefix + "unbreakable", voucher.isUnbreakable());
                        this.vouchersConfig.set(prefix + "hide-attributes", voucher.isHideAttributes());
                        this.vouchersConfig.set(prefix + "remove-item", voucher.isRemoveItem());
                        this.vouchersConfig.set(prefix + "heal-player", voucher.isHealPlayer());
                        this.vouchersConfig.set(prefix + "smite-effect", voucher.isSmiteEffect());
                        this.vouchersConfig.set(prefix + "coolDown", voucher.getCoolDown());
                        this.vouchersConfig.set(prefix + "broadcasts", voucher.getBroadcasts());
                        this.vouchersConfig.set(prefix + "messages", voucher.getMessages());
                        this.vouchersConfig.set(prefix + "commands", voucher.getCommands());
                        this.vouchersConfig.set(prefix + "actionbar", voucher.getActionBar());
                        this.vouchersConfig.set(prefix + "titles.title", voucher.getTitle());
                        this.vouchersConfig.set(prefix + "titles.subtitle", voucher.getSubTitle());
                        this.vouchersConfig.set(prefix + "titles.fade-in", voucher.getTitleFadeIn());
                        this.vouchersConfig.set(prefix + "titles.stay", voucher.getTitleStay());
                        this.vouchersConfig.set(prefix + "titles.fade-out", voucher.getTitleFadeOut());
                        this.vouchersConfig.set(prefix + "sounds.sound", voucher.getSound());
                        this.vouchersConfig.set(prefix + "sounds.pitch", voucher.getSoundPitch());
                        this.vouchersConfig.set(prefix + "particles.particle", voucher.getParticle());
                        this.vouchersConfig.set(prefix + "particles.amount", voucher.getParticleAmount());
                        this.vouchersConfig.set(prefix + "effects.effect", voucher.getEffect());
                        this.vouchersConfig.set(prefix + "effects.amplifier", voucher.getEffectAmplifier());
                        this.vouchersConfig.set(prefix + "itemstack", voucher.getItemStack());
                    }

                    this.vouchersConfig.saveChanges();

                    callback.accept(null);
                }
            } catch (Exception ex) {
                callback.accept(ex);
            }
        }, getName() + "-AsyncConfigSave").start();
    }

    @Override
    public void onConfigReload() {
        synchronized (this.vouchersConfig) {
            this.vouchersConfig.load();
        }

        loadVouchersFromFile();

        this.setLocale(getConfig().getString("System.Language Mode"), true);
        this.locale.reloadMessages();
    }

    @Override
    public List<Config> getExtraConfig() {
        return Collections.singletonList(this.vouchersConfig);
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

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public VoucherManager getVoucherManager() {
        return this.voucherManager;
    }

    public GuiManager getGuiManager() {
        return this.guiManager;
    }
}
