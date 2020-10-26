package it.iVirus.premiumdomainrg.spigot;

import java.sql.*;

public class Database {

    private String database, table, username, password, host;
    boolean ssl;
    private int port;

    public Database(String database, String table, String username, String password, String host, int port, boolean ssl) {
        setDatabase(database);
        setUsername(username);
        setPassword(password);
        setHost(host);
        setTable(table);
        setPort(port);
        setSSL(ssl);
    }


    private Connection connection;

    public void setup() {
        try {
            synchronized (this) {

                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase() + "?characterEncoding=latin1&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=" + getSSL(), getUsername(), getPassword()));
                PreparedStatement statement = getConnection().prepareStatement("create TABLE if not exists `" + getTable() + "` " +
                        "(ID int NOT NULL AUTO_INCREMENT,`Player` VARCHAR(100),PRIMARY KEY (ID), unique (Player))");
                statement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void resumeConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabase() + "?characterEncoding=latin1&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=" + getSSL(), getUsername(), getPassword()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPremium(String player) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * from " + getTable() + " where Player = ?");
            statement.setString(1, player);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDatabase() {
        return this.database;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getHost() {
        return this.host;
    }

    public String getTable() {
        return this.table;
    }

    public int getPort() {
        return this.port;
    }

    public boolean getSSL() {
        return this.ssl;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

}
