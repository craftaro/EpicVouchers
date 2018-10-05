package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Voucher {

    private final String key;
    private String permission;
    private Material material = Material.COOKIE;
    private short data = 0;
    private int cooldown = 0;
    private String name;
    private List<String> lore = new ArrayList();
    private boolean glow = true;
    private boolean confirm = true;
    private boolean unbreakable = true;
    private boolean hideAttributes = false;
    private boolean removeItem = true;
    private boolean feedPlayer = true;
    private boolean healPlayer = true;
    private boolean smiteEffect = true;

    private List<String> broadcasts = new ArrayList();
    private List<String> messages = new ArrayList<>();
    private List<String> commands = new ArrayList<>();

    private String actionBar;

    private String title = "&6Thank you.";
    private String subTitle = "&eYou have redeemed the voucher %voucher%.";
    private int titleFadeIn = 10;
    private int titleStay = 50;
    private int titleFadeOut = 10;

    private String sound = "NOTE_PLING";
    private int soundPitch = 1;

    private String particle = "FLAME";
    private int particleAmount = 100;

    private String effect = "SPEED";
    private int effectAmplifer = 2;
    private int effectDuration = 10;

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
            meta.setLore(getLore());
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

    public String getKey() {
        return key;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public short getData() {
        return data;
    }

    public void setData(short data) {
        this.data = data;
    }

    public String getName() {
        return Methods.formatText(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        List<String> itemLore = new ArrayList<>();
        for (String line : lore) {
            itemLore.add(Methods.formatText(line));
        }
        return itemLore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public boolean isHideAttributes() {
        return hideAttributes;
    }

    public void setHideAttributes(boolean hideAttributes) {
        this.hideAttributes = hideAttributes;
    }

    public boolean isRemoveItem() {
        return removeItem;
    }

    public void setRemoveItem(boolean removeItem) {
        this.removeItem = removeItem;
    }

    public boolean isFeedPlayer() {
        return feedPlayer;
    }

    public void setFeedPlayer(boolean feedPlayer) {
        this.feedPlayer = feedPlayer;
    }

    public boolean isHealPlayer() {
        return healPlayer;
    }

    public void setHealPlayer(boolean healPlayer) {
        this.healPlayer = healPlayer;
    }

    public boolean isSmiteEffect() {
        return smiteEffect;
    }

    public void setSmiteEffect(boolean smiteEffect) {
        this.smiteEffect = smiteEffect;
    }

    public List<String> getBroadcasts(boolean applyFormatting) {
        if (!applyFormatting) return broadcasts;
        List<String> itemBroadcasts = new ArrayList<>();
        for (String line : broadcasts) {
            itemBroadcasts.add(Methods.formatText(line));
        }
        return itemBroadcasts;
    }

    public void setBroadcasts(List<String> broadcasts) {
        this.broadcasts = broadcasts;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public List<String> getMessages(boolean applyFormatting) {
        if (!applyFormatting) return lore;
        List<String> itemMessages = new ArrayList<>();
        for (String line : lore) {
            itemMessages.add(Methods.formatText(line));
        }
        return itemMessages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public String getActionBar() {
        return Methods.formatText(actionBar);
    }

    public void setActionBar(String actionBar) {
        this.actionBar = actionBar;
    }

    public String getTitle() {
        return Methods.formatText(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return Methods.formatText(subTitle);
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getTitleFadeIn() {
        return titleFadeIn;
    }

    public void setTitleFadeIn(int titleFadeIn) {
        this.titleFadeIn = titleFadeIn;
    }

    public int getTitleStay() {
        return titleStay;
    }

    public void setTitleStay(int titleStay) {
        this.titleStay = titleStay;
    }

    public int getTitleFadeOut() {
        return titleFadeOut;
    }

    public void setTitleFadeOut(int titleFadeOut) {
        this.titleFadeOut = titleFadeOut;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getSoundPitch() {
        return soundPitch;
    }

    public void setSoundPitch(int soundPitch) {
        this.soundPitch = soundPitch;
    }

    public String getParticle() {
        return particle;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public int getParticleAmount() {
        return particleAmount;
    }

    public void setParticleAmount(int particleAmount) {
        this.particleAmount = particleAmount;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getEffectAmplifer() {
        return effectAmplifer;
    }

    public void setEffectAmplifer(int effectAmplifer) {
        this.effectAmplifer = effectAmplifer;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public void setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    @Override
    public String toString() {
        return key;
    }

}
