package xyz.lotho.me.skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.managers.island.Island;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.chat.Chat;

public class IslandProtectionListener implements Listener {

    private final Skyblock instance;

    public IslandProtectionListener(Skyblock instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onIslandWorldBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Member member = this.instance.getMemberManager().getMember(player.getUniqueId());

        if (member.isIslandProtectionBypass()) return;

        if (!member.hasIsland()) {
            if (player.getWorld().getName().equalsIgnoreCase("islands")) {
                event.setCancelled(true);
            }
            return;
        }
        if (!player.getWorld().getName().equalsIgnoreCase("islands")) return;

        Island island = member.getIsland();

        if (!island.isBlockWithinBounds(event.getBlock())) {
            event.setCancelled(true);
            player.sendMessage(Chat.color("&c&l<!> &cYou cannot modify blocks outside your island."));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onIslandWorldPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Member member = this.instance.getMemberManager().getMember(player.getUniqueId());

        if (member.isIslandProtectionBypass()) return;

        if (!member.hasIsland()) {
            if (player.getWorld().getName().equalsIgnoreCase("islands")) {
                event.setCancelled(true);
            }
            return;
        }
        if (!player.getWorld().getName().equalsIgnoreCase("islands")) return;

        Island island = member.getIsland();

        if (!island.isBlockWithinBounds(event.getBlock())) {
            event.setCancelled(true);
            player.sendMessage(Chat.color("&c&l<!> &cYou cannot modify blocks outside your island."));
        }
    }
}
