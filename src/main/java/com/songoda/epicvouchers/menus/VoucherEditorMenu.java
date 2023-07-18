package com.songoda.epicvouchers.menus;

import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.core.utils.ItemUtils;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.IconInv;
import com.songoda.epicvouchers.libraries.inventory.icons.Icon;
import com.songoda.epicvouchers.libraries.inventory.icons.StringIcon;
import com.songoda.epicvouchers.libraries.inventory.icons.StringListIcon;
import com.songoda.epicvouchers.libraries.inventory.icons.ToggleableIcon;
import com.songoda.epicvouchers.menus.sub.editor.EffectsMenu;
import com.songoda.epicvouchers.menus.sub.editor.ParticlesMenu;
import com.songoda.epicvouchers.menus.sub.editor.SoundsMenu;
import com.songoda.epicvouchers.menus.sub.editor.TitlesMenu;
import com.songoda.epicvouchers.voucher.Voucher;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;
import static org.bukkit.Material.BOOK;

public class VoucherEditorMenu extends IconInv {
    private final EpicVouchers instance;
    private final Voucher voucher;

    public VoucherEditorMenu(EpicVouchers instance, Voucher voucher) {
        super(36, "Voucher options");
        this.instance = instance;
        this.voucher = voucher;

        //Booleans

        addIcon(0, new ToggleableIcon("Glow", event -> voucher.setGlow(!voucher.isGlow()), voucher.isGlow()));

        addIcon(1, new ToggleableIcon("Confirm", event -> voucher.setConfirm(!voucher.isConfirm()), voucher.isConfirm()));

        addIcon(2, new ToggleableIcon("Unbreakable", event -> voucher.setUnbreakable(!voucher.isUnbreakable()), voucher.isUnbreakable()));

        addIcon(3, new ToggleableIcon("Hide Attributes", event -> voucher.setHideAttributes(!voucher.isHideAttributes()), voucher.isHideAttributes()));

        addIcon(4, new ToggleableIcon("Remove Item", event -> voucher.setRemoveItem(!voucher.isRemoveItem()), voucher.isRemoveItem()));

        addIcon(5, new ToggleableIcon("Feed Player", event -> voucher.setFeedPlayer(!voucher.isFeedPlayer()), voucher.isFeedPlayer()));

        addIcon(6, new ToggleableIcon("Heal Player", event -> voucher.setHealPlayer(!voucher.isHealPlayer()), voucher.isHealPlayer()));

        addIcon(7, new ToggleableIcon("Smite Effect", event -> voucher.setSmiteEffect(!voucher.isSmiteEffect()), voucher.isSmiteEffect()));

        //Strings

        addIcon(9, new StringIcon(instance, "Permission", voucher.getPermission(), (player, editString) -> {
            voucher.setPermission(editString);
            reopen(player);
        }));

        addIcon(10, new StringIcon(instance, "Actionbar", voucher.getActionBar(), (player, editString) -> {
            voucher.setActionBar(editString);
            reopen(player);
        }));

        addIcon(11, new StringIcon(instance, "Material", voucher.getMaterial().toString(), (player, editString) -> {
            if (editString.contains(":")) {
                voucher.setData(Short.parseShort(editString.split(":")[1]));
                voucher.setMaterial(Material.valueOf(editString.split(":")[0]));
            } else {
                voucher.setMaterial(Material.valueOf(editString));
            }

            reopen(player);
        }, string -> {
            if (string.isEmpty()) {
                return false;
            }

            if (string.contains(":") && string.split(":").length == 2) {
                String[] split = string.split(":");
                return Material.matchMaterial(split[0]) != null && StringUtils.isNumeric(split[1]);
            }

            return Material.matchMaterial(string) != null;
        }, true));

        addIcon(12, new StringIcon(instance, "Name", voucher.getName(false), (player, editString) -> {
            voucher.setName(editString);
            reopen(player);
        }));

        addIcon(13, new StringIcon(instance, new ItemBuilder(voucher.getTexture() == null ? XMaterial.PLAYER_HEAD.parseItem() : ItemUtils.getCustomHead(voucher.getTexture()))
                .name(YELLOW + "Skull Texture")
                .lore(GRAY + "Right click to edit", GRAY + "Left click to clear").build(), voucher.getTexture(), (player, editString) -> {
            voucher.setTexture(editString);
            reopen(player);
        }));

        // Sections

        addIcon(18, new StringListIcon(instance, voucher.getCommands(), "Commands", voucher));

        addIcon(19, new StringListIcon(instance, voucher.getBroadcasts(false), "Broadcasts", voucher));

        addIcon(20, new StringListIcon(instance, voucher.getMessages(false), "Messages", voucher));

        addIcon(21, new StringListIcon(instance, voucher.getLore(false), "Lore", voucher));

        addIcon(22, new Icon(new ItemBuilder(BOOK).name(YELLOW + "Titles").lore(GRAY + "Click to view").build(), event -> new TitlesMenu(instance, voucher).open(event.getPlayer())));

        addIcon(23, new Icon(new ItemBuilder(BOOK).name(YELLOW + "Sounds").lore(GRAY + "Click to view").build(), event -> new SoundsMenu(instance, voucher).open(event.getPlayer())));

        addIcon(24, new Icon(new ItemBuilder(BOOK).name(YELLOW + "Particles").lore(GRAY + "Click to view").build(), event -> new ParticlesMenu(instance, voucher).open(event.getPlayer())));

        addIcon(25, new Icon(new ItemBuilder(BOOK).name(YELLOW + "Effects").lore(GRAY + "Click to view").build(), event -> new EffectsMenu(instance, voucher).open(event.getPlayer())));

        // Misc

        addIcon(getInventory().getSize() - 5, voucher.toItemStack());

        addIcon(getInventory().getSize() - 9, new ItemBuilder(Material.BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .build(), event -> new OptionMenu(instance, voucher).open(event.getPlayer()));

        onClick(event -> {
            if (event.getItem() != null && event.getClickType() != ClickType.RIGHT && event.getSlot() < 18) {
                reopen(event.getPlayer());
            }
        });
    }

    private void reopen(Player player) {
        new VoucherEditorMenu(this.instance, this.voucher).open(player);
    }
}
