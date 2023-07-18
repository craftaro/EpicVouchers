package com.craftaro.epicvouchers.menus.sub.editor;

import com.craftaro.core.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.libraries.inventory.FastInv;
import com.craftaro.epicvouchers.menus.OptionMenu;
import com.craftaro.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.YELLOW;

public class SetItemMenu extends FastInv {
    public SetItemMenu(EpicVouchers instance, Voucher voucher) {
        super(27, "Set item");

        setDefaultCancel(false);

        fill(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())
                .name(ChatColor.RESET.toString()).build(), event -> event.setCancelled(true));

        addItem(13, null);

        addItem(18, new ItemBuilder(Material.BARRIER)
                .name(YELLOW + "Return")
                .lore(GRAY + "Return to the editor")
                .addGlow().build(), event -> {
            event.setCancelled(true);
            new OptionMenu(instance, voucher).open(event.getPlayer());
        });

        addItem(26, new ItemBuilder(Material.ANVIL)
                .name(ChatColor.GREEN + "Confirm")
                .lore(GRAY + "Left click to save without NBT", GRAY + "Right click to save with NBT")
                .build(), event -> {
            event.setCancelled(true);
            if (event.getInventory().getInventory().getItem(13) == null) {
                new OptionMenu(instance, voucher).open(event.getPlayer());
                return;
            }

            ItemStack itemStack = event.getInventory().getInventory().getItem(13);

            if (event.getClickType() == ClickType.RIGHT) {
                new OptionMenu(instance, voucher).open(event.getPlayer());
                voucher.setName("");
                voucher.setLore(null);
                voucher.setItemStack(itemStack);
            }

            voucher.setMaterial(itemStack.getType());

            if (itemStack.getDurability() != 0) {
                voucher.setData(itemStack.getDurability());
            }

            if (!itemStack.hasItemMeta()) {
                new OptionMenu(instance, voucher).open(event.getPlayer());
                return;
            }

            if (itemStack.getItemMeta().hasDisplayName()) {
                voucher.setName(itemStack.getItemMeta().getDisplayName());
            }

            if (itemStack.getItemMeta().hasLore()) {
                voucher.setLore(itemStack.getItemMeta().getLore());
            }

            new OptionMenu(instance, voucher).open(event.getPlayer());
        });
    }
}
