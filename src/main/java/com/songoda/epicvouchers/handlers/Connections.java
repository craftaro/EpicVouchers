package com.songoda.epicvouchers.handlers;

import com.craftaro.core.utils.TextUtils;
import com.songoda.epicvouchers.EpicVouchers;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;

public class Connections {
    private final EpicVouchers instance;
    private Connection connection;

    public Connections(EpicVouchers instance) {
        this.instance = instance;
    }

    public void openMySQL() {
        if (!this.instance.getConfig().getBoolean("Database.Activate Mysql Support") || this.connection == null) {
            return;
        }
        try {
            String mysqlIP = this.instance.getConfig().getString("Database.IP");
            String mysqlPort = this.instance.getConfig().getString("Database.PORT");
            String mysqlDatabase = this.instance.getConfig().getString("Database.Database Name");
            String mysqlUsername = this.instance.getConfig().getString("Database.Username");
            String mysqlPassword = this.instance.getConfig().getString("Database.Password");

            this.connection = DriverManager.getConnection("jdbc:mysql://" + mysqlIP + ":" + mysqlPort + "/" + mysqlDatabase + "?useSSL=true?autoReconnect=true", mysqlUsername, mysqlPassword);
            System.out.println(TextUtils.formatText("&fSuccessfully created a connection with MySQL."));
        } catch (Exception error) {
            System.out.println(TextUtils.formatText("&cFailed to create a connection with MySQL."));
            error.printStackTrace();
        }
    }

    public void closeMySQL() {
        if (!this.instance.getConfig().getBoolean("Database.Activate Mysql Support") || this.connection == null) {
            return;
        }
        try {
            this.connection.close();
            System.out.println(TextUtils.formatText("&fSuccessfully closed the MySQL connection."));
        } catch (Exception error) {
            System.out.println(TextUtils.formatText("&cFailed to close the MySQL connection."));
            error.printStackTrace();
        }
    }

    public void saveRedeem(Player player, String voucher) {
        if (!this.instance.getConfig().getBoolean("Database.Activate Mysql Support") || this.connection == null) {
            return;
        }
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        String time = date.toString();
        try {
            Statement statement = this.connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS redeems (id INT NOT NULL AUTO_INCREMENT, player varchar(120) NOT NULL, voucher varchar(120) NOT NULL, timestamp varchar(120) NOT NULL, PRIMARY KEY (ID));");
            statement.execute("INSERT INTO redeems VALUES (default, '" + player.getName() + "', '" + voucher + "', '" + time + "');");
            statement.close();
            System.out.println(TextUtils.formatText("&fSuccessfully saved the redeem in the MySQL database."));
        } catch (Exception error) {
            System.out.println(TextUtils.formatText("&cFailed to save the redeem data in the MySQL database."));
            error.printStackTrace();
        }
    }
}
