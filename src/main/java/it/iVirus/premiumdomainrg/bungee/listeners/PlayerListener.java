package it.iVirus.premiumdomainrg.bungee.listeners;

import com.google.common.base.Charsets;
import it.iVirus.premiumdomainrg.bungee.PremiumDomainRG;
import it.iVirus.premiumdomainrg.bungee.util.PDUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Field;
import java.util.UUID;

public class PlayerListener implements Listener {
    private final PremiumDomainRG plugin = PremiumDomainRG.getInstance();

    @EventHandler
    public void onProxyLogin(PreLoginEvent e) {
        String playerDomain = e.getConnection().getVirtualHost().getHostString().toLowerCase();
        if ((plugin.getPremiumPlayers().contains(e.getConnection().getName())) && (plugin.getPremiumDomains().contains(playerDomain))) {
            e.getConnection().setOnlineMode(true);
        }
        if ((plugin.getPremiumPlayers().contains(e.getConnection().getName())) && !(PremiumDomainRG.getInstance().getPremiumDomains().contains(playerDomain))) {
            e.getConnection().disconnect(new TextComponent(PDUtils.color(plugin.getConfig().getString("JoinFromPremium")
                    .replaceAll("%domain_premium%", plugin.getPremiumDomains().get(0)))));
            return;
        }
        if (!(plugin.getPremiumPlayers().contains(e.getConnection().getName())) && (PremiumDomainRG.getInstance().getPremiumDomains().contains(playerDomain))) {
            e.getConnection().disconnect(new TextComponent(PDUtils.color(plugin.getConfig().getString("JoinFromCrack")
                    .replaceAll("%domain_crack%", plugin.getConfig().getString("DomainCrack")))));
        }
    }

    @EventHandler(priority = -128)
    public void onPlayerLogin(LoginEvent e){
        PendingConnection c = e.getConnection();
        if (c.isOnlineMode()) {
            UUID offuuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + c.getName()).getBytes(Charsets.UTF_8));
            Class<?> initialHandlerClass = c.getClass();
            Field uniqueIdField;
            try {
                uniqueIdField = initialHandlerClass.getDeclaredField("uniqueId");
                uniqueIdField.setAccessible(true);
                uniqueIdField.set(c, offuuid);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }



}
