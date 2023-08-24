package de.rayzs.provpn.utils.builder;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.Bukkit;

public class InventoryBuilder {

    private Inventory inventory;

    private InventoryType inventoryType;
    private String inventoryName = "Generated Inventory | " + Math.random();
    private int size = 1;

    public InventoryBuilder setName(String inventoryName) {
        this.inventoryName = inventoryName;
        return this;
    }

    public InventoryBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public InventoryBuilder setType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        return this;
    }

    public InventoryBuilder create() {
        boolean resizeableInventory = inventoryType == null;
        inventory = resizeableInventory
                ? Bukkit.createInventory(null, size, inventoryName)
                : Bukkit.createInventory(null, inventoryType, inventoryName);
        return this;
    }

    public InventoryBuilder addItem(ItemStack itemStack) {
        inventory.addItem(itemStack);
        return this;
    }

    public InventoryBuilder setItem(ItemStack itemStack, int slot) {
        inventory.setItem(slot, itemStack);
        return this;
    }

    public void open(Player player) {
        if(inventory == null) return;
        player.openInventory(inventory);
    }

    public Inventory getInventory() { return inventory; }
    public String getInventoryName() { return inventoryName; }
    public ItemStack[] getContent() { return inventory.getContents(); }
    public int getSize() { return size; }
}
