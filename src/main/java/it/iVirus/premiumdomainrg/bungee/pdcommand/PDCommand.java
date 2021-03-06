package it.iVirus.premiumdomainrg.bungee.pdcommand;

import it.iVirus.premiumdomainrg.bungee.PremiumDomainRG;
import it.iVirus.premiumdomainrg.bungee.util.PDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashSet;
import java.util.Set;

public class PDCommand extends Command {
    private Set<String> confirm = new HashSet<>();
    private PremiumDomainRG plugin = PremiumDomainRG.getInstance();

    public PDCommand(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (!(cs instanceof ProxiedPlayer)) return;
        ProxiedPlayer p = (ProxiedPlayer) cs;
        if (args.length > 0) {
            p.sendMessage(new TextComponent(PDUtils.color(plugin.getConfig().getString("CorrectUse"))));
            return;
        }
        if (!confirm.contains(p.getName())) {
            confirm.add(p.getName());
            for (String s : plugin.getConfig().getStringList("Confirm")) {
                p.sendMessage(new TextComponent(PDUtils.color(s)));
            }
        } else {
            confirm.remove(p.getName());
            if (PDUtils.addPremium(p.getName())) {
                p.disconnect(new TextComponent(PDUtils.color(plugin.getConfig().getString("LogAgain")
                        .replaceAll("%domain_premium%", PremiumDomainRG.getInstance().getPremiumDomains().get(0)))));
            } else {
                p.sendMessage(new TextComponent(PDUtils.color(plugin.getConfig().getString("AlreadyPremium"))));
            }
        }
    }
}
