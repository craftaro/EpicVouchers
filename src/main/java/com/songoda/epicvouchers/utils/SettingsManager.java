package com.songoda.epicvouchers.utils;

import com.songoda.epicvouchers.EpicVouchers;
import org.bukkit.event.Listener;

/**
 * Created by songo on 6/4/2017.
 */
public class SettingsManager implements Listener {

    private final EpicVouchers instance;

    public SettingsManager(EpicVouchers instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    public void updateSettings() {
        for (Settings s : Settings.values()) {
            instance.getConfig().addDefault(s.setting, s.option);
        }
    }

    public enum Settings {

        CONFIRM_FILL_GLASS("Interface.Fill Interfaces With Glass", true),
        COOLDOWN_DELAY("Main.Cooldown Delay", 10),

        DATABASE_SUPPORT("Database.Activate Mysql Support", false),
        DATABASE_IP("Database.IP", "127.0.0.1"),
        DATABASE_PORT("Database.Port", 3306),
        DATABASE_NAME("Database.Database Name", "EpicVouchers"),
        DATABASE_USERNAME("Database.Username", "PUT_USERNAME_HERE"),
        DATABASE_PASSWORD("Database.Password", "PUT_PASSWORD_HERE"),

        LANGUGE_MODE("System.Language Mode", "en_US"),
        DEBUGGER_ENABLED("System.Debugger Enabled", false);

        private final String setting;
        private final Object option;

        Settings(String setting, Object option) {
            this.setting = setting;
            this.option = option;
        }

    }
}