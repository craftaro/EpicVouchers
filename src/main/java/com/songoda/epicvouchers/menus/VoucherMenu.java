package com.songoda.epicvouchers.menus;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.gui.AnvilGui;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.libraries.ItemBuilder;
import com.songoda.epicvouchers.libraries.inventory.IconInv;
import com.songoda.epicvouchers.libraries.inventory.icons.Icon;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.PAPER;

public class VoucherMenu extends IconInv {

    public VoucherMenu(EpicVouchers instance) {
        super((int) (Math.ceil(instance.getVoucherManager().getVouchers().size() / 9.0) * 9 + (instance.getVoucherManager().getVouchers().size() % 9 == 0 ? 9 : 0)), "Vouchers");

        for (Voucher voucher : instance.getVoucherManager().getVouchers()) {
            if (getInventory().firstEmpty() != -1) {
                addIcon(getInventory().firstEmpty(), voucher.toItemStack(), event -> new OptionMenu(instance, voucher).open(event.getPlayer()));
            }
        }

        addIcon(getInventory().getSize() - 1, new Icon(new ItemBuilder(PAPER)
                .name(TextUtils.formatText("&6New voucher"))
                .lore(TextUtils.formatText("&eCreate a new voucher with set id.")).build(), event -> {
            AnvilGui gui = new AnvilGui(event.getPlayer());
            gui.setTitle("Insert id");
            gui.setAction(aevent -> {
                final String msg = gui.getInputText().trim();
                if (instance.getVoucherManager().getVoucher(msg) != null) {
                    event.getPlayer().sendMessage(TextUtils.formatText("&cAlready a voucher registered with the id: " + msg));
                    new VoucherMenu(instance).open(event.getPlayer());
                    return;
                }

                Voucher voucher = new Voucher(msg, instance);
                voucher.setMaterial(PAPER);
                voucher.setName("&f" + msg);

                instance.getVoucherManager().addVoucher(voucher);
                event.getPlayer().sendMessage(TextUtils.formatText("&7Successfully created voucher with id &r" + msg + "&7."));
                new VoucherMenu(instance).open(event.getPlayer());
            });
            instance.getGuiManager().showGUI(event.getPlayer(), gui);
        }));

        fill(new Icon(new ItemBuilder(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ?
                new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE")) :
                new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7)).name(ChatColor.RESET.toString()).build()));
    }

}
