/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class MenuInventoryHolder implements InventoryHolder {

    private final Menu menu;

    public MenuInventoryHolder(@NotNull final Menu menu) {
        this.menu = menu;
    }

    @Override
    public Inventory getInventory() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Menu getMenu() {
        return menu;
    }
}
