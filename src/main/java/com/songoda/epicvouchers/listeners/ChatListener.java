package com.songoda.epicvouchers.listeners;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.menus.EditorMenu;
import com.songoda.epicvouchers.utils.Methods;
import com.songoda.epicvouchers.utils.SoundUtils;
import com.songoda.epicvouchers.voucher.Voucher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final EpicVouchers instance;

    public ChatListener(EpicVouchers instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if(!EditorMenu.getRename().containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        Voucher voucher = EditorMenu.getRename().remove(event.getPlayer().getUniqueId());
        voucher.setName(event.getMessage());

        event.getPlayer().sendMessage(Methods.formatText(instance.getLocale().getMessage("interface.editvoucher.renamefinish", event.getMessage())));

        new EditorMenu(instance, voucher).open(event.getPlayer());
        SoundUtils.playSound(event.getPlayer(), "NOTE_PIANO", 1);
        event.setCancelled(true);
    }
}
