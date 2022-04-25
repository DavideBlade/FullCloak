/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak.listener.player;

import com.gmail.davideblade99.fullcloak.FullCloak;
import com.gmail.davideblade99.fullcloak.Messages;
import com.gmail.davideblade99.fullcloak.Settings;
import com.gmail.davideblade99.fullcloak.event.player.BecomeVisibleEvent;
import com.gmail.davideblade99.fullcloak.user.User;
import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public final class BecomeVisible implements Listener {

    private final FullCloak plugin;

    public BecomeVisible(@NotNull final FullCloak instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBecomeVisible(final BecomeVisibleEvent event) {
        final User fcPlayer = event.getUser();
        final Player player = fcPlayer.getPlayer();
        final Settings settings = plugin.getSettings();

        // Show hidden player
        for (Player p : Bukkit.getOnlinePlayers())
            p.showPlayer(player);

        player.setFlySpeed(0.2F);
        player.setWalkSpeed(0.2F);

        fcPlayer.setInvisible(false);

        fcPlayer.setCooldownEndTime(System.currentTimeMillis() + settings.getCooldownTime() * 1000);

        if (event.isMaxTimeReached())
            MessageUtil.sendChatMessage(player, Messages.getMessage("Max time reached"));

        if (settings.messageWhenBecomeVisible())
            MessageUtil.sendChatMessage(player, Messages.getMessage("Show player"));
    }
}