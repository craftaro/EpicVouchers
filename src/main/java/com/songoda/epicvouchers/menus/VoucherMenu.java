package com.songoda.epicvouchers.menus;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.AbstractAnvilGUI;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.IconInv;
import com.songoda.epicvouchers.libraries.inventory.icons.Icon;
import com.songoda.epicvouchers.utils.ServerVersion;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static com.songoda.epicvouchers.libraries.AbstractAnvilGUI.AnvilSlot.INPUT_LEFT;
import static com.songoda.epicvouchers.utils.Methods.format;
import static org.bukkit.Material.PAPER;

public class VoucherMenu extends IconInv {

    public VoucherMenu(EpicVouchers instance) {
        super((int) (Math.ceil(instance.getVouchers().size() / 9.0)  * 9 + (instance.getVouchers().size() % 9 == 0 ? 9 : 0)), "Vouchers");

        for (Voucher voucher : instance.getVouchers().values()) {
            if (getInventory().firstEmpty() != -1) {
                addIcon(getInventory().firstEmpty(), voucher.toItemStack(), event -> new OptionMenu(instance, voucher).open(event.getPlayer()));
            }
        }

        addIcon(getInventory().getSize() - 1, new Icon(new ItemBuilder(PAPER)
                .name(format("&6New voucher"))
                .lore(format("&eCreate a new voucher with set id.")).build(), event -> {
            AbstractAnvilGUI anvilGUI = new AbstractAnvilGUI(instance, event.getPlayer(), anvilEvent -> {
                if (instance.getVouchers().containsKey(anvilEvent.getName())) {
                    event.getPlayer().sendMessage(format("&cAlready a voucher registered with the id: " + anvilEvent.getName()));
                    new VoucherMenu(instance).open(event.getPlayer());
                    return;
                }

                Voucher voucher = new Voucher(anvilEvent.getName(), instance);
                voucher.setMaterial(PAPER);
                voucher.setName("&f" + anvilEvent.getName());

                instance.getVouchers().put(anvilEvent.getName(), voucher);
                instance.getVouchersFile().getConfig().set("vouchers." + anvilEvent.getName() + ".material", voucher.getMaterial().toString());
                instance.getVouchersFile().getConfig().set("vouchers." + anvilEvent.getName() + ".name", voucher.getName(false));
                instance.getVouchersFile().saveConfig();
                event.getPlayer().sendMessage(format("&7Successfully created voucher with id &r{id}&7.", "{id}", anvilEvent.getName()));
                new VoucherMenu(instance).open(event.getPlayer());
            });
            anvilGUI.setSlot(INPUT_LEFT, new ItemBuilder(PAPER).name("Insert id").build());
            anvilGUI.open();
        }));

        fill(new Icon(new ItemBuilder(instance.getServerVersion().isServerVersionAtLeast(ServerVersion.V1_13) ?
                new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7)).name(ChatColor.RESET.toString()).build()));
    }

}
