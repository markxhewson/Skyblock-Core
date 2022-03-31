package xyz.lotho.me.skyblock.command.impl.island;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.command.Command;
import xyz.lotho.me.skyblock.command.CommandSource;
import xyz.lotho.me.skyblock.command.CompletableCommand;
import xyz.lotho.me.skyblock.managers.island.invite.InviteManager;
import xyz.lotho.me.skyblock.managers.island.member.IslandRole;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.chat.Chat;

public class IslandInviteCommand extends Command implements CompletableCommand {

    public IslandInviteCommand() {
        super("invite", null);

        this.setDescription("Invite your friends to your island.");

        this.removePermittedSources(CommandSource.CONSOLE, CommandSource.COMMAND_BLOCK);

        this.setMinArgs(1);
        this.setMaxArgs(1);
    }

    @Override
    protected void execute(CommandSender sender, String... args) throws Exception {
        Player player = (Player) sender;
        Member member = Skyblock.getInstance().getMemberManager().getMember(player.getUniqueId());

        if (!member.hasIsland()) {
            player.sendMessage(Chat.color("&c&l<!> &cYou do not have an island! &7/island create &cto create one!"));
            return;
        }

        Player invitedPlayer = Skyblock.getInstance().getServer().getPlayer(args[0]);
        if (invitedPlayer == null) {
            player.sendMessage(Chat.color("&c&l<!> &c" + args[0] + " is not online!"));
            return;
        }

        if (member.getIsland().isMember(invitedPlayer.getUniqueId())) {
            player.sendMessage(Chat.color("&c&l<!> &f" + invitedPlayer.getName() + " &cis already a member of your island!"));
            return;
        }

        InviteManager inviteManager = Skyblock.getInstance().getInviteManager();
        if (inviteManager.isAlreadyInvited(member.getIsland())) {
            player.sendMessage(Chat.color("&c&l<!> &cThis player already has a pending invite to join your island."));
            return;
        }

        inviteManager.createIslandInvite(player.getUniqueId(), invitedPlayer.getUniqueId(), member.getIsland());

        invitedPlayer.sendMessage(Chat.color("&a&l<!> &aYou have received an invite from " + player.getName() + " &ato join their island! &7/island accept &ato accept!"));
    }

    @Override
    public LiteralCommandNode<?> getCompletions() {
        return LiteralArgumentBuilder.literal("island")
                .then(LiteralArgumentBuilder.literal("invite"))
                .build();
    }
}
