package it.iVirus.premiumdomainrg.bungee.pdcommand;

import it.iVirus.premiumdomainrg.bungee.PremiumDomainRG;
import it.iVirus.premiumdomainrg.bungee.util.Colors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Command extends net.md_5.bungee.api.plugin.Command {
    private Set<String> confirm = new HashSet<>();
    private PremiumDomainRG plugin = PremiumDomainRG.getInstance();

    public Command(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (!(cs instanceof ProxiedPlayer)) return;
        ProxiedPlayer p = (ProxiedPlayer) cs;
        if(args.length > 0){
            p.sendMessage(new TextComponent(Colors.color(plugin.getConfig().getString("CorrectUse"))));
            return;
        }
        if (!confirm.contains(p.getName())) {
            confirm.add(p.getName());
            for (String s : plugin.getConfig().getStringList("Confirm")) {
                p.sendMessage(new TextComponent(Colors.color(s)));
            }
        } else {
            confirm.remove(p.getName());
            List<String> playersPremium = plugin.getConfig().getStringList("Premium");
            if (playersPremium.contains(p.getName())) {
                p.sendMessage(new TextComponent(Colors.color(plugin.getConfig().getString("AlreadyPremium"))));
                return;
            }
            playersPremium.add(p.getName());
            plugin.getConfig().set("Premium", playersPremium);
            plugin.save();
            plugin.reload();
            plugin.getConnector().newPremium(p.getName());
            p.disconnect(new TextComponent(Colors.color(plugin.getConfig().getString("LogAgain")
                    .replaceAll("%domain_premium%", PremiumDomainRG.getInstance().getPremiumDomains().get(0)))));
        }
    }
}
