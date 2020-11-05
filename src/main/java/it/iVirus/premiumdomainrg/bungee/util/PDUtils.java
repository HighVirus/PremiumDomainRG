package it.iVirus.premiumdomainrg.bungee.util;

import it.iVirus.premiumdomainrg.bungee.PremiumDomainRG;
import net.md_5.bungee.api.ChatColor;

public class PDUtils {
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static Boolean hasLetter(String string){
        char[] chars = string.toCharArray();
        for(char c : chars)
            if(Character.isLetter(c))
                return true;
        return false;
    }

    public static boolean addPremium(String player){
        if (PremiumDomainRG.getInstance().getPremiumPlayers().contains(player))
            return false;
        else{
            PremiumDomainRG.getInstance().getPremiumPlayers().add(player);
            PremiumDomainRG.getInstance().getConfig().set("Premium", PremiumDomainRG.getInstance().getPremiumPlayers());
            PremiumDomainRG.getInstance().save();
            PremiumDomainRG.getInstance().reloadConfig();
            PremiumDomainRG.getInstance().getConnector().newPremiumDb(player);
            return true;
        }
    }

    public static boolean removePremium(String player){
        if (!PremiumDomainRG.getInstance().getPremiumPlayers().contains(player))
            return false;
        else{
            PremiumDomainRG.getInstance().getPremiumPlayers().remove(player);
            PremiumDomainRG.getInstance().getConfig().set("Premium", PremiumDomainRG.getInstance().getPremiumPlayers());
            PremiumDomainRG.getInstance().save();
            PremiumDomainRG.getInstance().reloadConfig();
            PremiumDomainRG.getInstance().getConnector().removePremiumDb(player);
            return true;
        }
    }
}
