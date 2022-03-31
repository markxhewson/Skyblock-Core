package xyz.lotho.me.skyblock.managers.island.member;

import org.bukkit.ChatColor;

public enum IslandRole {

    ISLAND_OWNER("Island Owner", ChatColor.RED),
    ISLAND_ADMIN("Island Admin", ChatColor.GOLD),
    ISLAND_MEMBER("Island Member", ChatColor.GRAY);


    private final String islandRole;
    private final ChatColor color;

    private IslandRole(String islandRole, ChatColor color) {
        this.islandRole = islandRole;
        this.color = color;
    }

    public String getIslandRole() {
        return this.islandRole;
    }

    public ChatColor getColor() {
        return this.color;
    }
}
