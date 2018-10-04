package com.songoda.epicvouchers.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.UUID;

public class PreventHacks implements Listener {

    private static HashMap<UUID, String> commands = new HashMap<UUID, String>();

    @EventHandler
    public static void preventCommands(PlayerCommandPreprocessEvent event) {
        if (!commands.containsKey(event.getPlayer().getUniqueId())
                || event.getMessage().equalsIgnoreCase(commands.get(event.getPlayer().getUniqueId()))) {
            return;
        }
        event.setCancelled(true);
    }

    public static void addCommand(UUID uuid, String command) {
        commands.put(uuid, command);
    }
    public static void removeCommand(UUID uuid) {
        commands.remove(uuid);
    }


}