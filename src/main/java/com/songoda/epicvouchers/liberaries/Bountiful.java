package com.songoda.epicvouchers.liberaries;

import com.songoda.epicvouchers.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Bountiful {

	public static boolean oldmethod;
	public static String nmsversion;

	public static void findVersion() {
		nmsversion = Bukkit.getServer().getClass().getPackage().getName();
		nmsversion = nmsversion.substring(nmsversion.lastIndexOf(".") + 1);
	}

	public static Integer getPlayerProtocol(Player player) {
		return Integer.valueOf(47);
	}

	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
		} catch (Exception error) {
			Debugger.runReport(error);
		}
	}

	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (Exception error) {
			Debugger.runReport(error);
		}
		return null;
	}

	public static void sendTitle(Player player, Integer fadein, Integer stay, Integer fadeout, String title, String subtitle) {
		try {
			if (title != null) {
				Object field = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				Object chattitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
				Constructor<?> subtitleconstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				Object titlepacket = subtitleconstructor.newInstance(new Object[] { field, chattitle, fadein, stay, fadeout });
				sendPacket(player, titlepacket);
				field = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				chattitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
				subtitleconstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent") });
				titlepacket = subtitleconstructor.newInstance(new Object[] { field, chattitle });
				sendPacket(player, titlepacket);
			}
			if (subtitle != null) {
				Object field = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				Object chatsubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
				Constructor<?> subtitleconstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				Object subtitlepacket = subtitleconstructor.newInstance(new Object[] { field, chatsubtitle, fadein, stay, fadeout });
				sendPacket(player, subtitlepacket);
				field = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
				chatsubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
				subtitleconstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				subtitlepacket = subtitleconstructor.newInstance(new Object[] { field, chatsubtitle, fadein, stay, fadeout });
				sendPacket(player, subtitlepacket);
			}
		} catch (Exception error) {
			Debugger.runReport(error);
		}
	}

	public static void sendActionBar(Player player, String message) {
		if (nmsversion.startsWith("v1_12_")) {
			sendActionbarBefore(player, message);
		} else {
			sendActionbarAfter(player, message);
		}
	}

	public static void sendActionbarBefore(Player player, String message) {
		try {
			Class<?> craftplayerclass = Class.forName("org.bukkit.craftbukkit." + nmsversion + ".entity.CraftPlayer");
			Object craftplayer = craftplayerclass.cast(player);
			Class<?> playoutchat = Class.forName("net.minecraft.server." + nmsversion + ".PacketPlayOutChat");
			Class<?> simplepacket = Class.forName("net.minecraft.server." + nmsversion + ".Packet");
			Class<?> chatcomponent = Class.forName("net.minecraft.server." + nmsversion + ".ChatComponentText");
			Class<?> basecomponent = Class.forName("net.minecraft.server." + nmsversion + ".IChatBaseComponent");
			Class<?> chatmessagetypeclass = Class.forName("net.minecraft.server." + nmsversion + ".ChatMessageType");
			Object[] chatmessagetypes = chatmessagetypeclass.getEnumConstants();
			Object chatmessagetype = null;
			for (Object object : chatmessagetypes) {
				if (object.toString().equals("GAME_INFO")) {
					chatmessagetype = object;
				}
			}
			Object object = chatcomponent.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
			Object customobject = playoutchat.getConstructor(new Class[] { basecomponent, chatmessagetypeclass }).newInstance(new Object[] { object, chatmessagetype });
			Method method = craftplayerclass.getDeclaredMethod("getHandle", new Class[0]);
			Object invokedmethod = method.invoke(craftplayer, new Object[0]);
			Field field = invokedmethod.getClass().getDeclaredField("playerConnection");
			Object packetchat = field.get(invokedmethod);
			Method methodclass = packetchat.getClass().getDeclaredMethod("sendPacket", new Class[] { simplepacket });
			methodclass.invoke(packetchat, new Object[] { customobject });
		} catch (Exception error) {
			Debugger.runReport(error);
		}
	}

	public static void sendActionbarAfter(Player player, String message) {
		try {
			Class<?> craftplayerclass = Class.forName("org.bukkit.craftbukkit." + nmsversion + ".entity.CraftPlayer");
			Object craftplayer = craftplayerclass.cast(player);
			Class<?> playoutchat = Class.forName("net.minecraft.server." + nmsversion + ".PacketPlayOutChat");
			Class<?> simplepacket = Class.forName("net.minecraft.server." + nmsversion + ".Packet");
			Object customobject;
			if (oldmethod) {
				Class<?> chatserializer = Class.forName("net.minecraft.server." + nmsversion + ".ChatSerializer");
				Class<?> basecomponent = Class.forName("net.minecraft.server." + nmsversion + ".IChatBaseComponent");
				Method method = chatserializer.getDeclaredMethod("a", new Class[] { String.class });
				Object baseinvoke = basecomponent.cast(method.invoke(chatserializer, new Object[] { "{\"text\": \"" + message + "\"}" }));
				customobject = playoutchat.getConstructor(new Class[] { basecomponent, Byte.TYPE }).newInstance(new Object[] { baseinvoke, Byte.valueOf((byte) 2) });
			} else {
				Class<?> componenttext = Class.forName("net.minecraft.server." + nmsversion + ".ChatComponentText");
				Class<?> chatbase = Class.forName("net.minecraft.server." + nmsversion + ".IChatBaseComponent");
				Object object = componenttext.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
				customobject = playoutchat.getConstructor(new Class[] { chatbase, Byte.TYPE }).newInstance(new Object[] { object, Byte.valueOf((byte) 2) });
			}
			Method handlemethod = craftplayerclass.getDeclaredMethod("getHandle", new Class[0]);
			Object objectinvoked = handlemethod.invoke(craftplayer, new Object[0]);
			Field field = objectinvoked.getClass().getDeclaredField("playerConnection");
			Object fieldinvoked = field.get(objectinvoked);
			Method methodclass = fieldinvoked.getClass().getDeclaredMethod("sendPacket", new Class[] { simplepacket });
			methodclass.invoke(fieldinvoked, new Object[] { customobject });
		} catch (Exception error) {
			Debugger.runReport(error);
		}
	}

}