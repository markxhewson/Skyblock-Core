package xyz.lotho.me.skyblock.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import xyz.lotho.me.skyblock.command.Command;
import xyz.lotho.me.skyblock.command.CommandSource;
import xyz.lotho.me.skyblock.command.impl.island.*;

public class IslandCommand extends Command {

    public IslandCommand() {
        super("island", null);

        this.setDescription("Create an island.");
        this.addAliases("is");

        this.removePermittedSources(CommandSource.COMMAND_BLOCK, CommandSource.CONSOLE);

        this.setMinArgs(0);
        this.setMaxArgs(0);

        this.addSubCommands(
                new IslandCreateCommand(),
                new IslandHomeCommand(),
                new IslandResetCommand(),
                new IslandMembersCommand(),
                new IslandInviteCommand(),
                new IslandAcceptCommand()
        );
    }

    @Override
    protected void execute(CommandSender sender, String... args) throws Exception {
        this.getSubCommands().forEach(command -> {
            sender.sendMessage(ChatColor.BLUE + "/is " + command.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.GRAY + command.getDescription());
        });

        // todo: gui
    }
}
