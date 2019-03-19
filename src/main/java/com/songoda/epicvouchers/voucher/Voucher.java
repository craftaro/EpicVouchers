package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.events.ForceRedeemEvent;
import com.songoda.epicvouchers.events.VoucherReceiveEvent;
import com.songoda.epicvouchers.utils.Methods;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.songoda.epicvouchers.utils.Methods.format;
import static org.bukkit.Material.PAPER;

@Getter
@Setter
@Accessors(chain = true)
public class Voucher {

    private final String key;
    private final EpicVouchers instance;
    private String permission = "";
    private Material material = PAPER;
    private short data = 0;
    private int coolDown = 0;
    private String name;
    private List<String> lore = new ArrayList<>();
    private boolean glow = true;
    private boolean confirm = true;
    private boolean unbreakable = false;
    private boolean hideAttributes = false;
    private boolean removeItem = true;
    private boolean feedPlayer = false;
    private boolean healPlayer = false;
    private boolean smiteEffect = false;

    private List<String> broadcasts = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
    private List<String> commands = new ArrayList<>();

    private String actionBar;

    private String title = "";
    private String subTitle = "";
    private int titleFadeIn = 0;
    private int titleStay = 0;
    private int titleFadeOut = 0;

    private String sound = "";
    private int soundPitch = 0;

    private String particle = "";
    private int particleAmount = 0;

    private String effect = "";
    private int effectAmplifier = 0;
    private int effectDuration = 0;

    private ItemStack itemStack;

    public Voucher(String key, EpicVouchers instance) {
        this.key = key;
        this.instance = instance;
    }

    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    public ItemStack toItemStack(int amount) {
        ItemStack item = itemStack == null ? new ItemStack(material, amount, data) : itemStack;
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(material);
        }

        if (!name.isEmpty()) {
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
        ConfigurationSection cs = instance.getVouchersFile().getConfig().getConfigurationSection("vouchers." + getKey());
        cs.set(key, value);
        instance.getVouchersFile().saveConfig();
    }

    @Override
    public String toString() {
        return key;
    }

    public void giveAll(CommandSender sender, int amount) {
        give(sender, new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
    }

    public void give(CommandSender sender, List<Player> players, int amount) {
        String giveMessage = instance.getLocale().getMessage("command.give.send")
                .replaceAll("%player%", players.size() == 1 ? players.get(0).getName() : "everyone")
                .replaceAll("%voucher%", getName(true))
                .replaceAll("%amount%", String.valueOf(amount));

        for (Player player : players) {
            String receiveMessage = instance.getLocale().getMessage("command.give.receive")
                    .replaceAll("%voucher%", getName(true))
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%amount%", String.valueOf(amount));

            VoucherReceiveEvent event = new VoucherReceiveEvent(player, getName(true), toItemStack(amount), amount, sender);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                sender.sendMessage(instance.getLocale().getMessage("command.give.cancelled"));
                continue;
            }

            player.sendMessage(receiveMessage);
            player.getInventory().addItem(toItemStack(amount));
        }

        sender.sendMessage(giveMessage);
    }

    public void forceRedeem(CommandSender sender, List<Player> players, int amount) {
        for (Player player : players) {
            ForceRedeemEvent event = new ForceRedeemEvent(player, getName(true), amount, sender);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled() || player.equals(sender)) {
                continue;
            }

            for (int i = 0; i < amount; i++) {
                instance.getVoucherExecutor().redeemVoucher(player, this, player.getItemInHand(), false, null);
            }
        }
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
