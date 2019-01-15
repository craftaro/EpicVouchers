package com.songoda.epicvouchers.utils;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;

public class Methods {

    public static String format(String text) {
        if (text == null || text.equals(""))
            return "";
        return format(text, "", null);
    }

    public static String format(String text, String toReplace, @Nullable Object object) {
        return ChatColor.translateAlternateColorCodes('&', text).replace(toReplace, object == null ? "" : object.toString());
    }

}
