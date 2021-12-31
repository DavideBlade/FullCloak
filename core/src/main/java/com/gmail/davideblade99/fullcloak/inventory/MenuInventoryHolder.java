/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class MenuInventoryHolder implements InventoryHolder {

    private final GUI menu;

    public MenuInventoryHolder(GUI menu) {
        this.menu = menu;
    }

    @Override
    public Inventory getInventory() {
        throw new UnsupportedOperationException();
    }

    public GUI getMenu() {
        return menu;
    }
}
