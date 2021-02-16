package it.iVirus.premiumdomainrg.bungee.pdcommand;

import it.iVirus.premiumdomainrg.bungee.PremiumDomainRG;
import it.iVirus.premiumdomainrg.bungee.util.PDUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class AdminCommand extends Command {
    private final PremiumDomainRG plugin = PremiumDomainRG.getInstance();

    public AdminCommand(String command) {
        super(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("premiumdomainrg.admin")) {
            sender.sendMessage(new TextComponent(PDUtils.color(plugin.getConfig().getString("NoPermission"))));
            return;
        }
        if ((args.length == 0) || (args[0].equalsIgnoreCase("help"))) {
            for (String s : plugin.getConfig().getStringList("AdminHelp"))
                sender.sendMessage(new TextComponent(PDUtils.color(s)));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage(new TextComponent(PDUtils.color(plugin.getConfig().getString("Reload"))));
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 2) {
                for (String s : plugin.getConfig().getStringList("AdminHelp"))
                    sender.sendMessage(new TextComponent(PDUtils.color(s)));
            } else {
                if(PDUtils.removePremium(args[1]))
                    sender.sendMessage(new TextComponent(PDUtils.color(plugin.getConfig().getString("AdminRemoveUser"))));
                else
                    sender.sendMessage(new TextComponent(PDUtils.color(plugin.getConfig().getString("AdminUserNotFound"))));
            }
        }
    }
}
