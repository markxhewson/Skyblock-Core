package xyz.lotho.me.skyblock.command;

import com.mojang.brigadier.tree.LiteralCommandNode;

public interface CompletableCommand {
    /**
     * @return All completions for your command.
     */
    LiteralCommandNode<?> getCompletions();
}
