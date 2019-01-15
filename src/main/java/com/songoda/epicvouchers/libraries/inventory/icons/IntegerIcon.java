package com.songoda.epicvouchers.libraries.inventory.icons;

import com.songoda.epicvouchers.EpicVouchers;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class IntegerIcon extends StringIcon {
    public IntegerIcon(EpicVouchers instance, String string, int current, BiConsumer<Player, Integer> consumer) {
        super(instance, Material.IRON_INGOT, string, "" + current, (player, edited) -> consumer.accept(player, Integer.parseInt(edited)), StringUtils::isNumeric);
    }
}
