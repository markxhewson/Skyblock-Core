package xyz.lotho.me.skyblock.command.exception;

import xyz.lotho.me.skyblock.command.Command;

public class IllegalCommandUsageException extends Exception {

    private final String usage;

    public IllegalCommandUsageException(Command command) {
        super("Incorrect command usage for " + command.getName() + "! Correct usage: " + command.getUsage());

        this.usage = command.getUsage();
    }

    public String getUsage() {
        return this.usage;
    }
}
