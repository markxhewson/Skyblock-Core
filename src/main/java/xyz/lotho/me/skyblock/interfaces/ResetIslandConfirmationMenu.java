package xyz.lotho.me.skyblock.interfaces;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.interfaces.util.Menu;
import xyz.lotho.me.skyblock.managers.member.Member;
import xyz.lotho.me.skyblock.utils.chat.Chat;
import xyz.lotho.me.skyblock.utils.item.ItemBuilder;

public class ResetIslandConfirmationMenu extends Menu {

    private Skyblock instance;

    public ResetIslandConfirmationMenu(Skyblock instance) {
        super("Reset Island Confirmation Menu", 27);

        this.instance = instance;

        setItems();
    }

    @Override
    public String getMenuName() {
        return "Confirmation Menu";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setItems() {
        super.getInventory().setItem(12, new ItemBuilder(Material.GREEN_BANNER).displayname("&a&lConfirm").lore("", "&c&lWARNING", "&7All progress for this island will be", "&7permanently lost, are you sure?", "", "&7(( Click to reset island! ))").build());
        super.getInventory().setItem(14, new ItemBuilder(Material.RED_BANNER).displayname("&c&lCancel").lore("", "&7(( Click to cancel island reset! ))").build());
    }

    @Override
    public void handleClick(InventoryClickEvent event) throws Exception {
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (clickedItem == null) return;

        if (clickedItem.getType() == Material.GREEN_BANNER) {
            Member member = this.instance.getMemberManager().getMember(player.getUniqueId());

            member.getIsland().resetTheme();

            player.sendMessage(Chat.color("&a&l<!> &aYou have &nreset&r &ayour island to its default state!"));
            player.closeInventory();
        }
        else if (clickedItem.getType() == Material.RED_BANNER) {
            player.sendMessage(Chat.color("&c&l<!> &cYou have &ncancelled&r &cyour island reset!"));
            player.closeInventory();
        }
    }
}
