package it.iVirus.premiumdomainrg.bungee.database;

import it.iVirus.premiumdomainrg.bungee.PremiumDomainRG;
import it.iVirus.premiumdomainrg.bungee.util.PDUtils;

import java.sql.*;

public class Database {

    private String database, table, username, password, host;
    private boolean ssl;
    private int port;

    public Database() {
        setDatabase();
        setUsername();
        setPassword();
        setHost();
        setTable();
        setPort();
        setSSL();
    }


    private Connection connection;

    public void setup() {
        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database +
                    "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=" + this.ssl, this.username, this.password));
            PreparedStatement statement = getConnection().prepareStatement("create TABLE if not exists `" + this.table + "` " +
                    "(ID int NOT NULL AUTO_INCREMENT,`Player` VARCHAR(100),PRIMARY KEY (ID), unique (Player))");
            statement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void newPremiumDb(String player) {
        try (PreparedStatement statement = getConnection().prepareStatement("INSERT IGNORE INTO " + this.table + " (Player) VALUES (?)")){
            statement.setString(1, player);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePremiumDb(String player) {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM " + this.table + " WHERE Player=?")) {
            statement.setString(1, player);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resumeConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database +
                    "?autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=" + this.ssl, this.username, this.password));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void setDatabase() {
        this.database = PremiumDomainRG.getInstance().getConfig().getString("MySQL.Database");
    }

    private void setUsername() {
        this.username = PremiumDomainRG.getInstance().getConfig().getString("MySQL.Username");
    }

    private void setPassword() {
        this.password = PremiumDomainRG.getInstance().getConfig().getString("MySQL.Password");
    }

    private void setHost() {
        this.host = PremiumDomainRG.getInstance().getConfig().getString("MySQL.Host");
    }

    private void setTable() {
        this.table = PremiumDomainRG.getInstance().getConfig().getString("MySQL.Table");
    }

    private void setPort() {
        if (PDUtils.hasLetter(PremiumDomainRG.getInstance().getConfig().getString("MySQL.Port"))) {
            PremiumDomainRG.getInstance().getLogger().severe("Port cannot contain letters!");
            PremiumDomainRG.getInstance().getLogger().severe("Stopping...");
            PremiumDomainRG.getInstance().getProxy().stop();
        } else
            this.port = PremiumDomainRG.getInstance().getConfig().getInt("MySQL.Port");
    }

    private void setSSL() {
        if (!String.valueOf(PremiumDomainRG.getInstance().getConfig().get("MySQL.SSL")).equalsIgnoreCase("true")
                && !String.valueOf(PremiumDomainRG.getInstance().getConfig().get("MySQL.SSL")).equalsIgnoreCase("false")) {
            PremiumDomainRG.getInstance().getLogger().severe("SSL cannot be different from 'true' or 'false'!");
            PremiumDomainRG.getInstance().getLogger().severe("Stopping...");
            PremiumDomainRG.getInstance().getProxy().stop();
        } else
            this.ssl = PremiumDomainRG.getInstance().getConfig().getBoolean("MySQL.SSL");
    }


}
