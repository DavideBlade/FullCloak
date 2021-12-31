/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.listener.player;

import com.gmail.davideblade99.fullcloak.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {
        UserManager.addPlayer(event.getPlayer());
    }
}