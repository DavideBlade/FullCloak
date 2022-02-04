/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.List;

public final class Menu {

    private final String title;
    private final byte size;
    private final List<Item> items;

    public Menu(final String title, final byte size, List<Item> items) {
        this.title = title;
        this.size = size;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public Inventory getInventory() {
        final Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), size, title);

        for (Item item : items) {
            if(inventory.getItem(item.getSlot()) != null) // If more than one item has been set in the same slot, show only the first one
                continue;

            inventory.setItem(item.getSlot(), item);
        }

        return inventory;
    }

    public List<Item> getItems() {
        return items;
    }
}
