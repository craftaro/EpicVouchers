package nl.marido.deluxevouchers.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.marido.deluxevouchers.DeluxeVouchers;

public class DataHandler {

	public static String editorrenamestart;
	public static String editorrenamedone;
	public static String editorreceiveitem;
	public static boolean checkupdates;
	public static boolean debugerrors;
	public static String oldversionsound;
	public static int oldversionpitch;
	public static List<String> oldversionmessage;
	public static int cooldowndelay;
	public static String cooldownmessage;
	public static int editorbackitemslot;
	public static String editorbackitemmaterial;
	public static int editorbackitemdata;
	public static String editorbackitemname;
	public static List<String> editorbackitemlore;
	public static boolean editorbackitemglow;
	public static boolean editorbackitemunbreakable;
	public static boolean editorbackitemhideattributes;
	public static String editorbackitemsound;
	public static int editorbackitempitch;
	public static int editorcloneitemslot;
	public static String editorcloneitemmaterial;
	public static int editorcloneitemdata;
	public static String editorcloneitemname;
	public static List<String> editorcloneitemlore;
	public static boolean editorcloneitemglow;
	public static boolean editorcloneitemunbreakable;
	public static boolean editorcloneitemhideattributes;
	public static String editorcloneitemsound;
	public static int editorcloneitempitch;
	public static int editorrenameitemslot;
	public static String editorrenameitemmaterial;
	public static int editorrenameitemdata;
	public static String editorrenameitemname;
	public static List<String> editorrenameitemlore;
	public static boolean editorrenameitemglow;
	public static boolean editorrenameitemunbreakable;
	public static boolean editorrenameitemhideattributes;
	public static String editorrenameitemsound;
	public static int editorrenameitempitch;
	public static String editortitle;
	public static int editorslots;
	public static String editorsound;
	public static int editorpitch;
	public static boolean editorfill;
	public static String editorvouchertitle;
	public static int editorvoucherslot;
	public static int editorvoucherslots;
	public static String editorvouchersound;
	public static int editorvoucherpitch;
	public static boolean editorvoucherfill;
	public static String confirmtitle;
	public static int confirmslots;
	public static String confirmsound;
	public static int confirmpitch;
	public static boolean confirmfill;
	public static int confirmitemslot;
	public static String confirmitemmaterial;
	public static int confirmitemdata;
	public static String confirmitemname;
	public static List<String> confirmitemlore;
	public static boolean confirmitemglow;
	public static boolean confirmitemunbreakable;
	public static boolean confirmitemhideattributes;
	public static String confirmitemsound;
	public static int confirmitempitch;
	public static int cancelitemslot;
	public static String cancelitemmaterial;
	public static int cancelitemdata;
	public static String cancelitemname;
	public static List<String> cancelitemlore;
	public static boolean cancelitemglow;
	public static boolean cancelitemunbreakable;
	public static boolean cancelitemhideattributes;
	public static String cancelitemsound;
	public static int cancelitempitch;
	public static String commandreceive;
	public static String commandgive;
	public static String commandforce;
	public static String commandnoplayer;
	public static String commandnovoucher;
	public static String commandnonumber;
	public static String commandreload;
	public static String commandreset;
	public static String commandlicense;
	public static String commandlist;
	public static String commanddisable;
	public static String commandbackup;
	public static String commandpermission;
	public static String commandinvalid;
	public static List<String> commandhelp;
	public static boolean mysqlenabled;
	public static String mysqlhost;
	public static String mysqlport;
	public static String mysqluser;
	public static String mysqlpass;
	public static String mysqldata;
	public static String additions;

	public static File configfile;
	public static File vouchersfile;
	public static File mysqlfile;
	public static File messagesfile;

	public static FileConfiguration config;
	public static FileConfiguration vouchers;
	public static FileConfiguration mysql;
	public static FileConfiguration messages;

	// TODO: Switch to enums.
	public static void cacheData() {
		try {
			File folder = DeluxeVouchers.getInstance().getDataFolder();
			configfile = new File(folder, "config.yml");
			vouchersfile = new File(folder, "vouchers.yml");
			mysqlfile = new File(folder, "mysql.yml");
			messagesfile = new File(folder, "messages.yml");
			if (!configfile.exists()) {
				DeluxeVouchers.getInstance().saveResource("config.yml", true);
				DeluxeVouchers.printConsole("§fFailed to find the config.yml file. Generating...");
			}
			if (!vouchersfile.exists()) {
				DeluxeVouchers.getInstance().saveResource("vouchers.yml", true);
				DeluxeVouchers.printConsole("§fFailed to find the vouchers.yml file. Generating...");
			}
			if (!mysqlfile.exists()) {
				DeluxeVouchers.getInstance().saveResource("mysql.yml", true);
				DeluxeVouchers.printConsole("§fFailed to find the mysql.yml file. Generating...");
			}
			if (!messagesfile.exists()) {
				DeluxeVouchers.getInstance().saveResource("messages.yml", true);
				DeluxeVouchers.printConsole("§fFailed to find the messages.yml file. Generating...");
			}
			config = YamlConfiguration.loadConfiguration(configfile);
			vouchers = YamlConfiguration.loadConfiguration(vouchersfile);
			mysql = YamlConfiguration.loadConfiguration(mysqlfile);
			messages = YamlConfiguration.loadConfiguration(messagesfile);
			checkupdates = getBoolean(config, "check-updates");
			debugerrors = getBoolean(config, "debugger-mode");
			oldversionsound = getString(config, "outdated-version.sound");
			oldversionpitch = getInt(config, "outdated-version.pitch");
			oldversionmessage = getStringList(messages, "outdated-version");
			cooldowndelay = getInt(config, "cooldown-delay");
			cooldownmessage = getString(messages, "cooldown-message");
			editorbackitemslot = getInt(config, "editor-settings.back-item.slot");
			editorbackitemmaterial = getString(config, "editor-settings.back-item.material");
			editorbackitemdata = getInt(config, "editor-settings.back-item.data");
			editorbackitemname = getString(config, "editor-settings.back-item.name");
			editorbackitemlore = getStringList(config, "editor-settings.back-item.lore");
			editorbackitemglow = getBoolean(config, "editor-settings.back-item.glow");
			editorbackitemunbreakable = getBoolean(config, "editor-settings.back-item.unbreakable");
			editorbackitemhideattributes = getBoolean(config, "editor-settings.back-item.hide-attributes");
			editorbackitemsound = getString(config, "editor-settings.back-item.sounds.sound");
			editorbackitempitch = getInt(config, "editor-settings.back-item.sounds.pitch");
			editorcloneitemslot = getInt(config, "editor-settings.receive-item.slot");
			editorcloneitemmaterial = getString(config, "editor-settings.receive-item.material");
			editorcloneitemdata = getInt(config, "editor-settings.receive-item.data");
			editorcloneitemname = getString(config, "editor-settings.receive-item.name");
			editorcloneitemlore = getStringList(config, "editor-settings.receive-item.lore");
			editorcloneitemglow = getBoolean(config, "editor-settings.receive-item.glow");
			editorcloneitemunbreakable = getBoolean(config, "editor-settings.receive-item.unbreakable");
			editorcloneitemhideattributes = getBoolean(config, "editor-settings.receive-item.hide-attributes");
			editorcloneitemsound = getString(config, "editor-settings.receive-item.sounds.sound");
			editorcloneitempitch = getInt(config, "editor-settings.receive-item.sounds.pitch");
			editorrenameitemslot = getInt(config, "editor-settings.rename-item.slot");
			editorrenameitemmaterial = getString(config, "editor-settings.rename-item.material");
			editorrenameitemdata = getInt(config, "editor-settings.rename-item.data");
			editorrenameitemname = getString(config, "editor-settings.rename-item.name");
			editorrenamestart = getString(messages, "editor-renamer");
			editorrenamedone = getString(messages, "editor-renamed");
			editorreceiveitem = getString(messages, "editor-cloned");
			editorrenameitemlore = getStringList(config, "editor-settings.rename-item.lore");
			editorrenameitemglow = getBoolean(config, "editor-settings.rename-item.glow");
			editorrenameitemunbreakable = getBoolean(config, "editor-settings.rename-item.unbreakable");
			editorrenameitemhideattributes = getBoolean(config, "editor-settings.rename-item.hide-attributes");
			editorrenameitemsound = getString(config, "editor-settings.rename-item.sounds.sound");
			editorrenameitempitch = getInt(config, "editor-settings.rename-item.sounds.pitch");
			editortitle = getString(config, "editor-settings.editor-title");
			editorslots = getInt(config, "editor-settings.editor-slots");
			editorsound = getString(config, "editor-settings.editor-sounds.sound");
			editorpitch = getInt(config, "editor-settings.editor-sounds.pitch");
			editorfill = getBoolean(config, "editor-settings.fill-glass");
			editorvouchertitle = getString(config, "editor-settings.editor-voucher.title");
			editorvoucherslot = getInt(config, "editor-settings.editor-voucher.voucher-slot");
			editorvoucherslots = getInt(config, "editor-settings.editor-voucher.slots");
			editorvouchersound = getString(config, "editor-settings.editor-voucher.sound");
			editorvoucherpitch = getInt(config, "editor-settings.editor-voucher.pitch");
			editorvoucherfill = getBoolean(config, "editor-settings.editor-voucher.fill-glass");
			confirmtitle = getString(config, "confirm-settings.confirm-title");
			confirmslots = getInt(config, "confirm-settings.confirm-slots");
			confirmsound = getString(config, "confirm-settings.confirm-sounds.sound");
			confirmpitch = getInt(config, "confirm-settings.confirm-sounds.pitch");
			confirmfill = getBoolean(config, "confirm-settings.fill-glass");
			confirmitemslot = getInt(config, "confirm-settings.confirm-item.slot");
			confirmitemmaterial = getString(config, "confirm-settings.confirm-item.material");
			confirmitemdata = getInt(config, "confirm-settings.confirm-item.data");
			confirmitemname = getString(config, "confirm-settings.confirm-item.name");
			confirmitemlore = getStringList(config, "confirm-settings.confirm-item.lore");
			confirmitemglow = getBoolean(config, "confirm-settings.confirm-item.glow");
			confirmitemunbreakable = getBoolean(config, "confirm-settings.confirm-item.unbreakable");
			confirmitemhideattributes = getBoolean(config, "confirm-settings.confirm-item.hide-attributes");
			confirmitemsound = getString(config, "confirm-settings.confirm-item.sounds.sound");
			confirmitempitch = getInt(config, "confirm-settings.confirm-item.sounds.pitch");
			cancelitemslot = getInt(config, "confirm-settings.cancel-item.slot");
			cancelitemmaterial = getString(config, "confirm-settings.cancel-item.material");
			cancelitemdata = getInt(config, "confirm-settings.cancel-item.data");
			cancelitemname = getString(config, "confirm-settings.cancel-item.name");
			cancelitemlore = getStringList(config, "confirm-settings.cancel-item.lore");
			cancelitemglow = getBoolean(config, "confirm-settings.cancel-item.glow");
			cancelitemunbreakable = getBoolean(config, "confirm-settings.cancel-item.unbreakable");
			cancelitemhideattributes = getBoolean(config, "confirm-settings.cancel-item.hide-attributes");
			cancelitemsound = getString(config, "confirm-settings.cancel-item.sounds.sound");
			cancelitempitch = getInt(config, "confirm-settings.cancel-item.sounds.pitch");
			commandreceive = getString(messages, "command-receive");
			commandgive = getString(messages, "command-give");
			commandforce = getString(messages, "command-force");
			commandnoplayer = getString(messages, "command-noplayer");
			commandnovoucher = getString(messages, "command-novoucher");
			commandnonumber = getString(messages, "command-nonumber");
			commandreload = getString(messages, "command-reload");
			commandreset = getString(messages, "command-reset");
			commandlicense = getString(messages, "command-license");
			commandlist = getString(messages, "command-list");
			commanddisable = getString(messages, "command-disable");
			commandbackup = getString(messages, "command-backup");
			commandpermission = getString(messages, "command-permission");
			commandinvalid = getString(messages, "command-invalid");
			commandhelp = getStringList(messages, "command-help");
			mysqlenabled = getBoolean(mysql, "use-mysql");
			mysqlhost = getString(mysql, "mysqlhost");
			mysqlport = getString(mysql, "mysqlport");
			mysqluser = getString(mysql, "mysqluser");
			mysqlpass = getString(mysql, "mysqlpass");
			mysqldata = getString(mysql, "mysqldata");
			additions = getString(mysql, "additions");
		} catch (Exception error) {
			DeluxeVouchers.printConsole("§cFailed to copy the configuration options to the cache memory.");
			DeluxeVouchers.printConsole("§cMake sure to update your configuration options or reset it.");
			if (debugerrors) {
				error.printStackTrace();
			}
		}
	}

	public static boolean stringExist(FileConfiguration datafile, String path) {
		try {
			return datafile.getString(path) != null;
		} catch (Exception error) {
			DeluxeVouchers.printConsole("§cFailed to find the path " + path + ".");
			if (debugerrors) {
				error.printStackTrace();
			}
		}
		return false;
	}

	public static boolean getBoolean(FileConfiguration datafile, String path) {
		try {
			return datafile.getBoolean(path);
		} catch (Exception error) {
			DeluxeVouchers.printConsole("§cFailed to find the path " + path + ".");
			if (debugerrors) {
				error.printStackTrace();
			}
		}
		return false;
	}

	public static int getInt(FileConfiguration datafile, String path) {
		try {
			return datafile.getInt(path);
		} catch (Exception error) {
			DeluxeVouchers.printConsole("§cFailed to find the path " + path + ".");
			if (debugerrors) {
				error.printStackTrace();
			}
		}
		return 0;
	}

	public static String getString(FileConfiguration datafile, String path) {
		try {
			return DeluxeVouchers.applyColor(datafile.getString(path));
		} catch (Exception error) {
			DeluxeVouchers.printConsole("§cFailed to find the path " + path + ".");
			if (debugerrors) {
				error.printStackTrace();
			}
		}
		return null;
	}

	public static Set<String> getSection(FileConfiguration datafile, String path) {
		try {
			return datafile.getConfigurationSection(path).getKeys(false);
		} catch (Exception error) {
			DeluxeVouchers.printConsole("§cFailed to find the path " + path + ".");
			if (debugerrors) {
				error.printStackTrace();
			}
		}
		return null;
	}

	public static ArrayList<String> getStringList(FileConfiguration datafile, String path) {
		try {
			ArrayList<String> stringlist = new ArrayList<String>();
			for (String line : datafile.getStringList(path)) {
				stringlist.add(DeluxeVouchers.applyColor(line));
			}
			return stringlist;
		} catch (Exception error) {
			DeluxeVouchers.printConsole("§cFailed to find the path " + path + ".");
			if (debugerrors) {
				error.printStackTrace();
			}
		}
		return null;
	}

}