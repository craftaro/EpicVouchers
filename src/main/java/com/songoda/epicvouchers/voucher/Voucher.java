package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.utils.Methods;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Voucher {

    @Getter private final String key;
    @Getter @Setter private String permission;
    @Getter @Setter private Material material = Material.COOKIE;
    @Getter @Setter private short data = 0;
    @Getter @Setter private int cooldown = 0;
    @Setter private String name;
    @Setter private List<String> lore = new ArrayList<>();
    @Getter @Setter private boolean glow = true;
    @Getter @Setter private boolean confirm = true;
    @Getter @Setter private boolean unbreakable = true;
    @Getter @Setter private boolean hideAttributes = false;
    @Getter @Setter private boolean removeItem = true;
    @Getter @Setter private boolean feedPlayer = true;
    @Getter @Setter private boolean healPlayer = true;
    @Getter @Setter private boolean smiteEffect = true;

    @Setter private List<String> broadcasts = new ArrayList<>();
    @Setter private List<String> messages = new ArrayList<>();
    @Setter @Getter private List<String> commands = new ArrayList<>();

    @Setter private String actionBar;

    @Setter private String title = "&6Thank you.";
    @Setter private String subTitle = "&eYou have redeemed the voucher %voucher%.";
    @Getter @Setter private int titleFadeIn = 10;
    @Getter @Setter private int titleStay = 50;
    @Getter @Setter private int titleFadeOut = 10;

    @Getter @Setter private String sound = "NOTE_PLING";
    @Getter @Setter private int soundPitch = 1;

    @Getter @Setter private String particle = "FLAME";
    @Getter @Setter private int particleAmount = 100;

    @Getter @Setter private String effect = "SPEED";
    @Getter @Setter private int effectAmplifer = 2;
    @Getter @Setter private int effectDuration = 10;

    public Voucher(String key) {
        this.key = key;
    }

    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    public ItemStack toItemStack(int amount) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Methods.formatText(name));
        if (lore != null) {
            meta.setLore(getLore(true));
        }
        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (unbreakable) {
            meta.spigot().setUnbreakable(true);
        }
        if (hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        item.setItemMeta(meta);
        return item;
    }

    public String getName(boolean applyFormatting) {
        if (!applyFormatting) return name;
        return Methods.formatText(name);
    }

    public List<String> getLore(boolean applyFormatting) {
        if (!applyFormatting) return lore;
        List<String> itemLore = new ArrayList<>();
        for (String line : lore) {
            itemLore.add(Methods.formatText(line));
        }
        return itemLore;
    }

    public List<String> getBroadcasts(boolean applyFormatting) {
        if (!applyFormatting) return broadcasts;
        List<String> itemBroadcasts = new ArrayList<>();
        for (String line : broadcasts) {
            itemBroadcasts.add(Methods.formatText(line));
        }
        return itemBroadcasts;
    }

    public List<String> getMessages(boolean applyFormatting) {
        if (!applyFormatting) return messages;
        List<String> itemMessages = new ArrayList<>();
        for (String line : messages) {
            itemMessages.add(Methods.formatText(line));
        }
        return itemMessages;
    }

    @Override
    public String toString() {
        return key;
    }

    public String getActionBar() {
        return Methods.formatText(actionBar);
    }

    public String getSubTitle() {
        return Methods.formatText(subTitle);
    }

    public String getTitle() {
        return Methods.formatText(title);
    }

}
