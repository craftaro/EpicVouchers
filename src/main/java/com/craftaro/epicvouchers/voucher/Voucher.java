package com.craftaro.epicvouchers.voucher;

import com.craftaro.core.chat.AdventureUtils;
import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.core.third_party.net.kyori.adventure.text.Component;
import com.craftaro.third_party.com.cryptomorin.xseries.XMaterial;
import com.craftaro.core.third_party.de.tr7zw.nbtapi.NBTItem;
import com.craftaro.core.utils.ItemUtils;
import com.craftaro.core.utils.TextUtils;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.events.ForceRedeemEvent;
import com.craftaro.epicvouchers.events.VoucherReceiveEvent;
import com.craftaro.epicvouchers.menus.ConfirmMenu;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Material.PAPER;

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
    private String texture;
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
        ItemStack item = this.itemStack == null ? new ItemStack(this.material, amount, this.data) : this.itemStack;
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(this.material);
        }

        if (!this.name.isEmpty()) {
            meta.setDisplayName(TextUtils.formatText(this.name));
        }

        if (this.lore != null) {
            meta.setLore(getLore(true));
        }

        if (this.glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (this.hideAttributes) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        if (this.unbreakable) {
            if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9)) {
                meta.setUnbreakable(true);
                item.setItemMeta(meta);
            } else {
                item.setItemMeta(meta);

                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setByte("Unbreakable", (byte) 1);
                item = nbtItem.getItem();
            }
        }

        if (this.texture != null && !this.texture.isEmpty() && XMaterial.PLAYER_HEAD.parseMaterial() == this.material) {
            item = ItemUtils.getCustomHead(this.texture);
        }

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("epicvouchers:voucher", this.key);

        return nbtItem.getItem();
    }

    public String getName(boolean applyFormatting) {
        return applyFormatting ? TextUtils.formatText(this.name) : this.name;
    }

    public List<String> getLore(boolean applyFormatting) {
        if (!applyFormatting) {
            return Collections.unmodifiableList(this.lore);
        }

        List<String> lore = new ArrayList<>();
        for (String line : this.lore) {
            lore.add(TextUtils.formatText(line));
        }
        return lore;
    }

    public List<String> getBroadcasts(boolean applyFormatting) {
        if (!applyFormatting) {
            return this.broadcasts;
        }

        List<String> result = new ArrayList<>();
        for (String broadcast : this.broadcasts) {
            result.add(TextUtils.formatText(broadcast));
        }
        return result;
    }

    public List<String> getMessages(boolean applyFormatting) {
        if (!applyFormatting) {
            return this.messages;
        }

        List<String> list = new ArrayList<>();
        for (String message : this.messages) {
            list.add(TextUtils.formatText(message));
        }
        return list;
    }

    public void saveSetting(String key, List<String> list) {
        switch (key) {
            case "Commands":
                this.commands = list;
                break;
            case "Broadcasts":
                this.broadcasts = list;
                break;
            case "Messages":
                this.messages = list;
                break;
            case "Lore":
                this.lore = list;
                break;
        }
    }

    @Override
    public String toString() {
        return this.key;
    }

    public void giveAll(CommandSender sender, int amount) {
        give(sender, new ArrayList<>(Bukkit.getOnlinePlayers()), amount);
    }

    public void give(CommandSender sender, List<Player> players, int amount) {
        Component giveMessage = this.instance.getLocale().getMessage("command.give.send")
                .processPlaceholder("player", players.size() == 1 ? players.get(0).getName() : "everyone")
                .processPlaceholder("voucher", getName(true))
                .processPlaceholder("amount", String.valueOf(amount)).getPrefixedMessage();

        for (Player player : players) {
            Component receiveMessage = this.instance.getLocale().getMessage("command.give.receive")
                    .processPlaceholder("voucher", getName(true))
                    .processPlaceholder("player", player.getName())
                    .processPlaceholder("amount", String.valueOf(amount)).getPrefixedMessage();

            VoucherReceiveEvent event = new VoucherReceiveEvent(player, getName(true), toItemStack(amount), amount, sender);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                this.instance.getLocale().getMessage("command.give.cancelled").sendPrefixedMessage(sender);
                continue;
            }

            AdventureUtils.sendMessage(EpicVouchers.getInstance(), receiveMessage, player);
            player.getInventory().addItem(toItemStack(amount));
        }

        AdventureUtils.sendMessage(EpicVouchers.getInstance(), giveMessage, sender);
    }

    public void forceRedeem(CommandSender sender, List<Player> players, int amount) {
        for (Player player : players) {
            ForceRedeemEvent event = new ForceRedeemEvent(player, getName(true), amount, sender);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled() || player.equals(sender)) {
                continue;
            }

            for (int i = 0; i < amount; i++) {
                this.instance.getVoucherExecutor().redeemVoucher(player, this, player.getItemInHand(), false, null);
            }
        }
    }

    public void redeemVoucher(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // does the player have permission to redeem this voucher?
        if (!this.permission.isEmpty() && !player.hasPermission(this.permission)) {
            this.instance.getLocale().getMessage("event.general.nopermission").sendPrefixedMessage(player);
            return;
        }

        UUID uuid = player.getUniqueId();

        if (this.instance.getCoolDowns().isOnCoolDown(uuid)) {
            this.instance.getLocale().getMessage("event.general.cooldown")
                    .processPlaceholder("time", this.instance.getCoolDowns().getTime(uuid))
                    .processPlaceholder("voucher", getName(true))
                    .sendPrefixedMessage(player);
            return;
        }

        if (this.confirm) {
            new ConfirmMenu(this.instance,
                    () -> this.instance.getVoucherExecutor().redeemVoucher(player, this, event.getItem(), true, event),
                    () -> {
                    })
                    .open(player);
        } else {
            this.instance.getVoucherExecutor().redeemVoucher(player, this, event.getItem(), true, event);
        }
    }

    public String getTexture() {
        return this.texture;
    }

    public String getActionBar() {
        return TextUtils.formatText(this.actionBar);
    }

    public String getSubTitle() {
        return TextUtils.formatText(this.subTitle);
    }

    public String getTitle() {
        return TextUtils.formatText(this.title);
    }

    public String getKey() {
        return this.key;
    }

    public EpicVouchers getInstance() {
        return this.instance;
    }

    public String getPermission() {
        return this.permission;
    }

    public Material getMaterial() {
        return this.material;
    }

    public short getData() {
        return this.data;
    }

    public int getCoolDown() {
        return this.coolDown;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public boolean isGlow() {
        return this.glow;
    }

    public boolean isConfirm() {
        return this.confirm;
    }

    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    public boolean isHideAttributes() {
        return this.hideAttributes;
    }

    public boolean isRemoveItem() {
        return this.removeItem;
    }

    public boolean isFeedPlayer() {
        return this.feedPlayer;
    }

    public boolean isHealPlayer() {
        return this.healPlayer;
    }

    public boolean isSmiteEffect() {
        return this.smiteEffect;
    }

    public List<String> getBroadcasts() {
        return this.broadcasts;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public int getTitleFadeIn() {
        return this.titleFadeIn;
    }

    public int getTitleStay() {
        return this.titleStay;
    }

    public int getTitleFadeOut() {
        return this.titleFadeOut;
    }

    public String getSound() {
        return this.sound;
    }

    public int getSoundPitch() {
        return this.soundPitch;
    }

    public String getParticle() {
        return this.particle;
    }

    public int getParticleAmount() {
        return this.particleAmount;
    }

    public String getEffect() {
        return this.effect;
    }

    public int getEffectAmplifier() {
        return this.effectAmplifier;
    }

    public int getEffectDuration() {
        return this.effectDuration;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public Voucher setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public Voucher setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public Voucher setData(short data) {
        this.data = data;
        return this;
    }

    public Voucher setCoolDown(int coolDown) {
        this.coolDown = coolDown;
        return this;
    }

    public Voucher setName(String name) {
        this.name = name;
        return this;
    }

    public Voucher setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public Voucher setGlow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public Voucher setConfirm(boolean confirm) {
        this.confirm = confirm;
        return this;
    }

    public Voucher setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public Voucher setHideAttributes(boolean hideAttributes) {
        this.hideAttributes = hideAttributes;
        return this;
    }

    public Voucher setRemoveItem(boolean removeItem) {
        this.removeItem = removeItem;
        return this;
    }

    public Voucher setFeedPlayer(boolean feedPlayer) {
        this.feedPlayer = feedPlayer;
        return this;
    }

    public Voucher setHealPlayer(boolean healPlayer) {
        this.healPlayer = healPlayer;
        return this;
    }

    public Voucher setSmiteEffect(boolean smiteEffect) {
        this.smiteEffect = smiteEffect;
        return this;
    }

    public Voucher setBroadcasts(List<String> broadcasts) {
        this.broadcasts = broadcasts;
        return this;
    }

    public Voucher setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }

    public Voucher setCommands(List<String> commands) {
        this.commands = commands;
        return this;
    }

    public Voucher setTexture(String texture) {
        this.texture = texture;
        return this;
    }

    public Voucher setActionBar(String actionBar) {
        this.actionBar = actionBar;
        return this;
    }

    public Voucher setTitle(String title) {
        this.title = title;
        return this;
    }

    public Voucher setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public Voucher setTitleFadeIn(int titleFadeIn) {
        this.titleFadeIn = titleFadeIn;
        return this;
    }

    public Voucher setTitleStay(int titleStay) {
        this.titleStay = titleStay;
        return this;
    }

    public Voucher setTitleFadeOut(int titleFadeOut) {
        this.titleFadeOut = titleFadeOut;
        return this;
    }

    public Voucher setSound(String sound) {
        this.sound = sound;
        return this;
    }

    public Voucher setSoundPitch(int soundPitch) {
        this.soundPitch = soundPitch;
        return this;
    }

    public Voucher setParticle(String particle) {
        this.particle = particle;
        return this;
    }

    public Voucher setParticleAmount(int particleAmount) {
        this.particleAmount = particleAmount;
        return this;
    }

    public Voucher setEffect(String effect) {
        this.effect = effect;
        return this;
    }

    public Voucher setEffectAmplifier(int effectAmplifier) {
        this.effectAmplifier = effectAmplifier;
        return this;
    }

    public Voucher setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
        return this;
    }

    public Voucher setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
}
