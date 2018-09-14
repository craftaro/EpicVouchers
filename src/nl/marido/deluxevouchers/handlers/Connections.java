package nl.marido.deluxevouchers.handlers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;

import org.bukkit.entity.Player;

import nl.marido.deluxevouchers.DeluxeVouchers;

public class Connections {

	public static Connection connection;

	public static void saveRedeem(Player player, String voucher) {
		if (DataHandler.mysqlenabled) {
			if (connection != null) {
				Timestamp stamp = new Timestamp(System.currentTimeMillis());
				Date date = new Date(stamp.getTime());
				String time = date.toString();
				try {
					Statement statement = connection.createStatement();
					statement.execute("CREATE TABLE IF NOT EXISTS redeems (id INT NOT NULL AUTO_INCREMENT, player varchar(120) NOT NULL, voucher varchar(120) NOT NULL, timestamp varchar(120) NOT NULL, PRIMARY KEY (ID));");
					statement.execute("INSERT INTO redeems VALUES (default, '" + player.getName() + "', '" + voucher + "', '" + time + "');");
					statement.close();
					DeluxeVouchers.printConsole("§fSuccessfully saved the redeem in the MySQL database.");
				} catch (Exception error) {
					DeluxeVouchers.printConsole("§cFailed to save the redeem data in the MySQL database.");
					if (DataHandler.debugerrors) {
						error.printStackTrace();
					}
				}
			}
		}
	}

	public static void openMySQL() {
		if (DataHandler.mysqlenabled) {
			if (connection == null) {
				try {
					connection = DriverManager.getConnection("jdbc:mysql://" + DataHandler.mysqlhost + ":" + DataHandler.mysqlport + "/" + DataHandler.mysqldata + DataHandler.additions, DataHandler.mysqluser, DataHandler.mysqlpass);
					DeluxeVouchers.printConsole("§fSuccessfully created a connection with MySQL.");
				} catch (Exception error) {
					DeluxeVouchers.printConsole("§cFailed to create a connection with MySQL.");
					if (DataHandler.debugerrors) {
						error.printStackTrace();
					}
				}
			}
		}
	}

	public static void closeMySQL() {
		if (DataHandler.mysqlenabled) {
			if (connection != null) {
				try {
					connection.close();
					DeluxeVouchers.printConsole("§fSuccessfully closed the MySQL connection.");
				} catch (Exception error) {
					DeluxeVouchers.printConsole("§cFailed to close the MySQL connection.");
					if (DataHandler.debugerrors) {
						error.printStackTrace();
					}
				}
			}
		}
	}

}