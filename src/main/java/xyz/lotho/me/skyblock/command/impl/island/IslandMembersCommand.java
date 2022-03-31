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
import xyz.lotho.me.skyblock.interfaces.IslandMembersMenu;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.chat.Chat;

public class IslandMembersCommand extends Command implements CompletableCommand {

    public IslandMembersCommand() {
        super("members", null);

        this.setDescription("View your island members.");

        this.removePermittedSources(CommandSource.CONSOLE, CommandSource.COMMAND_BLOCK);

        this.setMinArgs(0);
        this.setMaxArgs(0);
    }

    @Override
    protected void execute(CommandSender sender, String... args) throws Exception {
        Player player = (Player) sender;
        Member member = Skyblock.getInstance().getMemberManager().getMember(player.getUniqueId());

        if (!member.hasIsland()) {
            player.sendMessage(Chat.color("&c&l<!> &cYou do not have an island! Create one using /island create!"));
            return;
        }

        new IslandMembersMenu(Skyblock.getInstance(), member.getIsland().getIslandMemberManager()).open(player);
    }

    @Override
    public LiteralCommandNode<?> getCompletions() {
        return LiteralArgumentBuilder.literal("island")
                .then(LiteralArgumentBuilder.literal("create"))
                .build();
    }
}
