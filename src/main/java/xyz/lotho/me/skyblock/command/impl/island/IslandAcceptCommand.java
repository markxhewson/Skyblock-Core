package xyz.lotho.me.skyblock.command.impl.island;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.command.Command;
import xyz.lotho.me.skyblock.command.CommandSource;
import xyz.lotho.me.skyblock.command.CompletableCommand;
import xyz.lotho.me.skyblock.managers.island.invite.Invite;
import xyz.lotho.me.skyblock.managers.island.invite.InviteManager;
import xyz.lotho.me.skyblock.managers.island.member.IslandRole;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.chat.Chat;

public class IslandAcceptCommand extends Command implements CompletableCommand {

    public IslandAcceptCommand() {
        super("accept", null);

        this.setDescription("Accept an invite to an island.");

        this.removePermittedSources(CommandSource.CONSOLE, CommandSource.COMMAND_BLOCK);

        this.setMinArgs(0);
        this.setMaxArgs(0);
    }

    @Override
    protected void execute(CommandSender sender, String... args) throws Exception {
        Player player = (Player) sender;
        Member member = Skyblock.getInstance().getMemberManager().getMember(player.getUniqueId());
        InviteManager inviteManager = Skyblock.getInstance().getInviteManager();

        if (member.hasIsland()) {
            player.sendMessage(Chat.color("&c&l<!> &cYou are already apart of an island! &7/island leave &cto leave and join a new one!"));
            return;
        }

        if (!inviteManager.hasInvite(player.getUniqueId())) {
            player.sendMessage(Chat.color("&c&l<!> &cYou have not been invited to join any islands!"));
            return;
        }

        Invite invite = inviteManager.getInvite(player.getUniqueId());
        OfflinePlayer inviter = Skyblock.getInstance().getServer().getOfflinePlayer(invite.getInviter());

        member.setIsland(invite.getIslandToJoin());
        member.getIsland().getIslandMemberManager().addMember(player.getUniqueId(), IslandRole.ISLAND_MEMBER);

        Skyblock.getInstance().getInviteManager().removeInvite(player.getUniqueId());

        member.getIsland().getIslandMemberManager().getIslandMembers().forEach(((uuid, islandMember) -> {
            Player islandPlayer = Skyblock.getInstance().getServer().getPlayer(uuid);
            if (islandPlayer == null) return;

            islandPlayer.sendMessage(Chat.color("&a&l<!> &f" + player.getName() + " &ahas accepted &f" + inviter.getName() + "'s &ainvite to join the island!"));
        }));

        Skyblock.getInstance().getServer().getScheduler().runTaskAsynchronously(Skyblock.getInstance(), () -> member.getIsland().save());
    }

    @Override
    public LiteralCommandNode<?> getCompletions() {
        return LiteralArgumentBuilder.literal("island")
                .then(LiteralArgumentBuilder.literal("accept"))
                .build();
    }
}
