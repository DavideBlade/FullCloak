/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.listener.inventory;

import com.gmail.davideblade99.fullcloak.inventory.Item;
import com.gmail.davideblade99.fullcloak.inventory.Menu;
import com.gmail.davideblade99.fullcloak.inventory.MenuInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class MenuClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(final InventoryClickEvent e) {
        final Inventory topInv = e.getInventory();

        // If it's not a FullCloak inventory
        if (!(topInv.getHolder() instanceof MenuInventoryHolder))
            return;
        if (e.getClickedInventory() == null)
            return;

        final ItemStack clickedItem = e.getCurrentItem(); // Item that was clicked
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;

        e.setCancelled(true);

        final Menu menu = ((MenuInventoryHolder) topInv.getHolder()).getMenu(); // Menu clicked
        final Player player = (Player) e.getWhoClicked(); // Player that clicked the item
        for (Item item : menu.getItems()) {
            if (e.getSlot() == item.getSlot()) {
                final String command = item.getCommand();

                if (command != null && !command.isEmpty())
                    Bukkit.dispatchCommand(player, command);

                if (!item.keepOpen())
                    player.closeInventory();

                break;
            }
        }
    }
}