package xyz.lotho.me.skyblock.command.impl.island;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.command.Command;
import xyz.lotho.me.skyblock.command.CommandSource;
import xyz.lotho.me.skyblock.command.CompletableCommand;
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
            Skyblock.getInstance().getIslandManager().createIsland(player);
            return;
        }

        Player invitedPlayer = Skyblock.getInstance().getServer().getPlayer(args[0]);
        if (invitedPlayer == null) {
            player.sendMessage(Chat.color("&c&l<!> &c" + args[0] + " is not online!"));
            return;
        }

        Member invitedMember = Skyblock.getInstance().getMemberManager().getMember(invitedPlayer.getUniqueId());
        invitedMember.setIsland(member.getIsland());

        member.getIsland().addMember(invitedPlayer.getUniqueId(), IslandRole.ISLAND_MEMBER);
        player.sendMessage(Chat.color("&a&l<!> &aYou have added &f" + invitedPlayer.getName() + " &ato your island."));

        Skyblock.getInstance().getServer().getScheduler().runTaskAsynchronously(Skyblock.getInstance(), () -> member.getIsland().save());
    }

    @Override
    public LiteralCommandNode<?> getCompletions() {
        return LiteralArgumentBuilder.literal("island")
                .then(LiteralArgumentBuilder.literal("invite"))
                .build();
    }
}
