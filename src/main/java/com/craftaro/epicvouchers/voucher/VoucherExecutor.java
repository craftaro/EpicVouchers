package com.craftaro.epicvouchers.voucher;

import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.epicvouchers.EpicVouchers;
import com.craftaro.epicvouchers.events.VoucherRedeemEvent;
import com.craftaro.epicvouchers.listeners.PlayerCommandListener;
import com.craftaro.third_party.com.cryptomorin.xseries.XSound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.logging.Level;

public class VoucherExecutor {
    private final EpicVouchers instance;

    public VoucherExecutor(EpicVouchers instance) {
        this.instance = instance;
    }

    public void redeemVoucher(Player player, Voucher voucher, ItemStack item, boolean manual, PlayerInteractEvent event) {
        try {
            VoucherRedeemEvent redeemEvent = new VoucherRedeemEvent(player, voucher.getName(true), item, manual);
            Bukkit.getServer().getPluginManager().callEvent(redeemEvent);

            if (redeemEvent.isCancelled()) {
                return;
            }

            boolean duplication = false;
            int slot = player.getInventory().getHeldItemSlot();

            if (event != null) {
                slot = event.getPlayer().getInventory().getHeldItemSlot();

                try {
                    if (event.getHand() == EquipmentSlot.OFF_HAND) slot = 40;
                } catch (Exception | Error ignore) {
                }

                if (!item.isSimilar(player.getInventory().getItem(slot))) {
                    duplication = true;
                }
            }

            if (!duplication) {
                if (manual) {
                    this.instance.getCoolDowns().addCoolDown(player.getUniqueId(), voucher);
                    if (voucher.isRemoveItem()) {
                        if (item.getAmount() <= 1) {
                            item = null;
                        } else {
                            item.setAmount(item.getAmount() - 1);
                        }
                        player.getInventory().setItem(slot, item);
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
                    } else if (command.startsWith("[delay")) {
                        String delayCommand = StringUtils.substringBetween(command, "[", "]");
                        int delay = Integer.parseInt(delayCommand.split("-", 2)[1]);
                        final String finalCommand = command.replace("[" + delayCommand + "]", "");
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this.instance, () -> runCommand(finalCommand, player), 20L * delay);
                    } else {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
                if (voucher.getActionBar() != null && !voucher.getActionBar().isEmpty()) {
                    String actionbar = voucher.getActionBar().replaceAll("%player%", name).replaceAll("%voucher%", voucher.getName(true));
                    this.instance.getLocale().newMessage(actionbar).sendActionBar(player);
                }

                if (voucher.getTitle() != null && !voucher.getTitle().isEmpty()) {
                    String title = voucher.getTitle().replaceAll("%player%", name).replaceAll("%voucher%", voucher.getName(true));
                    String subtitle = voucher.getSubTitle().replaceAll("%player%", name).replaceAll("%voucher%", voucher.getName(true));

                    int fadein = voucher.getTitleFadeIn();
                    int stay = voucher.getTitleStay();
                    int fadeout = voucher.getTitleFadeOut();

                    if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_11)) {
                        player.sendTitle(title, subtitle, fadein, stay, fadeout);
                    } else {
                        player.sendTitle(title, subtitle);
                    }
                }

                if (voucher.getSound() != null && !voucher.getSound().isEmpty()) {
                    Optional<XSound> sound = XSound.matchXSound(voucher.getSound());
                    sound.ifPresent(xSound -> xSound.play(player, 1.0f, voucher.getSoundPitch()));
                }

                String particle = voucher.getParticle();

                if (particle != null && !particle.isEmpty()) {
                    player.getWorld().playEffect(player.getLocation(), Effect.valueOf(particle), voucher.getParticleAmount());
                }

                String effect = voucher.getEffect();

                if (effect != null && !effect.isEmpty()) {
                    int amplifier = voucher.getEffectAmplifier();
                    int duration = voucher.getEffectDuration() * 20;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), duration, amplifier));
                }

                this.instance.getLogger().log(Level.INFO, player.getName() + " has successfully redeemed the voucher " + voucher.getKey() + ".");
                this.instance.getConnections().saveRedeem(player, voucher.getName(true));
            } else {
                this.instance.getLogger().log(Level.WARNING, player.getName() + " has failed to duplicate the voucher " + voucher.getKey() + ".");
            }
        } catch (Exception error) {
            this.instance.getLogger().log(Level.SEVERE, "Failed to redeem the voucher " + voucher.getKey() + " for the player " + player.getName() + ".");
            this.instance.getLogger().log(Level.SEVERE, error.getMessage());
            error.printStackTrace();
        }
    }

    private void runCommand(String command, Player player) {
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
        } else {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
