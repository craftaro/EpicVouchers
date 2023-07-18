package com.craftaro.epicvouchers.settings;

import com.craftaro.core.configuration.Config;
import com.craftaro.core.configuration.ConfigSetting;
import com.craftaro.epicvouchers.EpicVouchers;
import org.bukkit.event.Listener;

public class Settings implements Listener {
    static final Config config = EpicVouchers.getPlugin(EpicVouchers.class).getCoreConfig();

    public static final ConfigSetting FILL_GLASS = new ConfigSetting(config, "Interface.Fill Interfaces With Glass", true);
    public static final ConfigSetting COOLDOWN_DELAY = new ConfigSetting(config, "Main.Cooldown Delay", 10);
    public static final ConfigSetting CHECK_FOR_LEGACY_ITEMS = new ConfigSetting(config, "Main.Check For Legacy Items", false,
            "When you have a really old installation of EpicVouchers, some items in chests etc. might still be created with the old system.",
            "This enables checking/detection for those items.",
            "2 warnings: Enabling this comes with a performance impact with many vouchers configured + This check will be removed in the future");

    public static final ConfigSetting DATABASE_SUPPORT = new ConfigSetting(config, "Database.Activate Mysql Support", false);
    public static final ConfigSetting DATABASE_IP = new ConfigSetting(config, "Database.IP", "127.0.0.1");
    public static final ConfigSetting DATABASE_PORT = new ConfigSetting(config, "Database.Port", 3306);
    public static final ConfigSetting DATABASE_NAME = new ConfigSetting(config, "Database.Database Name", "EpicVouchers");
    public static final ConfigSetting DATABASE_USERNAME = new ConfigSetting(config, "Database.Username", "PUT_USERNAME_HERE");
    public static final ConfigSetting DATABASE_PASSWORD = new ConfigSetting(config, "Database.Password", "PUT_PASSWORD_HERE");

    public static final ConfigSetting LANGUGE_MODE = new ConfigSetting(config, "System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    public static void setupConfig() {
        config.load();
        config.setAutoremove(true).setAutosave(true);
        config.saveChanges();
    }
}
