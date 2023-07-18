package com.craftaro.epicvouchers.menus;

import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.core.gui.AnvilGui;
import com.craftaro.core.utils.TextUtils;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.libraries.ItemBuilder;
import com.craftaro.epicvouchers.libraries.inventory.IconInv;
import com.craftaro.epicvouchers.libraries.inventory.icons.Icon;
import com.craftaro.epicvouchers.voucher.Voucher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.PAPER;

public class VoucherMenu extends IconInv {
    public VoucherMenu(EpicVouchers instance) {
        // FIXME: Having too many vouchers throws an error:
        //        Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got 594)
        //        Applied hotfix to use a maximum size of 54
        //        Example file: https://support.songoda.com/secure/attachment/17258/17258_vouchers.yml

        super(Math.min((int) (Math.ceil(instance.getVoucherManager().getVouchers().size() / 9.0) * 9 + (instance.getVoucherManager().getVouchers().size() % 9 == 0 ? 9 : 0)), 54), "Vouchers");

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
            gui.setAction(aEvent -> {
                final String msg = gui.getInputText().trim();
                if (instance.getVoucherManager().getVoucher(msg) != null) {
                    event.getPlayer().sendMessage(TextUtils.formatText("&cAlready a voucher registered with the id: " + msg));
                    new VoucherMenu(instance).open(event.getPlayer());
                    return;
                }

                Voucher voucher = new Voucher(msg, instance);
                voucher.setMaterial(PAPER);
                voucher.setName("&f" + msg);
                voucher.setTexture("");

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
