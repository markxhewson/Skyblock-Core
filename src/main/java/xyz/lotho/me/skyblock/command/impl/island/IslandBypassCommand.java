package xyz.lotho.me.skyblock.command.impl.island;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.command.Command;
import xyz.lotho.me.skyblock.command.CommandSource;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.chat.Chat;

public class IslandBypassCommand extends Command {
    public IslandBypassCommand() {
        super("bypass", "skyblock.island.bypass");

        this.setDescription("Bypass island world protection.");

        this.setMinArgs(0);
        this.setMaxArgs(2);
    }

    //Syntax: /island bypass [setting] [player]
    @Override
    protected void execute(CommandSender sender, String... args) {
        Player target = null;
        Boolean setting = null; //Using wrapped value it can be null

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "on":
                case "true":
                case "enable":
                case "enabled":
                    setting = true;
                    break;
                case "off":
                case "false":
                case "disable":
                case "disabled":
                    setting = false;
                    break;
            }

            if (args.length > 1 || setting == null) {
                int arg = (args.length > 1 ? 1 : 0);
                Player targetPlayer = Skyblock.getInstance().getServer().getPlayer(args[arg]);
                if (targetPlayer == null) {
                    sender.sendMessage(Chat.color("&c&l<!> &c" + args[0] + " is not online!"));
                    return;
                }
                target = targetPlayer;
            }
        }

        if (target == null) {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(Chat.color("&c&l<!> &cYou must specify a player!"));
                return;
            }
        }

        Member member = Skyblock.getInstance().getMemberManager().getMember(target);

        if (setting == null) {
            setting = !member.isIslandProtectionBypass();
        }

        if (setting == member.isIslandProtectionBypass()) {
            sender.sendMessage(Chat.color("&c&l<!> &c" + (target == sender ? "Your" : target.getName() + "'s") + " island build bypass mode is already " + (setting ? "on" : "off") + "!"));
            return;
        }

        member.setIslandProtectionBypass(setting);

        sender.sendMessage(Chat.color((setting ? "&a" : "&c") + "&l<!> " + (setting ? "&a" : "&c") + (target == sender ? "Your" : target.getName() + "'s") + " island build bypass mode has been turned " + (setting ? "on" : "off") + "."));
    }
}
