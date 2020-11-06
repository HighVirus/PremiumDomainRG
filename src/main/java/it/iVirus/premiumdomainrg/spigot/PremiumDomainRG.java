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
        connector = new Database();
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

    public static Boolean hasLetter(String string){
        char[] chars = string.toCharArray();
        for(char c : chars)
            if(Character.isLetter(c))
                return true;
        return false;
    }

    public static PremiumDomainRG getInstance(){
        return instance;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        if (connector.checkPremium(e.getPlayer().getName())) {
            AuthMeApi.getInstance().forceLogin(e.getPlayer());
        }
    }


}
