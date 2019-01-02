package com.songoda.epicvouchers.voucher;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.events.VoucherRedeemEvent;
import com.songoda.epicvouchers.libraries.Bountiful;
import com.songoda.epicvouchers.listeners.PlayerCommandListener;
import com.songoda.epicvouchers.utils.Methods;
import com.songoda.epicvouchers.utils.SoundUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

public class VoucherExecutor {
    private final EpicVouchers instance;

    public VoucherExecutor(EpicVouchers instance) {
        this.instance = instance;
    }

    public void redeemVoucher(Player player, Voucher voucher, ItemStack item, boolean manual) {
        try {
            VoucherRedeemEvent event = new VoucherRedeemEvent(player, voucher.getName(true), item, manual);
            Bukkit.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            boolean duplication = false;

            if (!player.getItemInHand().isSimilar(item)) {
                duplication = true;
            }

            if (!duplication) {
                if (manual) {
                    instance.getCooldowns().addCooldown(player.getUniqueId(), voucher);
                    if (voucher.isRemoveItem()) {
                        ItemStack clone = player.getItemInHand().clone();
                        if (clone.getAmount() <= 1) {
                            clone.setType(Material.AIR);
                        } else {
                            clone.setAmount(clone.getAmount() - 1);
                        }
                        player.setItemInHand(clone);
                        player.updateInventory();
                    }
                }

                if (voucher.isFeedPlayer()) {
                    player.setFoodLevel(20);
                }

                if (voucher.isHealPlayer()) {
                    player.setHealth(player.getMaxHealth());
                }

                if (voucher.isSmiteEffect()) {
                    player.getWorld().strikeLightningEffect(player.getLocation());
                }

                String name = player.getName();

                for (String broadcast : voucher.getBroadcasts(true)) {
                    broadcast = broadcast.replaceAll("%player%", name);
                    broadcast = broadcast.replaceAll("%voucher%", voucher.getName(true));
                    for (Player everyone : Bukkit.getOnlinePlayers()) {
                        everyone.sendMessage(broadcast);
                    }
                }

                for (String message : voucher.getMessages(true)) {
                    message = message.replaceAll("%player%", name);
                    message = message.replaceAll("%voucher%", voucher.getName(true));
                    player.sendMessage(message);
                }

                for (String command : voucher.getCommands()) {
                    command = command.replaceAll("%player%", name).replaceAll("%voucher%", voucher.getName(false));

                    if (command.startsWith("[player]")) {
                        command = command.replace("[player]", "");
                        player.performCommand(command);
                    } else if (command.startsWith("[op]")) {
                        command = command.replace("[op]", "");
                        boolean wasOp = player.isOp();
                        PlayerCommandListener.addCommand(player.getUniqueId(), command);
                        player.setOp(true);
                        player.performCommand(command);

                        if (!wasOp) {
                            player.setOp(false);
                        }

                        PlayerCommandListener.removeCommand(player.getUniqueId());
                    } else if (command.startsWith("[chat]")) {
                        command = command.replace("[chat]", "");
                        player.chat(command);
                    } else if (command.startsWith("[delay]")) {
                        //command = command.replace("[delay]", "");
                        throw new UnsupportedOperationException("delay is not supported yet");
                    } else {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }

                String actionbar = voucher.getActionBar().replaceAll("%player%", name).replaceAll("%voucher%", voucher.getName(true));
                Bountiful.sendActionBar(player, actionbar);

                String title = voucher.getTitle().replaceAll("%player%", name).replaceAll("%voucher%", voucher.getName(true));
                String subtitle = voucher.getSubTitle().replaceAll("%player%", name).replaceAll("%voucher%", voucher.getName(true));

                int fadein = voucher.getTitleFadeIn();
                int stay = voucher.getTitleStay();
                int fadeout = voucher.getTitleFadeOut();

                Bountiful.sendTitle(player, fadein, stay, fadeout, title, subtitle);

                String sound = voucher.getSound();
                int pitch = voucher.getSoundPitch();
                SoundUtils.playSound(player, sound, pitch);

                String particle = voucher.getParticle();

                if (particle != null && !particle.isEmpty()) {
                    int amount = voucher.getParticleAmount();
                    player.getWorld().playEffect(player.getLocation(), Effect.valueOf(particle), amount);
                }

                String effect = voucher.getEffect();

                if (!effect.isEmpty()) {
                    int amplifier = voucher.getEffectAmplifer();
                    int duration = voucher.getEffectDuration() * 20;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), duration, amplifier));
                }

                instance.getLogger().log(Level.INFO, Methods.formatText("&f" + player.getName() + " has successfully redeemed the voucher " + voucher.getKey() + "."));
                instance.getConnections().saveRedeem(player, voucher.getName(true));
            } else {
                instance.getLogger().log(Level.WARNING, Methods.formatText("&c" + player.getName() + " has failed to duplicate the voucher " + voucher.getKey() + "."));
            }
        } catch (Exception error) {
            error.printStackTrace();
            instance.getLogger().log(Level.SEVERE, Methods.formatText("&cFailed to redeem the voucher " + voucher.getKey() + " for the player " + player.getName() + "."));
//            Debugger.runReport(error);
        }
    }

}