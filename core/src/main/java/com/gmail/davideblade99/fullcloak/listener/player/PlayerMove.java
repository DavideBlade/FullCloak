/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.listener.player;

import com.gmail.davideblade99.fullcloak.FullCloak;
import com.gmail.davideblade99.fullcloak.Messages;
import com.gmail.davideblade99.fullcloak.user.User;
import com.gmail.davideblade99.fullcloak.user.UserManager;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public final class PlayerMove implements Listener {

    private final FullCloak plugin;

    public PlayerMove(final FullCloak instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(final PlayerMoveEvent e) {
        final Location from = e.getFrom();
        final Location to = e.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ() && from.getBlockY() == to.getBlockY())
            return;

        final Player player = e.getPlayer();
        final User fcPlayer = UserManager.getUser(player);
        if (!fcPlayer.isInvisible())
            return;

        final boolean canMove = plugin.getSettings().canMoveWhenInvisible();
        if (canMove && plugin.getSettings().playParticlesOnPlayerMove())
            player.getWorld().playEffect(player.getLocation(), fcPlayer.getEffect(), 5);
        else if (!canMove) {
            player.teleport(new Location(player.getWorld(), from.getX(), from.getY(), from.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            MessageUtil.sendChatMessage(player, Messages.getMessage("No move when invisible"));
        }
    }
}