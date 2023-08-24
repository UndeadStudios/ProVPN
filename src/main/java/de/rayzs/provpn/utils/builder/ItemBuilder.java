package de.rayzs.provpn.utils.builder;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.*;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final List<String> loreList;
    private final ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.loreList = new ArrayList<>();
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1, (short)0);
        this.itemMeta = this.itemStack.getItemMeta();
        this.loreList = new ArrayList<>();
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setAmount(int value) {
        this.itemStack.setAmount(value);
        return this;
    }

    public ItemBuilder setGlow() {
        this.itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setGlow(boolean bool) {
        if(!bool) return this;
        setGlow();
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.loreList.add(lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.itemStack.setDurability(durability);
        return this;
    }

    public ItemStack build() {
        if (!this.loreList.isEmpty())
            this.itemMeta.setLore(this.loreList);
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}