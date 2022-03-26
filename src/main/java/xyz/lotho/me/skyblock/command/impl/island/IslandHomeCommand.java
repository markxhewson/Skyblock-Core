package xyz.lotho.me.skyblock.command.impl.island;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.command.Command;
import xyz.lotho.me.skyblock.command.CommandSource;
import xyz.lotho.me.skyblock.command.CompletableCommand;
import xyz.lotho.me.skyblock.managers.island.Island;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.Chat;

public class IslandHomeCommand extends Command implements CompletableCommand {

    public IslandHomeCommand() {
        super("home", null);

        this.setDescription("Go to your island.");

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

        Island island = member.getIsland();

        player.teleport(island.getCenter());
        player.sendMessage(Chat.color("&a&l<!> &aTransporting you to your island home.."));
    }

    @Override
    public LiteralCommandNode<?> getCompletions() {
        return LiteralArgumentBuilder.literal("island")
                .then(LiteralArgumentBuilder.literal("home"))
                .build();
    }
}
