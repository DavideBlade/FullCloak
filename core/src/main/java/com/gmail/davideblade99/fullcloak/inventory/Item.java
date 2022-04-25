/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public final class Item extends ItemStack {

    private final byte slot;
    private final String command;
    private final boolean keepOpen;

    public Item(@NotNull final Material type, final short damage, @Nullable final String displayName, @Nullable final String lore, final byte slot, @Nullable final String command, final boolean keepOpen) {
        super(type, 1, damage);

        this.slot = slot;
        this.command = command;
        this.keepOpen = keepOpen;

        final ItemMeta meta = super.getItemMeta();
        if (meta == null) // Item (AIR) hasn't item meta
            return;

        if (displayName != null)
            meta.setDisplayName(displayName);
        if (lore != null)
            meta.setLore(Collections.singletonList(lore));

        super.setItemMeta(meta);
    }


    public byte getSlot() {
        return slot;
    }

    @Nullable
    public String getCommand() {
        return command;
    }

    public boolean keepOpen() {
        return keepOpen;
    }
}
