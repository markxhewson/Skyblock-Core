package xyz.lotho.me.skyblock.interfaces.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.lotho.me.skyblock.Skyblock;
import xyz.lotho.me.skyblock.utils.item.ItemBuilder;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    protected final String inventoryName;
    protected final int size;

    protected ItemStack FILLER_GLASS = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build();

    public Menu(String inventoryName, int size) {
        this.inventoryName = inventoryName;
        this.size = size;

        this.inventory = Bukkit.createInventory(null, this.size, this.inventoryName);
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleClick(InventoryClickEvent e) throws Exception;

    public abstract void setItems();

    public void open(Player player) {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setItems();
        this.fillRemainingSlots();

        player.openInventory(this.getInventory());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void fillRemainingSlots(){
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

}