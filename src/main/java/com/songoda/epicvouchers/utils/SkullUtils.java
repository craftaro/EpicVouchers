package com.songoda.epicvouchers.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.songoda.core.compatibility.ServerVersion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class SkullUtils {


    /*
     * Custom skull texture. Should probably be moved to SongodaCore.
     */
    public static ItemStack customTexture(ItemStack itemStack, String texture) {
        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_8)) {
            return itemStack;
        }

        if (texture == null || texture.isEmpty()) {
            return itemStack;
        }

        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), "CustomHead");
        if (texture.endsWith("=")) {
            gameProfile.getProperties().put("textures", new Property("texture", texture.replaceAll("=", "")));
        } else {
            byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", texture).getBytes());
            gameProfile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        }

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
            itemStack.setItemMeta(skullMeta);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        return itemStack;
    }
}
