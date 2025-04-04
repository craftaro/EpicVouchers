package com.songoda.epicvouchers.libraries;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A complete {@link ItemStack} inventory for FastInv (only works on 1.8+).
 * <p>
 * The project is on <a href="https://github.com/MrMicky-FR/FastInv">GitHub</a>
 *
 * @author MrMicky
 */
public class ItemBuilder {
    private final ItemStack item;
    private ItemMeta meta;

    /*
     * Constructors:
     */
    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, byte data) {
        this(new ItemStack(material, 1, data));
    }

    public ItemBuilder(Material material, int amount, byte data) {
        this(new ItemStack(material, amount, data));
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    /*
     * Meta:
     */
    public boolean hasMeta() {
        return getMeta() != null;
    }

    public ItemMeta getMeta() {
        return this.meta;
    }

    public ItemBuilder meta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    /*
     * Name:
     */
    public boolean hasName() {
        return this.meta.hasDisplayName();
    }

    public String getName() {
        return this.meta.getDisplayName();
    }

    public ItemBuilder name(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    /*
     * Lore:
     */
    public boolean hasLore() {
        return this.meta.hasLore();
    }

    public List<String> getLore() {
        return this.meta.getLore();
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    /*
     * Enchantments:
     */
    public boolean hasEnchants() {
        return this.meta.hasEnchants();
    }

    public boolean hasEnchant(Enchantment enchantment) {
        return this.meta.hasEnchant(enchantment);
    }

    public boolean hasConflictingEnchant(Enchantment enchantment) {
        return this.meta.hasConflictingEnchant(enchantment);
    }

    public Map<Enchantment, Integer> getEnchants() {
        return this.meta.getEnchants();
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addGlow() {
        return enchant(Enchantment.DURABILITY, 1).addFlags(ItemFlag.HIDE_ENCHANTS);
    }

    /*
     * Flags:
     */
    public boolean hasFlag(ItemFlag flag) {
        return this.meta.hasItemFlag(flag);
    }

    public Set<ItemFlag> getFlags() {
        return this.meta.getItemFlags();
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    /*
     * Unbreakability:
     */
    public boolean isUnbreakable() {
        return this.meta.isUnbreakable();
    }

    public ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    /*
     * ==========================
     *
     * SPECIFIC META
     *
     * ==========================
     */

    /*
     * Banners:
     */
    public DyeColor getBannerBaseColor() {
        return ((BannerMeta) this.meta).getBaseColor();
    }

    public List<Pattern> getBannerPatterns() {
        return ((BannerMeta) this.meta).getPatterns();
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder bannerBaseColor(DyeColor color) {
        ((BannerMeta) this.meta).setBaseColor(color);
        return this;
    }

    public ItemBuilder bannerPatterns(List<Pattern> patterns) {
        ((BannerMeta) this.meta).setPatterns(patterns);
        return this;
    }

    public ItemBuilder bannerPattern(int i, Pattern pattern) {
        ((BannerMeta) this.meta).setPattern(i, pattern);
        return this;
    }

    public ItemBuilder addBannerPatterns(Pattern pattern) {
        ((BannerMeta) this.meta).addPattern(pattern);
        return this;
    }

    public ItemBuilder removeBannerPattern(int i) {
        ((BannerMeta) this.meta).removePattern(i);
        return this;
    }

    /*
     * Leather armor:
     */
    public Color getLeatherArmorColor() {
        return ((LeatherArmorMeta) this.meta).getColor();
    }

    public ItemBuilder leatherArmorColor(Color color) {
        ((LeatherArmorMeta) this.meta).setColor(color);
        return this;
    }

    /*
     * Skulls:
     */
    public boolean hasSkullOwner() {
        return ((SkullMeta) this.meta).hasOwner();
    }

    @SuppressWarnings("deprecation")
    public String getSkullOwner() {
        return ((SkullMeta) this.meta).getOwner();
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder skullOwner(String owner) {
        this.item.setDurability((short) 3);
        ((SkullMeta) this.meta).setOwner(owner);
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.item.setDurability((short) durability);
        return this;
    }

    /*
     * Potions:
     */
    public boolean hasPotionEffect(PotionEffectType type) {
        return ((PotionMeta) this.meta).hasCustomEffect(type);
    }

    public boolean hasPotionEffects() {
        return ((PotionMeta) this.meta).hasCustomEffects();
    }

    public List<PotionEffect> getPotionEffects() {
        return ((PotionMeta) this.meta).getCustomEffects();
    }

    public ItemBuilder addPotionEffect(PotionEffect effect, boolean overwrite) {
        ((PotionMeta) this.meta).addCustomEffect(effect, overwrite);
        return this;
    }

    /*
     * Build the ItemStack.
     */
    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }
}
