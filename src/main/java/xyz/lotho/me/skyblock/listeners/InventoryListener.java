package xyz.lotho.me.skyblock.listeners;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.interfaces.util.Menu;

public class InventoryListener implements Listener {

    private Skyblock instance;

    public InventoryListener(Skyblock instance) {
        this.instance = instance;
    }

    @SneakyThrows
    @EventHandler(priority = EventPriority.HIGH)
    public void handleInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof Menu)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null) return;

        Menu menu = (Menu) holder;
        menu.handleClick(event);
    }
}
