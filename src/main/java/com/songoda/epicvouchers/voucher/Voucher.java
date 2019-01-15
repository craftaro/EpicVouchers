package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.utils.Methods;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.songoda.epicvouchers.utils.Methods.format;
import static org.bukkit.Material.PAPER;

@Accessors(chain = true)
public class Voucher {

    @Getter private final String key;
    @Getter @Setter private String permission = "";
    @Getter @Setter private Material material = PAPER;
    @Getter @Setter private short data = 0;
    @Getter @Setter private int cooldown = 0;
    @Setter private String name;
    @Setter private List<String> lore = new ArrayList<>();
    @Getter @Setter private boolean glow = true;
    @Getter @Setter private boolean confirm = true;
    @Getter @Setter private boolean unbreakable = false;
    @Getter @Setter private boolean hideAttributes = false;
    @Getter @Setter private boolean removeItem = true;
    @Getter @Setter private boolean feedPlayer = false;
    @Getter @Setter private boolean healPlayer = false;
    @Getter @Setter private boolean smiteEffect = false;

    @Setter private List<String> broadcasts = new ArrayList<>();
    @Setter private List<String> messages = new ArrayList<>();
    @Setter @Getter private List<String> commands = new ArrayList<>();

    @Setter private String actionBar;

    @Setter private String title = "";
    @Setter private String subTitle = "";
    @Getter @Setter private int titleFadeIn = 0;
    @Getter @Setter private int titleStay = 0;
    @Getter @Setter private int titleFadeOut = 0;

    @Getter @Setter private String sound = "";
    @Getter @Setter private int soundPitch = 0;

    @Getter @Setter private String particle = "";
    @Getter @Setter private int particleAmount = 0;

    @Getter @Setter private String effect = "";
    @Getter @Setter private int effectAmplifier = 0;
    @Getter @Setter private int effectDuration = 0;

    @Getter @Setter private ItemStack itemStack;

    public Voucher(String key) {
        this.key = key;
    }

    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    public ItemStack toItemStack(int amount) {
        ItemStack item = itemStack == null ? new ItemStack(material, amount, data) : itemStack;
        ItemMeta meta = item.getItemMeta();

        if(meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(material);
        }

        if(!name.isEmpty()) {
            meta.setDisplayName(format(name));
        }

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
        return applyFormatting ? format(name) : name;
    }

    public List<String> getLore(boolean applyFormatting) {
        return applyFormatting ? lore.stream().map(Methods::format).collect(Collectors.toList()) : lore;
    }

    public List<String> getBroadcasts(boolean applyFormatting) {
        return applyFormatting ? broadcasts.stream().map(Methods::format).collect(Collectors.toList()) : broadcasts;
    }

    public List<String> getMessages(boolean applyFormatting) {
        return applyFormatting ? messages.stream().map(Methods::format).collect(Collectors.toList()) : messages;
    }

    public void saveSetting(String key, Object value) {
        ConfigurationSection cs = EpicVouchers.getInstance().getVouchersFile().getConfig().getConfigurationSection("vouchers." + getKey());
        cs.set(key, value);
        EpicVouchers.getInstance().getVouchersFile().saveConfig();
    }

    @Override
    public String toString() {
        return key;
    }

    public String getActionBar() {
        return format(actionBar);
    }

    public String getSubTitle() {
        return format(subTitle);
    }

    public String getTitle() {
        return format(title);
    }

}
