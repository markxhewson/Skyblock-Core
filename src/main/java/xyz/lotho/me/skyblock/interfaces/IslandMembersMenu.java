package xyz.lotho.me.skyblock.interfaces;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.interfaces.util.Menu;
import xyz.lotho.me.skyblock.managers.island.member.IslandMemberManager;
import xyz.lotho.me.skyblock.managers.island.member.IslandRole;
import xyz.lotho.me.skyblock.utils.item.ItemBuilder;

import java.util.concurrent.atomic.AtomicInteger;

public class IslandMembersMenu extends Menu {

    private final Skyblock instance;
    private final IslandMemberManager islandMemberManager;

    public IslandMembersMenu(Skyblock instance, IslandMemberManager islandMemberManager) {
        this.instance = instance;
        this.islandMemberManager = islandMemberManager;
    }

    @Override
    public String getMenuName() {
        return "Island Members";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setItems() {
        AtomicInteger startingIndex = new AtomicInteger(10);

        this.islandMemberManager.getIslandMembers().forEach((uuid, islandMember) -> {
            OfflinePlayer offlinePlayer = this.instance.getServer().getOfflinePlayer(uuid);
            IslandRole islandRole = islandMember.getIslandRole();

            this.getInventory().setItem(startingIndex.getAndIncrement(),
                    new ItemBuilder(Material.PLAYER_HEAD)
                            .setDisplayname("&a" + offlinePlayer.getName())
                            .lore("&7Role: " + islandRole.getColor() + islandRole.getIslandRole()).build()
            );
        });
    }

    @Override
    public void handleClick(InventoryClickEvent event) throws Exception {}
}
