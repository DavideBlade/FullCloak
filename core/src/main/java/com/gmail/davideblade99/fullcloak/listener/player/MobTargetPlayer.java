/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.listener.player;

import com.gmail.davideblade99.fullcloak.user.UserManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public final class MobTargetPlayer implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerBecomesTarget(final EntityTargetLivingEntityEvent event) {
        final Entity target = event.getTarget();
        if (!(target instanceof Player)) // If the hit entity isn't a player
            return;

        if (UserManager.getUser((Player) target).isInvisible())
            event.setCancelled(true); // Keep the old target so, if the mob is already targeting the player, will continue to chase him
    }
}