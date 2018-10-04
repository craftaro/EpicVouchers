package com.songoda.epicvouchers.handlers;

import com.songoda.epicvouchers.EpicVouchers;
import com.songoda.epicvouchers.utils.Debugger;
import com.songoda.epicvouchers.utils.Methods;
import org.bukkit.entity.Player;

import java.sql.*;

public class Connections {

    private EpicVouchers instance;
    private Connection connection;

    public Connections(EpicVouchers instance) {
        this.instance = instance;
    }

    public void openMySQL() {
        if (!instance.getConfig().getBoolean("Database.Activate Mysql Support") || connection == null) {
            return;
        }
        try {
            String mysqlIP = instance.getConfig().getString("Database.IP");
            String mysqlPort = instance.getConfig().getString("Database.PORT");
            String mysqlDatabase = instance.getConfig().getString("Database.Database Name");
            String mysqlUsername = instance.getConfig().getString("Database.Username");
            String mysqlPassword = instance.getConfig().getString("Database.Password");


            connection = DriverManager.getConnection("jdbc:mysql://" + mysqlIP + ":" + mysqlPort + "/" + mysqlDatabase + "?useSSL=true?autoReconnect=true", mysqlUsername, mysqlPassword);
            System.out.println(Methods.formatText("&fSuccessfully created a connection with MySQL."));
        } catch (Exception error) {
            System.out.println(Methods.formatText("&cFailed to create a connection with MySQL."));
            Debugger.runReport(error);
        }
    }

    public void closeMySQL() {
        if (!instance.getConfig().getBoolean("Database.Activate Mysql Support") || connection == null) {
            return;
        }
        try {
            connection.close();
            System.out.println(Methods.formatText("&fSuccessfully closed the MySQL connection."));
        } catch (Exception error) {
            System.out.println(Methods.formatText("&cFailed to close the MySQL connection."));
            Debugger.runReport(error);
        }
    }

    public void saveRedeem(Player player, String voucher) {
        if (!instance.getConfig().getBoolean("Database.Activate Mysql Support") || connection == null) {
            return;
        }
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        String time = date.toString();
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS redeems (id INT NOT NULL AUTO_INCREMENT, player varchar(120) NOT NULL, voucher varchar(120) NOT NULL, timestamp varchar(120) NOT NULL, PRIMARY KEY (ID));");
            statement.execute("INSERT INTO redeems VALUES (default, '" + player.getName() + "', '" + voucher + "', '" + time + "');");
            statement.close();
            System.out.println(Methods.formatText("&fSuccessfully saved the redeem in the MySQL database."));
        } catch (Exception error) {
            System.out.println(Methods.formatText("&cFailed to save the redeem data in the MySQL database."));
            Debugger.runReport(error);
        }
    }

}