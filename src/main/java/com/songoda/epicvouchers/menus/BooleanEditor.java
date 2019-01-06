package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.FastInv;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.Material;

import java.util.Collections;

import static org.bukkit.ChatColor.*;
import static org.bukkit.enchantments.Enchantment.DURABILITY;
import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;

public class BooleanEditor extends FastInv {
    public BooleanEditor(EpicVouchers instance, Voucher voucher) {
        super(9, instance.getLocale().getMessage("interface.boolean.title"));

        addItem(0, new ItemBuilder(Material.BARRIER)
                .name(instance.getLocale().getMessage("interface.editvoucher.backtitle"))
                .lore(Collections.singletonList(instance.getLocale().getMessage("interface.editvoucher.backlore")))
                .enchant(DURABILITY, 1)
                .addFlags(HIDE_ENCHANTS)
                .build(), event -> new EditorMenu(instance, voucher).open(event.getPlayer()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Glow").lore(
                voucher.isGlow() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setGlow(!voucher.isGlow()).saveSetting("glow", voucher.isGlow()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Confirm").lore(
                voucher.isConfirm() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setConfirm(!voucher.isConfirm()).saveSetting("confirm", voucher.isConfirm()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Unbreakable").lore(
                voucher.isUnbreakable() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setUnbreakable(!voucher.isUnbreakable()).saveSetting("unbreakable", voucher.isUnbreakable()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Hide Attributes").lore(
                voucher.isHideAttributes() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setHideAttributes(!voucher.isHideAttributes()).saveSetting("hide-attributes", voucher.isHideAttributes()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Remove Item").lore(
                voucher.isRemoveItem() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setRemoveItem(!voucher.isRemoveItem()).saveSetting("remove-item", voucher.isRemoveItem()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Feed Player").lore(
                voucher.isFeedPlayer() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setFeedPlayer(!voucher.isFeedPlayer()).saveSetting("feed-player", voucher.isFeedPlayer()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Heal Player").lore(
                voucher.isHealPlayer() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setHealPlayer(!voucher.isHealPlayer()).saveSetting("heal-player", voucher.isHealPlayer()));

        addItem(new ItemBuilder(Material.PAPER).name(YELLOW + "Smite Effect").lore(
                voucher.isSmiteEffect() ? GREEN + "ENABLED" : RED + "DISABLED"
        ).build(), event -> voucher.setSmiteEffect(!voucher.isSmiteEffect()).saveSetting("smite-effect", voucher.isSmiteEffect()));


        onClick(event -> {
            if(event.getItem() != null && event.getSlot() != 0) {
                new BooleanEditor(instance, voucher).open(event.getPlayer());
            }
        });
    }
}
