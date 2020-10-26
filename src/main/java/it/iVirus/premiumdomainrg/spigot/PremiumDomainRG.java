package it.iVirus.premiumdomainrg.spigot;

import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class PremiumDomainRG extends JavaPlugin implements Listener {
    public static PremiumDomainRG instance;
    private Database connector;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        String database = getConfig().getString("MySQL.Database");
        String table = getConfig().getString("MySQL.Table");
        String username = getConfig().getString("MySQL.Username");
        String password = getConfig().getString("MySQL.Password");
        String host = getConfig().getString("MySQL.Host");
        boolean ssl = getConfig().getBoolean("MySQL.SSL");
        int port = getConfig().getInt("MySQL.Port");
        connector = new Database(database, table, username, password, host, port, ssl);
        connector.setup();
        new BukkitRunnable() {
            @Override
            public void run(){
                try {
                    connector.getConnection().close();
                    connector.resumeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(this, 0L, getConfig().getInt("resumeTask") * 20);

        getLogger().info("Enabled!");
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        if (connector.checkPremium(e.getPlayer().getName())) {
            AuthMeApi.getInstance().forceLogin(e.getPlayer());
        }
    }


}
