/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Menu {

    private final String title;
    private final byte size;
    private final List<Item> items;

    public Menu(@NotNull final String title, final byte size, @NotNull final List<Item> items) {
        this.title = title;
        this.size = size;
        this.items = items;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    @NotNull
    public Inventory getInventory() {
        final Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), size, title);

        for (Item item : items) {
            if(inventory.getItem(item.getSlot()) != null) // If more than one item has been set in the same slot, show only the first one
                continue;

            inventory.setItem(item.getSlot(), item);
        }

        return inventory;
    }

    @NotNull
    public List<Item> getItems() {
        return items;
    }
}
