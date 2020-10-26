package it.iVirus.premiumdomainrg.bungee.listeners;

import com.google.common.base.Charsets;
import it.iVirus.premiumdomainrg.bungee.PremiumDomainRG;
import it.iVirus.premiumdomainrg.bungee.util.Colors;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class PlayerListener implements Listener {
    private PremiumDomainRG plugin = PremiumDomainRG.getInstance();
    private List<String> premium;

    @EventHandler
    public void onProxyLogin(PreLoginEvent e) {
        premium = plugin.getConfig().getStringList("Premium");
        String playerDomain = e.getConnection().getVirtualHost().getHostString().toLowerCase();
        if ((premium.contains(e.getConnection().getName())) && (PremiumDomainRG.getInstance().getPremiumDomains().contains(playerDomain))) {
            e.getConnection().setOnlineMode(true);
        }
        if ((premium.contains(e.getConnection().getName())) && !(PremiumDomainRG.getInstance().getPremiumDomains().contains(playerDomain))) {
            e.getConnection().disconnect(new TextComponent(Colors.color(plugin.getConfig().getString("JoinFromPremium")
                    .replaceAll("%domain_premium%", PremiumDomainRG.getInstance().getPremiumDomains().get(0)))));
            return;
        }
        if (!(premium.contains(e.getConnection().getName())) && (PremiumDomainRG.getInstance().getPremiumDomains().contains(playerDomain))) {
            e.getConnection().disconnect(new TextComponent(Colors.color(plugin.getConfig().getString("JoinFromCrack")
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
