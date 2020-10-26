package it.iVirus.premiumdomainrg.bungee;

import com.google.common.io.ByteStreams;
import it.iVirus.premiumdomainrg.bungee.database.Database;
import it.iVirus.premiumdomainrg.bungee.listeners.PlayerListener;
import it.iVirus.premiumdomainrg.bungee.pdcommand.Command;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PremiumDomainRG extends Plugin {

    public static PremiumDomainRG instance;
    private Configuration config;
    private Database connector;
    private File configFile;
    private List<String> premiumDomains = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        createConfig();
        String database = getConfig().getString("MySQL.Database");
        String table = getConfig().getString("MySQL.Table");
        String username = getConfig().getString("MySQL.Username");
        String password = getConfig().getString("MySQL.Password");
        String host = getConfig().getString("MySQL.Host");
        boolean ssl = getConfig().getBoolean("MySQL.SSL");
        int port = getConfig().getInt("MySQL.Port");
        connector = new Database(database, table, username, password, host, port, ssl);
        connector.setup();
        premiumDomains.addAll(config.getStringList("DomainPremium"));
        getConfig().getStringList("Premium").forEach(connector::newPremium);
        getProxy().getPluginManager().registerCommand(this, new Command("premium"));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerListener());



        getProxy().getScheduler().schedule (this, () -> {
            try {
                connector.getConnection().close();
                connector.resumeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        },10 , getConfig().getInt("resumeTask"), TimeUnit.SECONDS);

    }

    public List<String> getPremiumDomains(){
        return premiumDomains;
    }

    public void createConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(getConfigFile())) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error on creating config file", e);
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public static PremiumDomainRG getInstance() {
        return instance;
    }

    public Database getConnector() {
        return connector;
    }

    public File getConfigFile(){
        return configFile;
    }

    public void save(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(getConfig(), getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload(){
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
